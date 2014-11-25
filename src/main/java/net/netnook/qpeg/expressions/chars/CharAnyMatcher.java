package net.netnook.qpeg.expressions.chars;

final class CharAnyMatcher extends CharMatcher {

	static final CharAnyMatcher INSTANCE = new CharAnyMatcher();

	private CharAnyMatcher() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return true;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		buf.append('.');
	}
}
