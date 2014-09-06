package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.PredicateNode;
import net.netnook.qpeg.parsetree.ParseNode;

public abstract class Predicate implements SimpleExpression {

	public static TruePredicate match(ParsingExpression expression) {
		return new TruePredicate(expression);
	}

	private static class TruePredicate extends Predicate {
		private final ParsingExpression expression;

		private TruePredicate(ParsingExpression expression) {
			this.expression = expression;
		}

		@Override
		public String buildGrammar() {
			return "&(" + expression.buildGrammar() + ")";
		}

		@Override
		public ParseNode parse(Context context) {
			int startPosition = context.position();

			ParseNode child = expression.parse(context);

			int endPosition = context.position();

			context.setPosition(startPosition);

			if (child == null) {
				return null;
			}

			return new PredicateNode(context, this, startPosition, endPosition, child);
		}
	}

	public static FalsePredicate not(ParsingExpression expression) {
		return new FalsePredicate(expression);
	}

	private static class FalsePredicate extends Predicate {
		private final ParsingExpression expression;

		private FalsePredicate(ParsingExpression expression) {
			this.expression = expression;
		}

		@Override
		public String buildGrammar() {
			return "!(" + expression.buildGrammar() + ")";
		}

		@Override
		public ParseNode parse(Context context) {
			int startPosition = context.position();

			ParseNode child = expression.parse(context);

			context.setPosition(startPosition);

			if (child != null) {
				return null;
			}

			return new PredicateNode(context, this, startPosition, startPosition, child);
		}
	}

}
