package net.netnook.qpeg.expressions.chars;

class CharAnyTester extends CharTester {

	static final CharAnyTester INSTANCE = new CharAnyTester();

	private CharAnyTester() {
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
