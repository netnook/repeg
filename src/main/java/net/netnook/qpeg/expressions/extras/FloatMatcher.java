package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;

public final class FloatMatcher extends SimpleExpression {

	private static final FloatMatcher INSTANCE = new FloatMatcher();

	public static FloatMatcher instance() {
		return INSTANCE;
	}

	private FloatMatcher() {
		super(OnSuccessHandler.PUSH_TEXT_AS_FLOAT);
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
