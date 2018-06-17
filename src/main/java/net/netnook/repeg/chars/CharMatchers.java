package net.netnook.repeg.chars;

/**
 * Utility class to produce {@link CharMatcher CharMatchers}.
 */
public final class CharMatchers {

	private CharMatchers() {
		// defeat instantiation.
	}

	/**
	 * Create a {@link CharMatcher} which matches any character.
	 *
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher any() {
		return CharAnyMatcher.INSTANCE;
	}

	/**
	 * Create a {@link CharMatcher} which matches any ASCII whitespace.  See {@link AsciiWhitespaceMatcher}
	 *
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher asciiWhitespace() {
		return AsciiWhitespaceMatcher.INSTANCE;
	}

	/**
	 * Create a {@link CharMatcher} which matches any horizontal whitespace.  See {@link HorizontalWhitespaceMatcher}
	 *
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher horizontalWhitespace() {
		return HorizontalWhitespaceMatcher.INSTANCE;
	}

	/**
	 * Create a {@link CharMatcher} which matches '\n' or '\r' characters.
	 * <p>
	 * See {@link CRLFMatcher}
	 *
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher crlf() {
		return CRLFMatcher.INSTANCE;
	}

	/**
	 * Create a {@link CharMatcher} which matches the specific character {@code c}.
	 *
	 * @param c the character to match.
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher is(char c) {
		return new CharIsMatcher(c);
	}

	/**
	 * Create a {@link CharMatcher} which matches any of the characters in the supplied string of {@code characters}.
	 *
	 * @param characters the characters to match.
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher in(String characters) {
		return new CharInMatcher(characters);
	}

	/**
	 * Create a {@link CharMatcher} which matches any character in the range {@code from} to {@code to} (both inclusive).
	 * <p>
	 * Example {@code CharMatcher.inRange('a', 'z')} to match any lower case ascii letter.
	 *
	 * @param from the lowest character to match (inclusive).
	 * @param to   the highert character to match (inclusive).
	 * @return the resulting {@link CharMatcher}.
	 */
	public static CharMatcher inRange(char from, char to) {
		return new CharInRangeMatcher(from, to);
	}
}
