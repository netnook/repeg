package net.netnook.qpeg.expressions;

public class NewlineMatcher extends SimpleExpression {

	private static final int LF = '\n';
	private static final int CR = '\r';

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private Builder() {
			// no-op
		}

		@Override
		public NewlineMatcher doBuild(BuildContext ctxt) {
			if (getOnSuccess() == null) {
				onSuccess(OnSuccessHandler.PUSH_TEXT_TO_STACK);
			}
			return new NewlineMatcher(this);
		}
	}

	private NewlineMatcher(Builder builder) {
		super(builder);
	}

	@Override
	public String buildGrammar() {
		return "('\\n'|'\\r\\n'|'\\r')";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int c1 = context.consumeChar();

		// TODO: compare this to unicode line termination
		if (c1 == LF) {
			return true;
		} else if (c1 == CR) {
			if (context.consumeChar() != LF) {
				context.rewindInput();
			}
			return true;
		}

		return false;
	}
}
