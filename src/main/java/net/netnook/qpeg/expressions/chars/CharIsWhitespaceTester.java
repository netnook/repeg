package net.netnook.qpeg.expressions.chars;

class CharIsWhitespaceTester extends CharTester {

	private static final int SPACE = ' ';
	private static final int TAB = '\t';
	private static final int LF = '\n';
	private static final int LINE_TABULATION = 0x0b;
	private static final int FORM_FEED = '\f';
	private static final int CR = '\r';

	static final CharIsWhitespaceTester INSTANCE = new CharIsWhitespaceTester();

	private CharIsWhitespaceTester() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test == SPACE //
				|| test == TAB //
				|| test == LF //
				|| test == LINE_TABULATION //
				|| test == FORM_FEED //
				|| test == CR;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		buf.append("\\s");
	}
}
