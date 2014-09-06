package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.CharacterNode;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;

public abstract class CharMatcher implements SimpleExpression {

	private static final WhitespaceCharMatcher WHITESPACE_CHAR_MATCHER = new WhitespaceCharMatcher();

	public static WhitespaceCharMatcher whitespace() {
		return WHITESPACE_CHAR_MATCHER;
	}

	private static class WhitespaceCharMatcher extends CharMatcher {

		@Override
		public String buildGrammar() {
			return "[:w:]";
		}

		@Override
		public ParseNode parse(Context context) {
			int startPos = context.position();

			while (true) {
				char found = context.peekChar();
				if (!Character.isWhitespace(found)) {
					break;
				}
				context.incrementPosition();
			}

			int endPos = context.position();

			if (endPos > startPos) {
				return new CharacterNode(context, this, startPos, endPos);
			}

			return null;
		}
	}

	public static CharMatcher of(char c) {
		return new SingleCharMatcher(c);
	}

	private static class SingleCharMatcher extends CharMatcher {
		private final char c;

		private SingleCharMatcher(char c) {
			this.c = c;
		}

		@Override
		public String buildGrammar() {
			return "'" + c + "'";
		}

		@Override
		public ParseNode parse(Context context) {
			int startPos = context.position();
			char found = context.peekChar();
			if (found != c) {
				return null;
			}
			int endPos = context.incrementPosition();
			return new CharacterNode(context, this, startPos, endPos);
		}
	}

	public static CharMatcher anyOf(String characters) {
		return new AnyOfCharMatcher(characters);
	}

	private static class AnyOfCharMatcher extends CharMatcher {
		private final String characters;

		private AnyOfCharMatcher(String characters) {
			this.characters = characters;
		}

		@Override
		public String buildGrammar() {
			return "[" + characters + "]";
		}

		@Override
		public ParseNode parse(Context context) {
			int startPos = context.position();
			char found = context.peekChar();
			if (characters.indexOf(found) < 0) {
				return null;
			}
			int endPos = context.incrementPosition();
			return new CharacterNode(context, this, startPos, endPos);
		}
	}

	public static CharMatcher inRange(char from, char to) {
		return new CharRangeMatcher(from, to);
	}

	private static class CharRangeMatcher extends CharMatcher {
		private final char from;
		private final char to;

		private CharRangeMatcher(char from, char to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public ParseNode parse(Context context) {
			int startPos = context.position();
			char found = context.peekChar();
			if (!isMatch(found)) {
				return null;
			}
			int endPos = context.incrementPosition();
			return new CharacterNode(context, this, startPos, endPos);
		}

		private boolean isMatch(char found) {
			return found >= from && found <= to;
		}

		@Override
		public String buildGrammar() {
			return "[" + from + "-" + to + "]";
		}
	}
}
