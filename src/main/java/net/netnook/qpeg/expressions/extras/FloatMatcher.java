package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;

public final class FloatMatcher extends SimpleExpression {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private Builder() {
			// no-op
		}

		@Override
		protected FloatMatcher doBuild() {
			if (getOnSuccess() == null) {
				onSuccess(OnSuccessHandler.PUSH_TEXT_AS_FLOAT);
			}
			return new FloatMatcher(this);
		}
	}

	private FloatMatcher(Builder builder) {
		super(builder);
	}

	@Override
	public String buildGrammar() {
		return "[:float:]";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {

		int c = context.consumeChar();

		if (c != '-' && c != '.' && (c < '0' || c > '9')) {
			return false;
		}
		boolean isNegative = (c == '-');
		boolean foundDot = (c == '.');

		while (true) {
			c = context.consumeChar();

			if (c == '.') {
				if (foundDot) {
					context.rewindInput();
					break;
				}
				foundDot = true;

			} else if (c < '0' || c > '9') {
				context.rewindInput();
				break;
			}
		}

		int len = context.position() - startPosition;

		if (isNegative && foundDot) {
			return len > 2;
		} else if (isNegative || foundDot) {
			return len > 1;
		} else {
			return len > 0;
		}
	}
}
