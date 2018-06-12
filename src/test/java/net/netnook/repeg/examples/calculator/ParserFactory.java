package net.netnook.repeg.examples.calculator;

import net.netnook.repeg.ParserFactoryBase;
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
						Expression, //
						endOfInput() //
				);
			}
		},

		Expression {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						Term, //
						zeroOrMore( //
								sequence( //
										one(characterIn("+-")).onSuccess(pushTextAsString()), //
										Term //
								) //
						) //
				).onSuccess((context) -> {
					int count = context.stackSize();

					if (count % 2 != 1) {
						throw new IllegalStateException("Expected odd number of children but found " + count);
					}

					int result = context.get(0);

					for (int i = 1; i < count; i += 2) {
						String op = context.get(i).toString();
						int term = context.get(i + 1);
						if (op.equals("+")) {
							result = result + term;
						} else if (op.equals("-")) {
							result = result - term;
						} else {
							throw new RuntimeException("No match. Found '" + op + "'");
						}
					}

					context.replaceWith(result);
				});
			}
		},

		Term {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						Factor, //
						zeroOrMore( //
								sequence( //
										one(characterIn("*/")).onSuccess(pushTextAsString()), //
										Factor //
								) //
						) //
				).onSuccess((context) -> {
					int count = context.stackSize();

					if (count % 2 != 1) {
						throw new IllegalStateException("Expected odd number of children but found " + count);
					}

					int result = context.get(0);

					for (int i = 1; i < count; i += 2) {
						String op = context.get(i).toString();
						int term = context.get(i + 1);
						if (op.equals("*")) {
							result = result * term;
						} else if (op.equals("/")) {
							result = result * term;
						} else {
							throw new RuntimeException("No match. Found '" + op + "'");
						}
					}

					context.replaceWith(result);
				});
			}
		},

		Factor {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						zeroOrMore(asciiWhitespace()), //
						choice( //
								Number, //
								Parens //
						), //
						zeroOrMore(asciiWhitespace()) //
				); //
			}
		},

		Parens {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						one('('), //
						Expression, //
						one(')') //
				);
			}
		},

		Number {
			@Override
			public ParsingExpressionBuilder expression() {
				return oneOrMore(characterInRange('0', '9')).onSuccess(pushTextAsInteger());
			}
		}
	}
}
