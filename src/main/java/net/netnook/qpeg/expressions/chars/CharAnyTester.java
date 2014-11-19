package net.netnook.qpeg.expressions.chars;

import net.netnook.qpeg.expressions.Context;

class CharAnyTester extends CharTester {

	static final CharAnyTester INSTANCE = new CharAnyTester();

	private CharAnyTester() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test != Context.END_OF_INPUT;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		buf.append('.');
	}
}
