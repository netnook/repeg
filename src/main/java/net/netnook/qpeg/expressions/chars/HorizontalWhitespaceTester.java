package net.netnook.qpeg.expressions.chars;

class HorizontalWhitespaceTester extends CharTester {

	private static final int SPACE = ' ';
	private static final int TAB = '\t';
	private static final int NO_BREAK_SPACE = 0xa0;
	private static final int OGHAM_SPACE_MARK = 0x1680;
	private static final int MONGOLIAN_VOWEL_SEPARATOR = 0x180e;
	private static final int EN_QUAD = 0x2000;
	private static final int HAIR_SPACE = 0x200a;
	private static final int NARROW_NO_BREAK_SPACE = 0x202f;
	private static final int MEDIUM_MATHEMATICAL_SPACE = 0x205f;
	private static final int IDEOGRAPHIC_SPACE = 0x3000;

	static final HorizontalWhitespaceTester INSTANCE = new HorizontalWhitespaceTester();

	private HorizontalWhitespaceTester() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test == TAB //
				|| test == SPACE //
				|| test == NO_BREAK_SPACE //
				|| test == OGHAM_SPACE_MARK //
				|| test == MONGOLIAN_VOWEL_SEPARATOR //
				|| (test >= EN_QUAD && test <= HAIR_SPACE) //
				|| test == NARROW_NO_BREAK_SPACE //
				|| test == MEDIUM_MATHEMATICAL_SPACE //
				|| test == IDEOGRAPHIC_SPACE;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		buf.append("\\h");
	}
}
