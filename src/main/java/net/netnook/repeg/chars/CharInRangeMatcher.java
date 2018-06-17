package net.netnook.repeg.chars;

/**
 * {@link CharMatcher} which matches a character if between a lower ({@code from}) and upper ({@code to}) bounds.  Inclusive match.
 */
final class CharInRangeMatcher implements CharMatcher {
	private final char from;
	private final char to;

	CharInRangeMatcher(char from, char to) {
		if (to < from) {
			throw new IllegalArgumentException("Illegal attempt to define a character-in-range test with to < from");
		}
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean isMatch(int test) {
		return test >= from && test <= to;
	}

	@Override
	public String buildGrammar() {
		return "[" + CharMatcher.escapeGrammarChar(from) + "-" + CharMatcher.escapeGrammarChar(to) + "]";
	}
}

