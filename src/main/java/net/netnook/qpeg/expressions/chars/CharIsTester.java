package net.netnook.qpeg.expressions.chars;

class CharIsTester extends CharTester {
	private final char c;

	CharIsTester(char c) {
		this.c = c;
	}

	@Override
	public boolean isMatch(int test) {
		return test == c;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		appendGrammarChar(buf, 0, c);
	}
}
