package net.netnook.qpeg.expressions.core;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.Visitor;

public final class EndOfInput extends SimpleExpression {

	private static final Builder EOI_BUILDER = new Builder();

	public static Builder instance() {
		return EOI_BUILDER;
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private final EndOfInput instance;

		Builder() {
			instance = new EndOfInput(this);
		}

		@Override
		protected ParsingExpression doBuild() {
			return instance;
		}

		@Override
		public ParsingExpressionBuilderBase onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}
	}

	private EndOfInput(ParsingExpressionBuilderBase builder) {
		super(builder.getOnSuccess());
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String buildGrammar() {
		return "EOI";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		return context.charAt(startPosition) == RootContext.END_OF_INPUT;
	}
}
