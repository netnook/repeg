package net.netnook.repeg.examples.calculator;

import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.RuleEnum;

public class ParserFactory extends ParserFactoryBase<Integer> {

	@Override
	protected RuleEnum getStartRule() {
		return Rules.START;
	}

	public enum Rules implements RuleEnum {
		START {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						Expression, //
						endOfInput() //
				);
			}
		},

		Expression {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						Term, //
						zeroOrMore( //
								sequence( //
										one(characterIn("+-")).onSuccess(pushTextAsString()), //
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

		Term {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						Factor, //
						zeroOrMore( //
								sequence( //
										one(characterIn("*/")).onSuccess(pushTextAsString()), //
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
		},

		Factor {
			@Override
			public ExpressionBuilder expression() {
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
			public ExpressionBuilder expression() {
				return sequence( //
						one('('), //
						Expression, //
						one(')') //
				);
			}
		},

		Number {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore(characterInRange('0', '9')).onSuccess(pushTextAsInteger());
			}
		}
	}
}
