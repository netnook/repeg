package net.netnook.qpeg.expressions.chars;

class CharIsWhitespaceTester extends CharTester {

	static final CharIsWhitespaceTester INSTANCE = new CharIsWhitespaceTester();

	private CharIsWhitespaceTester() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return Character.isWhitespace(test);
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		// FIXME character class \s does not match Character.isWhitespace !!!
		buf.append("\\s");
	}
}
