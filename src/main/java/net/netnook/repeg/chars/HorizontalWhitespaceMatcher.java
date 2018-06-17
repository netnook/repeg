package net.netnook.repeg.chars;

/**
 * {@link CharMatcher} which matches any horizontal whitespace character.
 * <p>
 * A horizontal whitespace character is one of:
 * <ul>
 *     <li>TAB (U+0009, '\t')</li>
 *     <li>SPACE (U+0020, ' ')</li>
 *     <li>NO BREAK SPACE (U+00A0)</li>
 *     <li>OGHAM SPACE MARK (U+1680)</li>
 *     <li>MONGOLIAN VOWEL SEPARATOR (U+180E)</li>
 *     <li>EN QUAD (U+2000) to ZERO WIDTH SPACE (U+200b) - inclusive</li>
 *     <li>NARROW NO-BREAK SPACE (U+202F)</li>
 *     <li>MEDIUM MATHEMATICAL SPACE (U+205F)</li>
 *     <li>IDEOGRAPHIC SPACE (U+3000)</li>
 *     <li>ZERO WIDTH NO-BREAK SPACE (U+FEFF)</li>
 * </ul>
 * <p>
 * This matcher corresponds to the {@code '\h'} shorthand character class.
 */
final class HorizontalWhitespaceMatcher implements CharMatcher {

	private static final int TAB = '\t';
	private static final int SPACE = ' ';
	private static final int NO_BREAK_SPACE = 0xa0;
	private static final int OGHAM_SPACE_MARK = 0x1680;
	private static final int MONGOLIAN_VOWEL_SEPARATOR = 0x180e;
	private static final int EN_QUAD = 0x2000;
	private static final int ZERO_WIDTH_SPACE = 0x200b;
	private static final int NARROW_NO_BREAK_SPACE = 0x202f;
	private static final int MEDIUM_MATHEMATICAL_SPACE = 0x205f;
	private static final int IDEOGRAPHIC_SPACE = 0x3000;
	private static final int ZERO_WIDTH_NO_BREAK_SPACE = 0xfeff;

	static final HorizontalWhitespaceMatcher INSTANCE = new HorizontalWhitespaceMatcher();

	private HorizontalWhitespaceMatcher() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test == TAB //
				|| test == SPACE //
				|| test == NO_BREAK_SPACE //
				|| test == OGHAM_SPACE_MARK //
				|| test == MONGOLIAN_VOWEL_SEPARATOR //
				|| (test >= EN_QUAD && test <= ZERO_WIDTH_SPACE) //
				|| test == NARROW_NO_BREAK_SPACE //
				|| test == MEDIUM_MATHEMATICAL_SPACE //
				|| test == IDEOGRAPHIC_SPACE //
				|| test == ZERO_WIDTH_NO_BREAK_SPACE;
	}

	@Override
	public String buildGrammar() {
		return "[\\h]";
	}
}
