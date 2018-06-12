package net.netnook.repeg.expressions.chars;

/**
 * {@link CharMatcher} which matches a character if present in a specified string of characters.
 */
final class CharInMatcher extends CharMatcher {

	private final String characters;

	/**
	 * @param characters set characters to match.
	 */
	// TODO: convert this to indexed array for the common case a few characters in narrow range (e.g. char < 1024 ?)
	CharInMatcher(String characters) {
		this.characters = characters;
	}

	@Override
	public boolean isMatch(int test) {
		return characters.indexOf(test) >= 0;
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		for (int i = 0; i < characters.length(); i++) {
			appendGrammarChar(buf, i, characters.charAt(i));
		}
	}
}
