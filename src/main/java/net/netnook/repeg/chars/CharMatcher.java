package net.netnook.repeg.chars;

/**
 * Interface for character matchers.
 * <p>
 * A character matcher determines a match of a single character to specified rules.  Specific rules
 * are implemented in concrete child classes of this {@code CharMatcher}.  In addition, this class
 * provides static methods to create many of the commonly used character matchers.
 */
public interface CharMatcher {

	/**
	 * Invert the {@link CharMatcher}.
	 *
	 * @return an inverted {@link CharMatcher}.
	 */
	default CharMatcher not() {
		return new InvertedCharMatcher(this);
	}

	/**
	 * Match method to be implemented by concrete child classes of {@link CharMatcher}.
	 *
	 * @param c the character to test
	 * @return {@code true} if the {@code c} matches the rule, {@code false} otherwise.
	 */
	boolean isMatch(int c);

	/**
	 * Build a string grammar representing this matcher.
	 *
	 * @return grammar
	 */
	String buildGrammar();

	static String escapeGrammarChar(char c) {
		if (c == '^') {
			return "\\^";
		} else if (c == '\\') {
			return "\\\\";
		} else if (c == '\t') {
			return "\\t";
		} else if (c == '\n') {
			return "\\n";
		} else if (c == '\r') {
			return "\\r";
		} else if (c == '\f') {
			return "\\f";
		} else if (c == '-') {
			return "\\-";
		} else {
			return "" + c;
		}
	}
}
