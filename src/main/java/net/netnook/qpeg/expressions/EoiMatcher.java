package net.netnook.qpeg.expressions;

public class EoiMatcher extends SimpleExpression {

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
		public ParsingExpression build(BuildContext ctxt) {
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
	public boolean parse(Context context) {
		if (context.peekChar() != Context.END_OF_INPUT) {
			return false;
		}
		return true;
	}
}
