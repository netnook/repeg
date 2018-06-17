package net.netnook.repeg.chars;

/**
 * {@link CharMatcher} which matches a character if present in a specified string of characters.
 */
final class CharInMatcher implements CharMatcher {

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
	public String buildGrammar() {
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		for (int i = 0; i < characters.length(); i++) {
			buf.append(CharMatcher.escapeGrammarChar(characters.charAt(i)));
		}
		buf.append("]");
		return buf.toString();
	}
}
