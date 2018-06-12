package net.netnook.repeg.expressions.chars;

/**
 * {@link CharMatcher} which matches a character if between a lower ({@code from}) and upper ({@code to}) bounds.  Inclusive match.
 */
final class CharInRangeMatcher extends CharMatcher {
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
	protected void appendCharacterClass(StringBuilder buf) {
		appendGrammarChar(buf, 0, from);
		buf.append('-');
		appendGrammarChar(buf, 1, to);
	}
}

