package net.netnook.repeg.expressions.chars;

/**
 * {@link CharMatcher} which matches ASCII carriage return (U+000D, '\r') or line feed (U+000A, '\n') characters.
 */
final class CRLFMatcher extends CharMatcher {

	private static final int LF = '\n';
	private static final int CR = '\r';

	static final CRLFMatcher INSTANCE = new CRLFMatcher();

	private CRLFMatcher() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test == LF || test == CR;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		buf.append("\\n\\r");
	}
}
