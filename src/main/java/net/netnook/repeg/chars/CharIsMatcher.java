package net.netnook.repeg.chars;

/**
 * {@link CharMatcher} which matches a single character.
 */
final class CharIsMatcher implements CharMatcher {
	private final char c;

	CharIsMatcher(char c) {
		this.c = c;
	}

	@Override
	public boolean isMatch(int test) {
		return test == c;
	}

	@Override
	public String buildGrammar() {
		return "[" + CharMatcher.escapeGrammarChar(c) + "]";
	}
}
