package net.netnook.qpeg.expressions.core;

import net.netnook.qpeg.expressions.BuildContext;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.Visitor;

public final class EoiMatcher extends SimpleExpression {

	private static final Builder EOI_BUILDER = new Builder();

	public static Builder instance() {
		return EOI_BUILDER;
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private final EoiMatcher instance;

		Builder() {
			instance = new EoiMatcher(this);
		}

		@Override
		protected ParsingExpression doBuild(BuildContext ctxt) {
			return instance;
		}

		@Override
		public ParsingExpressionBuilderBase ignore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ParsingExpressionBuilderBase onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}
	}

	private EoiMatcher(ParsingExpressionBuilderBase builder) {
		super(builder);
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
		return context.peekChar() == RootContext.END_OF_INPUT;
	}
}
