package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;

public final class FloatExpression extends SimpleExpression {

	private static final FloatExpression INSTANCE = new FloatExpression();

	public static FloatExpression instance() {
		return INSTANCE;
	}

	private FloatExpression() {
		super(OnSuccessHandler.PUSH_TEXT_AS_FLOAT);
	}

	@Override
	public String buildGrammar() {
		return "[:float:]";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int pos = startPosition;
		int c = context.charAt(pos);

		if (c != '-' && c != '.' && (c < '0' || c > '9')) {
			return false;
		}
		pos++;
		boolean isNegative = (c == '-');
		boolean foundDot = (c == '.');

		while (true) {
			c = context.charAt(pos);

			if (c == '.') {
				if (foundDot) {
					break;
				}
				foundDot = true;

			} else if (c < '0' || c > '9') {
				break;
			}
			pos++;
		}

		context.setPosition(pos);

		int len = pos - startPosition;

		if (isNegative && foundDot) {
			return len > 2;
		} else if (isNegative || foundDot) {
			return len > 1;
		} else {
			return len > 0;
		}
	}
}
