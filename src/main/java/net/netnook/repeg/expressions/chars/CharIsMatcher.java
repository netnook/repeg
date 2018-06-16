package net.netnook.repeg.expressions.chars;

/**
 * {@link CharMatcher} which matches a single character.
 */
final class CharIsMatcher extends CharMatcher {
	private final char c;

	CharIsMatcher(char c) {
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