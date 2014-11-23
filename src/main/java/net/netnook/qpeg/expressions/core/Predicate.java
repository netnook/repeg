package net.netnook.qpeg.expressions.core;

import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.expressions.BuildContext;
import net.netnook.qpeg.expressions.CompoundExpression;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;

public final class Predicate extends CompoundExpression {

	public static Builder match(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, false);
	}

	public static Builder not(ParsingExpressionBuilder expression) {
		return new Builder().expression(expression, true);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private boolean invert;

		public Builder expression(ParsingExpressionBuilder expression, boolean invert) {
			this.expression = expression;
			this.invert = invert;
			return this;
		}

		@Override
		public Builder ignore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected Predicate doBuild(BuildContext ctxt) {
			return new Predicate(this, expression.build(ctxt));
		}
	}

	protected final ParsingExpression expression;
	protected final boolean invert;

	protected Predicate(Builder builder, ParsingExpression expression) {
		super(builder);
		this.expression = expression;
		this.invert = builder.invert;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		boolean match = expression.parse(context);

		boolean success = invert ^ match;

		context.resetTo(startPosition, startStackIdx);

		return success;
	}

	@Override
	public String buildGrammar() {
		return invert  //
				? "!(" + expression.buildGrammar() + ")" //
				: "&(" + expression.buildGrammar() + ")";
	}
}
