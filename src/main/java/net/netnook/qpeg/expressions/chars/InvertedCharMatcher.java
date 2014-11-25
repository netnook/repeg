package net.netnook.qpeg.expressions.chars;

final class InvertedCharMatcher extends CharMatcher {

	private final CharMatcher wrapped;

	InvertedCharMatcher(CharMatcher wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public boolean isMatch(int c) {
		return !wrapped.isMatch(c);
	}

	public String buildGrammar() {
		StringBuilder buf = new StringBuilder();
		buf.append("[^");
		wrapped.appendCharacterClass(buf);
		buf.append(']');
		return buf.toString();
	}

	@Override
	protected void appendCharacterClass(StringBuilder buf) {
		throw new UnsupportedOperationException("Should never be called");
	}
}
