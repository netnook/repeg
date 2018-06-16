package net.netnook.repeg.examples.template;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.examples.template.model.Template;
import net.netnook.repeg.examples.template.model.TemplateExpression;
import net.netnook.repeg.examples.template.model.TemplateIf;
import net.netnook.repeg.examples.template.model.TemplateLoop;
import net.netnook.repeg.examples.template.model.TemplateNode;
import net.netnook.repeg.examples.template.model.TemplateText;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions.ParsingRuleBuilder;

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
						Nodes, //
						endOfInput() //
				).onSuccess(context -> {
					List<TemplateNode> nodes = context.get(0);
					Template template = new Template(nodes);
					template.preTrim();
					context.replaceWith(template);
				});
			}
		},

		Nodes {
			@Override
			public ParsingExpressionBuilder expression() {
				return zeroOrMore(Node).onSuccess(context -> {
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

		Node {
			@Override
			public ParsingExpressionBuilder expression() {
				return choice( //
						Text, //
						ControlFlowNode, //
						ExpressionNode //
				);
			}
		},

		ControlFlowNode {
			@Override
			public ParsingExpressionBuilder expression() {
				return choice( //
						IfControlFlow, //
						LoopControlFlow //
				);
			}
		},

		IfControlFlow {
			// {% if x %} ... {% endif %}
			@Override
			public ParsingExpressionBuilder expression() {
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
						Nodes, //
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
					List<TemplateNode> children = context.get(3);
					boolean trimAfterChildren = context.get(4);
					boolean trimAfter = context.get(5);

					TemplateIf node = new TemplateIf(ref, children);
					node.getTrims().setBefore(trimBefore);
					node.getTrims().setAfter(trimAfter);
					node.getTrims().setBeforeChildren(trimBeforeChildren);
					node.getTrims().setAfterChildren(trimAfterChildren);

					context.replaceWith(node);
				});
			}
		},

		LoopControlFlow {
			// {% for x in y %} ... {% endfor %}
			@Override
			public ParsingExpressionBuilder expression() {
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
						Nodes, //
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
					List<TemplateNode> children = context.get(4);
					boolean trimAfterChildren = context.get(5);
					boolean trimAfter = context.get(6);

					TemplateLoop node = new TemplateLoop(var, ref, children);
					node.getTrims().setBefore(trimBefore);
					node.getTrims().setAfter(trimAfter);
					node.getTrims().setBeforeChildren(trimBeforeChildren);
					node.getTrims().setAfterChildren(trimAfterChildren);

					context.replaceWith(node);
				});
			}
		},

		ExpressionNode {
			@Override
			public ParsingExpressionBuilder expression() {
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

					TemplateExpression node = new TemplateExpression(ref);
					node.getTrims().setBefore(trimBefore);
					node.getTrims().setAfter(trimAfter);

					context.replaceWith(node);
				});
			}
		},

		Ref {
			@Override
			public ParsingExpressionBuilder expression() {
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
			public ParsingExpressionBuilder expression() {
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
			public ParsingExpressionBuilder expression() {
				return oneOrMore( //
						sequence( //
								not(string("{{")), //
								not(string("{%")), //
								one(anyChar()) //
						) //
				).onSuccess(context -> {
					context.push(new TemplateText(context.getCharSequence().toString()));
				});
			}
		},

		TrimControl {
			@Override
			public ParsingExpressionBuilder expression() {
				return optional('-').onSuccess(context -> {
					context.push(context.inputLength() > 0);
				});
			}
		},

		SkipWhitespace {
			@Override
			public ParsingExpressionBuilder expression() {
				return zeroOrMore(horizontalWhitespace());
			}
		},

		SkipMin1Whitespace {
			@Override
			public ParsingExpressionBuilder expression() {
				return oneOrMore(horizontalWhitespace());
			}
		}
	}
}
