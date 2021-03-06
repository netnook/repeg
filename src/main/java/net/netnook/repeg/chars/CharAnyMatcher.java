package net.netnook.repeg.chars;

/**
 * {@link CharMatcher} which matches any character.
 */
final class CharAnyMatcher implements CharMatcher {

	static final CharAnyMatcher INSTANCE = new CharAnyMatcher();

	private CharAnyMatcher() {
		// defeat instantiation
	}

	/**
	 * Test {@code c}
	 *
	 * @param c character to match
	 * @return always {@code true}
	 */
	@Override
	public boolean isMatch(int c) {
		return true;
	}

	@Override
	public String buildGrammar() {
		return "[.]";
	}
}
