package net.netnook.qpeg.examples.calculator;

import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.ParserFactoryBase;
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
						Expression, //
						endOfInput() //
				);
			}
		},

		//
		Expression() {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						Term, //
						zeroOrMore( //
								sequence( //
										oneOf("+-"), //
										Term //
								) //
						) //
				).onSuccess((context) -> {
					int count = context.size();

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

		Term() {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						Factor, //
						zeroOrMore( //
								sequence( //
										oneOf("*/"), //
										Factor //
								) //
						) //
				).onSuccess((context) -> {
					int count = context.size();

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
		}, //
		Factor {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence(ignoredWhitespace(), //
						choice( //
								Number, //
								Parens //
						), //
						ignoredWhitespace() //
				); //
			}
		}, //
		Parens {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						character('(').ignore(), //
						Expression, //
						character(')').ignore() //
				);
			}
		}, //
		Number {
			@Override
			public ParsingExpressionBuilder expression() {
				return oneOrMore(Digit).onSuccess(TEXT_TO_INTEGER);
			}
		}, //
		Digit {
			@Override
			public ParsingExpressionBuilder expression() {
				return characterInRange('0', '9').ignore();
			}
		}
	}
}
