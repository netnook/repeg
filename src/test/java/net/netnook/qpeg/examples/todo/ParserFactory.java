package net.netnook.qpeg.examples.todo;

import java.time.LocalDate;

import net.netnook.qpeg.ParserFactoryBase;
import net.netnook.qpeg.examples.todo.model.Project;
import net.netnook.qpeg.examples.todo.model.Task;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;

public class ParserFactory extends ParserFactoryBase {

	@Override
	protected ParsingRuleBuilder getStartRule() {
		return Rules.START;
	}

	public enum Rules implements ParsingRuleBuilder {
		START {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						zeroOrMore(Proj), //
						EmptyLines, endOfInput() //
				);
			}
		},

		Proj() {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						EmptyLines, //
						ProjectNameLine, //
						zeroOrMore(TaskLine) //
				).onSuccess(context -> {
					Project project = new Project();
					project.setName(context.get(0));

					int count = context.stackSize() - 1;
					for (int i = 0; i < count; i++) {
						project.addTask(context.get(i + 1));
					}

					context.replaceWith(project);
				});
			}
		},

		ProjectNameLine {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						ProjectName, //
						endOfLineOrInput() //
				);
			}
		}, //
		ProjectName {
			@Override
			public ParsingExpressionBuilder expression() {
				return crlf() //
						.invert() //
						.oneOrMore() //
						.onSuccess(context -> context.replaceWith(context.getCharSequence().toString().trim()));
			}
		}, //
		TaskLine {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						horizontalWhitespace().oneOrMore().ignore(), //
						DoneField, //
						DueDateField, //
						DescriptionField, //
						endOfLineOrInput()).onSuccess(context -> {
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
		}, //
		DoneField {
			@Override
			public ParsingExpressionBuilder expression() {
				return optional( //
						sequence( //
								string("(x)").onSuccess(push(Boolean.TRUE)), //
								horizontalWhitespace().oneOrMore().ignore() //
						) //
				).onSuccess(orElsePush(Boolean.FALSE));
			}
		}, //
		DueDateField {
			@Override
			public ParsingExpressionBuilder expression() {
				return optional( //
						sequence( //
								characterInRange('0', '9').count(4).onSuccess(textToInteger()), //
								character('-').ignore(), //
								characterInRange('0', '9').count(2).onSuccess(textToInteger()), //
								character('-').ignore(), //
								characterInRange('0', '9').count(2).onSuccess(textToInteger()), //
								horizontalWhitespace().oneOrMore().ignore() //
						).onSuccess(context -> {
							int year = context.get(0);
							int month = context.get(1);
							int day = context.get(2);
							context.replaceWith(LocalDate.of(year, month, day));
						}) //
				).onSuccess(orElsePush(null));
			}
		}, //
		DescriptionField {
			@Override
			public ParsingExpressionBuilder expression() {
				return crlf() //
						.invert() //
						.oneOrMore() //
						.onSuccess(context -> context.push(context.getCharSequence().toString().trim()));
			}
		}, //
		EmptyLines {
			@Override
			public ParsingExpressionBuilder expression() {
				return zeroOrMore( //
						sequence( //
								horizontalWhitespace().zeroOrMore().ignore(), //
								crlf().ignore() //
						) //
				);
			}
		};
	}
}