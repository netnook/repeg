package net.netnook.qpeg.expressions.chars;

public abstract class CharTester {

	protected CharTester() {
		// no-op
	}

	public CharTester invert() {
		return new InvertedCharTester(this);
	}

	public abstract boolean isMatch(int c);

	public String buildGrammar() {
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		appendCharacterClass(buf);
		buf.append(']');
		return buf.toString();
	}

	protected abstract void appendCharacterClass(StringBuilder buf);

	protected void appendGrammarChar(StringBuilder buf, int pos, char c) {
		if (pos == 0 && c == '^') {
			buf.append("\\^");
		} else if (c == '\\') {
			buf.append("\\\\");
		} else if (c == '\t') {
			buf.append("\\t");
		} else if (c == '\n') {
			buf.append("\\n");
		} else if (c == '\r') {
			buf.append("\\r");
		} else if (c == '\f') {
			buf.append("\\f");
		} else if (c == '-') {
			buf.append("\\-");
		} else {
			buf.append(c);
		}
	}
}
