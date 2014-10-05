package net.netnook.qpeg.expressions;

public abstract class CharTester {

	public static CharTester of(char c) {
		return new ExactCharTester(c);
	}

	public static CharTester oneOf(String characters) {
		return new OneOfCharTester(characters);
	}

	public static CharTester inRange(char from, char to) {
		return new CharRangeTester(from, to);
	}

	public static CharTester whitespace() {
		return new WhitespaceCharTester();
	}

	public abstract boolean isMatch(char c);

	public abstract String buildGrammar();

	private static class ExactCharTester extends CharTester {
		private final char c;

		private ExactCharTester(char c) {
			this.c = c;
		}

		@Override
		public boolean isMatch(char test) {
			return test == c;
		}

		@Override
		public String buildGrammar() {
			return "" + c;
		}
	}

	private static class OneOfCharTester extends CharTester {
		private final String characters;

		private OneOfCharTester(String characters) {
			this.characters = characters;
		}

		@Override
		public boolean isMatch(char test) {
			return characters.indexOf(test) >= 0;
		}

		@Override
		public String buildGrammar() {
			return "[" + characters + "]";
		}
	}

	private static class CharRangeTester extends CharTester {
		private final char from;
		private final char to;

		private CharRangeTester(char from, char to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean isMatch(char test) {
			return test >= from && test <= to;
		}

		@Override
		public String buildGrammar() {
			return "[" + from + "-" + to + "]";
		}
	}

	private static class WhitespaceCharTester extends CharTester {

		@Override
		public boolean isMatch(char test) {
			return Character.isWhitespace(test);
		}

		@Override
		public String buildGrammar() {
			return "[:w:]";
		}
	}
}
