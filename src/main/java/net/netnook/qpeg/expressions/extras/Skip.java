package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.chars.CharTester;

public final class Skip extends SimpleExpression {

	public static Skip characters(CharTester charTester) {
		return new Skip(charTester);
	}

	private final CharTester matcher;

	private Skip(CharTester matcher) {
		super(OnSuccessHandler.NO_OP);
		this.matcher = matcher;
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int pos = startPosition;
		while (true) {
			int found = context.charAt(pos);

			boolean match = (found != RootContext.END_OF_INPUT) && matcher.isMatch(found);
			if (!match) {
				break;
			}

			pos++;
		}

		context.setPosition(pos);

		return true;
	}

	@Override
	public String buildGrammar() {
		throw new UnsupportedOperationException();
	}
}
