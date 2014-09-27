package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;

public abstract class Predicate extends SimpleExpression {

	protected Predicate() {
		super(true, null, null);
	}

	public static TruePredicateBuilder match(ParsingExpressionBuilder expression) {
		return new TruePredicateBuilder().expression(expression);
	}

	public static class TruePredicateBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public TruePredicateBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public TruePredicateBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public TruePredicate build(BuildContext ctxt) {
			return new TruePredicate(expression.build(ctxt));
		}
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
		public boolean parse(Context context) {
			int startPosition = context.position();
			int stackPosition = context.stackPosition();

			boolean match = expression.parse(context);

//			int endPosition = context.position();

			context.setPosition(startPosition);
			context.resetStack(stackPosition);

			return match;
//			if (match == null) {
//				return null;
//			}
//
//			return new PredicateNode(context, this, startPosition, endPosition, child);
		}
	}

	public static FalsePredicateBuilder not(ParsingExpressionBuilder expression) {
		return new FalsePredicateBuilder().expression(expression);
	}

	public static class FalsePredicateBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;

		public FalsePredicateBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		@Override
		public FalsePredicateBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public FalsePredicate build(BuildContext ctxt) {
			return new FalsePredicate(expression.build(ctxt));
		}
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
		public boolean parse(Context context) {
			int startPosition = context.position();
			int stackPosition = context.stackPosition();

			boolean match = expression.parse(context);

			//			int endPosition = context.position();

			context.setPosition(startPosition);
			context.resetStack(stackPosition);

			return !match;



//			int startPosition = context.position();
//
//			ParseNode child = expression.parse(context);
//
//			context.setPosition(startPosition);
//
//			if (child != null) {
//				return null;
//			}
//
//			return new PredicateNode(context, this, startPosition, startPosition, child);
		}
	}

}
