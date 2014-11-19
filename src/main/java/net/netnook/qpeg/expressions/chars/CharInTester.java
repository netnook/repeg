package net.netnook.qpeg.expressions.chars;

class CharInTester extends CharTester {

	private final String characters;

	// TODO: convert this to indexed array for the common case a few characters in narrow range (e.g. char < 1024 ?)
	CharInTester(String characters) {
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
