package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.chars.CharMatcher;

public final class SkipExpression extends SimpleExpression {

	public static SkipExpression characters(CharMatcher matcher) {
		return new SkipExpression(matcher);
	}

	private final CharMatcher matcher;

	private SkipExpression(CharMatcher matcher) {
		super(null);
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
