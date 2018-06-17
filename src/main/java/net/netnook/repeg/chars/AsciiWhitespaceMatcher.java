package net.netnook.repeg.chars;

/**
 * {@link CharMatcher} which matches any ASCII whitespace character.
 * <p>
 * An ASCII whitespace character is one of:
 * <ul>
 *     <li>TAB (U+0009, '\t')</li>
 *     <li>SPACE (U+0020, ' ')</li>
 *     <li>LINE FEED (U+000A, '\n')</li>
 *     <li>LINE TABULATION (U+000B)</li>
 *     <li>FORM FEED (U+000C, '\f')</li>
 *     <li>CARRIAGE RETURN (U+000D, '\r')</li>
 * </ul>
 * <p>
 * This matcher corresponds to the {@code '\s'} shorthand character class.
 */
final class AsciiWhitespaceMatcher implements CharMatcher {

	private static final int TAB = '\t';
	private static final int SPACE = ' ';
	private static final int LF = '\n';
	private static final int LINE_TABULATION = 0x0b;
	private static final int FORM_FEED = '\f';
	private static final int CR = '\r';

	static final AsciiWhitespaceMatcher INSTANCE = new AsciiWhitespaceMatcher();

	private AsciiWhitespaceMatcher() {
		// defeat instantiation
	}

	@Override
	public boolean isMatch(int test) {
		return test == TAB //
				|| test == SPACE //
				|| test == LF //
				|| test == LINE_TABULATION //
				|| test == FORM_FEED //
				|| test == CR;
	}

	@Override
	public String buildGrammar() {
		return "[\\s]";
	}
}
