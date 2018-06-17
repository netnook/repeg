package net.netnook.repeg.examples.template;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.RuleEnum;
import net.netnook.repeg.examples.template.model.Expression;
import net.netnook.repeg.examples.template.model.If;
import net.netnook.repeg.examples.template.model.Loop;
import net.netnook.repeg.examples.template.model.Node;
import net.netnook.repeg.examples.template.model.Template;
import net.netnook.repeg.examples.template.model.Text;

public class ParserFactory extends ParserFactoryBase<Template> {

	@Override
	protected RuleEnum getStartRule() {
		return Rules.START;
	}

	public enum Rules implements RuleEnum {
		START {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						NodesExpr, //
						endOfInput() //
				).onSuccess(context -> {
					List<Node> nodes = context.get(0);
					Template template = new Template(nodes);
					template.preTrim();
					context.replaceWith(template);
				});
			}
		},

		NodesExpr {
			@Override
			public ExpressionBuilder expression() {
				return zeroOrMore(NodeExpr).onSuccess(context -> {
					int count = context.stackSize();
					if (count == 0) {
						context.replaceWith(Collections.emptyList());
					} else if (count == 1) {
						context.replaceWith(Collections.singletonList(context.get(0)));
					} else {
						context.replaceWith(context.getAll());
					}
				});
			}
		},

		NodeExpr {
			@Override
			public ExpressionBuilder expression() {
				return choice( //
						Text, //
						ControlFlowExpr, //
						ExpressionExpr //
				);
			}
		},

		ControlFlowExpr {
			@Override
			public ExpressionBuilder expression() {
				return choice( //
						IfControlFlowExpr, //
						LoopControlFlowExpr //
				);
			}
		},

		IfControlFlowExpr {
			// {% if x %} ... {% endif %}
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						string("{%"), //
						TrimControl, //
						SkipMin1Whitespace, //
						string("if"), //
						SkipMin1Whitespace, //
						Ref, //
						SkipMin1Whitespace, //
						TrimControl, //
						string("%}"), //
						NodesExpr, //
						string("{%"), //
						TrimControl, //
						SkipMin1Whitespace, //
						string("endif"), //
						SkipMin1Whitespace, //
						TrimControl, //
						string("%}") //
				).onSuccess(context -> {
					boolean trimBefore = context.get(0);
					String ref = context.get(1);
					boolean trimBeforeChildren = context.get(2);
					List<Node> children = context.get(3);
					boolean trimAfterChildren = context.get(4);
					boolean trimAfter = context.get(5);

					If node = new If(ref, children);
					node.getTrimSettings().setBefore(trimBefore);
					node.getTrimSettings().setAfter(trimAfter);
					node.getTrimSettings().setBeforeChildren(trimBeforeChildren);
					node.getTrimSettings().setAfterChildren(trimAfterChildren);

					context.replaceWith(node);
				});
			}
		},

		LoopControlFlowExpr {
			// {% for x in y %} ... {% endfor %}
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						string("{%"), //
						TrimControl, //
						SkipMin1Whitespace, //
						string("for"), //
						SkipMin1Whitespace, //
						Var, SkipMin1Whitespace, //
						string("in"), //
						SkipMin1Whitespace, //
						Ref, //
						SkipMin1Whitespace, //
						TrimControl, //
						string("%}"), //
						NodesExpr, //
						string("{%"), //
						TrimControl, //
						SkipWhitespace, //
						string("endfor"), //
						SkipWhitespace, //
						TrimControl, //
						string("%}") //
				).onSuccess(context -> {
					boolean trimBefore = context.get(0);
					String var = context.get(1);
					String ref = context.get(2);
					boolean trimBeforeChildren = context.get(3);
					List<Node> children = context.get(4);
					boolean trimAfterChildren = context.get(5);
					boolean trimAfter = context.get(6);

					Loop node = new Loop(var, ref, children);
					node.getTrimSettings().setBefore(trimBefore);
					node.getTrimSettings().setAfter(trimAfter);
					node.getTrimSettings().setBeforeChildren(trimBeforeChildren);
					node.getTrimSettings().setAfterChildren(trimAfterChildren);

					context.replaceWith(node);
				});
			}
		},

		ExpressionExpr {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						string("{{"), //
						TrimControl, //
						SkipMin1Whitespace, //
						Ref, //
						SkipMin1Whitespace, //
						TrimControl, //
						string("}}") //
				).onSuccess(context -> {
					boolean trimBefore = context.get(0);
					String ref = context.get(1);
					boolean trimAfter = context.get(2);

					Expression node = new Expression(ref);
					node.getTrimSettings().setBefore(trimBefore);
					node.getTrimSettings().setAfter(trimAfter);

					context.replaceWith(node);
				});
			}
		},

		Ref {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore( //
						choice( //
								one(characterInRange('a', 'z')), //
								one(characterInRange('A', 'Z')), //
								one(characterInRange('0', '9')), //
								one(characterIn("._")) //
						) //
				).onSuccess(context -> {
					context.push(context.getCharSequence().toString());
				});
			}
		},

		Var {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore( //
						choice( //
								one(characterInRange('a', 'z')), //
								one(characterInRange('A', 'Z')), //
								one(characterInRange('0', '9')) //
						) //
				).onSuccess(context -> {
					context.push(context.getCharSequence().toString());
				});
			}
		},

		Text {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore( //
						sequence( //
								not(string("{{")), //
								not(string("{%")), //
								one(anyChar()) //
						) //
				).onSuccess(context -> {
					context.push(new Text(context.getCharSequence().toString()));
				});
			}
		},

		TrimControl {
			@Override
			public ExpressionBuilder expression() {
				return optional('-').onSuccess(context -> {
					// Push "true" if there was a '-'.  Push "false" otherwise.
					context.push(context.inputLength() > 0);
				});
			}
		},

		SkipWhitespace {
			@Override
			public ExpressionBuilder expression() {
				return zeroOrMore(horizontalWhitespace());
			}
		},

		SkipMin1Whitespace {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore(horizontalWhitespace());
			}
		}
	}
}
