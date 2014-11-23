package net.netnook.qpeg.expressions.chars;

public final class CharTesters {

	private CharTesters() {
		// defeat instantiation
	}

	public static CharTester any() {
		return CharAnyTester.INSTANCE;
	}

	public static CharTester isWhitespace() {
		return CharIsWhitespaceTester.INSTANCE;
	}

	public static CharTester horizontalWhitespace() {
		return HorizontalWhitespaceTester.INSTANCE;
	}

	public static CharTester is(char c) {
		return new CharIsTester(c);
	}

	public static CharTester in(String characters) {
		return new CharInTester(characters);
	}

	public static CharTester inRange(char from, char to) {
		return new CharInRangeTester(from, to);
	}

}
