package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.CharacterNode;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;

public abstract class CharMatcher extends SimpleExpression {

	public static WhitespaceCharMatcherBuilder whitespace() {
		return WHITESPACE_CHAR_MATCHER_BUILDER;
	}

	public static class WhitespaceCharMatcherBuilder extends ParsingExpressionBuilderBase {

		@Override
		public WhitespaceCharMatcher build(BuildContext ctxt) {
			return WHITESPACE_CHAR_MATCHER;
		}
	}

	private static final WhitespaceCharMatcherBuilder WHITESPACE_CHAR_MATCHER_BUILDER = new WhitespaceCharMatcherBuilder();
	private static final WhitespaceCharMatcher WHITESPACE_CHAR_MATCHER = new WhitespaceCharMatcher(false, null);

	//	public static WhitespaceCharMatcher whitespace() {
	//		return WHITESPACE_CHAR_MATCHER;
	//	}

	protected CharMatcher(boolean ignore, String alias) {
		super(ignore, alias);
	}

	private static class WhitespaceCharMatcher extends CharMatcher {

		@Override
		public String buildGrammar() {
			return "[:w:]";
		}

		WhitespaceCharMatcher(boolean ignore, String alias) {
			super(ignore, alias);
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

		//		@Override
		//		public CharMatcher ignore() {
		//			return new WhitespaceCharMatcher(true, alias);
		//		}
	}

	public static SingleCharMatcherBuilder of(char c) {
		return new SingleCharMatcherBuilder().character(c);
	}

	public static class SingleCharMatcherBuilder extends ParsingExpressionBuilderBase {
		private char c;

		public SingleCharMatcherBuilder character(char c) {
			this.c = c;
			return this;
		}

		@Override
		public SingleCharMatcherBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public SingleCharMatcherBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public SingleCharMatcherBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public SingleCharMatcher build(BuildContext ctxt) {
			return new SingleCharMatcher(c, ignore(), alias());
		}
	}

	//	public static CharMatcher of(char c) {
	//		return new SingleCharMatcher(c, false, null);
	//	}

	private static class SingleCharMatcher extends CharMatcher {
		private final char c;

		private SingleCharMatcher(char c, boolean ignore, String alias) {
			super(ignore, alias);
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

		//		@Override
		//		public SingleCharMatcher ignore() {
		//			return new SingleCharMatcher(c, true, alias);
		//		}
	}

	public static AnyOfCharMatcherBuilder anyOf(String characters) {
		return new AnyOfCharMatcherBuilder().characters(characters);
	}

	public static class AnyOfCharMatcherBuilder extends ParsingExpressionBuilderBase {
		private String characters;
		//		private Function<? super OneOrMoreNode, ParseNode> onSuccess = NO_OP;

		public AnyOfCharMatcherBuilder characters(String characters) {
			this.characters = characters;
			return this;
		}

		//		public AnyOfCharMatcherBuilder onSuccess(Function<? super OneOrMoreNode, ParseNode> onSuccess) {
		//			this.onSuccess = onSuccess;
		//			return this;
		//		}

		@Override
		public AnyOfCharMatcherBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public AnyOfCharMatcherBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public AnyOfCharMatcherBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public AnyOfCharMatcher build(BuildContext ctxt) {
			return new AnyOfCharMatcher(characters, ignore(), alias());
		}
	}

	//	public static CharMatcher anyOf(String characters) {
	//		return new AnyOfCharMatcher(characters, false, null);
	//	}

	private static class AnyOfCharMatcher extends CharMatcher {
		private final String characters;

		private AnyOfCharMatcher(String characters, boolean ignore, String alias) {
			super(ignore, alias);
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

		//		@Override
		//		public AnyOfCharMatcher ignore() {
		//			return new AnyOfCharMatcher(characters, true, alias);
		//		}
	}

	public static CharRangeMatcherBuilder inRange(char from, char to) {
		return new CharRangeMatcherBuilder().range(from, to);
	}

	//
	//	public static OneOrMore.OneOrMoreBuilder of(ParsingExpressionBuilder expression) {
	//		return new OneOrMore.OneOrMoreBuilder().expression(expression);
	//	}

	public static class CharRangeMatcherBuilder extends ParsingExpressionBuilderBase {
		private char from;
		private char to;
		//		private ParsingExpressionBuilder expression;
		//		private Function<? super OneOrMoreNode, ParseNode> onSuccess = NO_OP;

		public CharRangeMatcherBuilder range(char from, char to) {
			this.from = from;
			this.to = to;
			//			this.expression = expression;
			return this;
		}

		//		public CharRangeMatcherBuilder onSuccess(Function<? super OneOrMoreNode, ParseNode> onSuccess) {
		//			this.onSuccess = onSuccess;
		//			return this;
		//		}

		@Override
		public CharRangeMatcherBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public CharRangeMatcherBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public CharRangeMatcherBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public CharRangeMatcher build(BuildContext ctxt) {
			return new CharRangeMatcher(from, to, ignore(), alias());
		}
	}

	//	public static CharMatcher inRange(char from, char to) {
	//		return new CharRangeMatcher(from, to, false, null);
	//	}

	private static class CharRangeMatcher extends CharMatcher {
		private final char from;
		private final char to;

		private CharRangeMatcher(char from, char to, boolean ignore, String alias) {
			super(ignore, alias);
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

		//		@Override
		//		public CharRangeMatcher ignore() {
		//			return new CharRangeMatcher(from, to, true, alias);
		//		}
	}
}
