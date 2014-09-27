package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.LeafNode2;

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

	protected CharMatcher(boolean ignore, String alias) {
		super(ignore, alias, OnSuccessHandler.NO_OP);
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
		public boolean parse(Context context) {
			Context.Marker start = context.marker();

			while (true) {
				char found = context.peekChar();
				if (!Character.isWhitespace(found)) {
					break;
				}
				context.incrementPosition();
			}

			int endPos = context.position();

			if (endPos == start.inputPosition) {
				return false;
			}

			// FIXME: Should something be added to the stack for whitespace
			//context.lastStart(start);

			//onSuccess.accept(context, start);

			//context.push(new LeafNode2(context, this, startPos, endPos));

			//onSuccess.accept(context, stackPosition); FIXME: handle on success !!!

			return true;
		}
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
		@Deprecated
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
		public boolean parse(Context context) {
			Context.Marker start = context.marker();
			char found = context.peekChar();
			if (found != c) {
				return false;
			}
			int endPos = context.incrementPosition();

			// FIXME: what should the default be ?
			context.push(new LeafNode2(context, this, start.inputPosition, endPos));
			//context.push(new CharacterNode(context, this, startPos, endPos));
			// FIXME: handle on success
			return true;
		}
	}

	public static AnyOfCharMatcherBuilder anyOf(String characters) {
		return new AnyOfCharMatcherBuilder().characters(characters);
	}

	public static class AnyOfCharMatcherBuilder extends ParsingExpressionBuilderBase {
		private String characters;

		public AnyOfCharMatcherBuilder characters(String characters) {
			this.characters = characters;
			return this;
		}

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
		public boolean parse(Context context) {
			int startPos = context.position();
			char found = context.peekChar();
			if (characters.indexOf(found) < 0) {
				return false;
			}
			int endPos = context.incrementPosition();
			// FIXME: what should the default be ?
			context.push(new LeafNode2(context, this, startPos, endPos));
			//context.push(new CharacterNode(context, this, startPos, endPos));
			// FIXME: handle on sucess
			return true;
		}
	}

	public static CharRangeMatcherBuilder inRange(char from, char to) {
		return new CharRangeMatcherBuilder().range(from, to);
	}

	public static class CharRangeMatcherBuilder extends ParsingExpressionBuilderBase {
		private char from;
		private char to;

		public CharRangeMatcherBuilder range(char from, char to) {
			this.from = from;
			this.to = to;
			return this;
		}

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

	private static class CharRangeMatcher extends CharMatcher {
		private final char from;
		private final char to;

		private CharRangeMatcher(char from, char to, boolean ignore, String alias) {
			super(ignore, alias);
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean parse(Context context) {
			int startPos = context.position();
			char found = context.peekChar();
			if (!isMatch(found)) {
				return false;
			}
			int endPos = context.incrementPosition();

			//context.push(new CharacterNode(context, this, startPos, endPos));
			// FIXME: what should the default be ?
			context.push(new LeafNode2(context, this, startPos, endPos));
			// FIXME: handle on success
			return true;
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
