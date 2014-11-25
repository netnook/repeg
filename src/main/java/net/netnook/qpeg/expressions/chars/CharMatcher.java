package net.netnook.qpeg.expressions.chars;

public abstract class CharMatcher {

	public static CharMatcher any() {
		return CharAnyMatcher.INSTANCE;
	}

	public static CharMatcher whitespace() {
		return CharIsWhitespaceMatcher.INSTANCE;
	}

	public static CharMatcher horizontalWhitespace() {
		return HorizontalWhitespaceMatcher.INSTANCE;
	}

	public static CharMatcher is(char c) {
		return new CharIsMatcher(c);
	}

	public static CharMatcher in(String characters) {
		return new CharInMatcher(characters);
	}

	public static CharMatcher inRange(char from, char to) {
		return new CharInRangeMatcher(from, to);
	}

	protected CharMatcher() {
		// no-op
	}

	public CharMatcher not() {
		return new InvertedCharMatcher(this);
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
