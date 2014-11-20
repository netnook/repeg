package net.netnook.qpeg.expressions.chars;

class HorizontalWhitespaceTester extends CharTester {

	static final HorizontalWhitespaceTester INSTANCE = new HorizontalWhitespaceTester();

	private HorizontalWhitespaceTester() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test == 0x09 //
				|| test == 0x20 //
				|| test == 0xa0 //
				|| test == 0x1680 //
				|| test == 0x180e //
				|| (test >= 0x2000 && test <= 0x200a) //
				|| test == 0x202f //
				|| test == 0x205f //
				|| test == 0x3000;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		buf.append("\\h");
	}
}
