package net.netnook.repeg.chars;

final class InvertedCharMatcher implements CharMatcher {

	private final CharMatcher wrapped;

	InvertedCharMatcher(CharMatcher wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean isMatch(int c) {
		return !wrapped.isMatch(c);
	}

	public String buildGrammar() {
		String wrappedGrammar = wrapped.buildGrammar();

		StringBuilder buf = new StringBuilder(wrappedGrammar.length() - 2 + 3);
		buf.append("[^");
		buf.append(wrappedGrammar, 1, wrappedGrammar.length() - 1);
		buf.append(']');
		return buf.toString();
	}
}
