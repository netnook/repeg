package net.netnook.repeg.expressions.chars;

/**
 * Base class for character matchers.
 * <p>
 * A character matcher determines a match of a single character to specified rules.  Specific rules
 * are implemented in concrete child classes of this {@code CharMatcher}.  In addition, this class
 * provides static methods to create many of the commonly used character matchers.
 */
public abstract class CharMatcher {

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

	protected CharMatcher() {
		// no-op
	}

	/**
	 * Invert the {@link CharMatcher}.
	 *
	 * @return an inverted {@link CharMatcher}.
	 */
	public CharMatcher not() {
		return new InvertedCharMatcher(this);
	}

	/**
	 * Match method to be implemented by concrete child classes of {@link CharMatcher}.
	 *
	 * @param c the character to test
	 * @return {@code true} if the {@code c} matches the rule, {@code false} otherwise.
	 */
	public abstract boolean isMatch(int c);

	/**
	 * Build a string grammar representing this matcher.
	 *
	 * @return grammar
	 */
	public String buildGrammar() {
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		appendCharacterClass(buf);
		buf.append(']');
		return buf.toString();
	}

	protected abstract void appendCharacterClass(StringBuilder buf);

	protected void appendGrammarChar(StringBuilder buf, int pos, char c) {
		if (pos == 0 && c == '^') {
			buf.append("\\^");
		} else if (c == '\\') {
			buf.append("\\\\");
		} else if (c == '\t') {
			buf.append("\\t");
		} else if (c == '\n') {
			buf.append("\\n");
		} else if (c == '\r') {
			buf.append("\\r");
		} else if (c == '\f') {
			buf.append("\\f");
		} else if (c == '-') {
			buf.append("\\-");
		} else {
			buf.append(c);
		}
	}
}
