package net.netnook.repeg.examples.todo;

import java.time.LocalDate;
import java.util.List;

import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.RuleEnum;
import net.netnook.repeg.examples.todo.model.Project;
import net.netnook.repeg.examples.todo.model.Task;

public class ParserFactory extends ParserFactoryBase<List<Project>> {

	@Override
	protected RuleEnum getStartRule() {
		return Rules.START;
	}

	public enum Rules implements RuleEnum {
		START {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						zeroOrMore(Proj), //
						EmptyLines, //
						endOfInput() //
				);
			}
		},

		Proj {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						EmptyLines, //
						ProjectNameLine, //
						zeroOrMore(TaskLine) //
				).onSuccess(context -> {
					Project project = new Project();
					project.setName(context.get(0));

					int count = context.size() - 1;
					for (int i = 0; i < count; i++) {
						project.addTask(context.get(i + 1));
					}

					context.replaceWith(project);
				});
			}
		},

		ProjectNameLine {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						ProjectName, //
						endOfLineOrInput() //
				);
			}
		},

		ProjectName {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore(crlf().not()) //
						.onSuccess(context -> context.replaceWith(context.getCurrentText().toString().trim()));
			}
		},

		TaskLine {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						oneOrMore(horizontalWhitespace()), //
						DoneField, //
						DueDateField, //
						DescriptionField, //
						endOfLineOrInput() //
				).onSuccess(context -> {
					Boolean done = context.get(0);
					LocalDate dueDate = context.get(1);
					String description = context.get(2);

					Task task = new Task();
					task.setDone(done);
					task.setDueDate(dueDate);
					task.setDescription(description);
					context.replaceWith(task);
				});
			}
		},

		DoneField {
			@Override
			public ExpressionBuilder expression() {
				return optional( //
						sequence( //
								string("(x)").onSuccess(push(Boolean.TRUE)), //
								oneOrMore(horizontalWhitespace()) //
						) //
				).onSuccess(pushIfEmpty(Boolean.FALSE));
			}
		},

		DueDateField {
			@Override
			public ExpressionBuilder expression() {
				return optional( //
						sequence( //
								repeat(4, characterInRange('0', '9')).onSuccess(pushTextAsInteger()), //
								one('-'), //
								repeat(2, characterInRange('0', '9')).onSuccess(pushTextAsInteger()), //
								one('-'), //
								repeat(2, characterInRange('0', '9')).onSuccess(pushTextAsInteger()), //
								oneOrMore(horizontalWhitespace()) //
						).onSuccess(context -> {
							int year = context.get(0);
							int month = context.get(1);
							int day = context.get(2);
							context.replaceWith(LocalDate.of(year, month, day));
						}) //
				).onSuccess(pushIfEmpty(null));
			}
		},

		DescriptionField {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore(crlf().not()) //
						.onSuccess(context -> context.push(context.getCurrentText().toString().trim()));
			}
		},

		EmptyLines {
			@Override
			public ExpressionBuilder expression() {
				return zeroOrMore( //
						sequence( //
								zeroOrMore(horizontalWhitespace()), //
								one(crlf()) //
						) //
				);
			}
		};
	}
}
