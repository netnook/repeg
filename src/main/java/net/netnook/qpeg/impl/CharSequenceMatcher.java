package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.impl.Context.Marker;

public abstract class CharSequenceMatcher extends SimpleExpression {

	public static WhitespaceCharMatcherBuilder whitespace() {
		return new WhitespaceCharMatcherBuilder();
	}

	public static class WhitespaceCharMatcherBuilder extends ParsingExpressionBuilderBase {

		@Override
		public WhitespaceCharMatcher build(BuildContext ctxt) {
			return new WhitespaceCharMatcher(this);
		}
	}

	protected CharSequenceMatcher(ParsingExpressionBuilderBase builder) {
		super(builder);
	}

	private static class WhitespaceCharMatcher extends CharSequenceMatcher {

		WhitespaceCharMatcher(WhitespaceCharMatcherBuilder builder) {
			super(builder);
		}

		@Override
		public String buildGrammar() {
			return "[:w:]";
		}

		@Override
		public boolean parse(Context context) {
			Marker start = context.marker();

			while (true) {
				char found = context.peekChar();
				if (!isMatch(found)) {
					break;
				}
				context.incrementPosition();
			}

			int endPos = context.position();

			if (endPos == start.inputPosition) {
				return false;
			}

			if (!ignore) {
				context.mark(start);
				context.pushText(start);
				onSuccess.accept(context);
			}

			return true;
		}

		private boolean isMatch(char found) {
			return Character.isWhitespace(found);
		}
	}

	public static StringMatcherBuilder string(String str) {
		return new StringMatcherBuilder(str);
	}

	public static class StringMatcherBuilder extends ParsingExpressionBuilderBase {

		private String str;

		private StringMatcherBuilder(String str) {
			this.str = str;
		}

		@Override
		public StringMatcher build(BuildContext ctxt) {
			return new StringMatcher(this);
		}
	}

	private static class StringMatcher extends CharSequenceMatcher {

		private final String str;

		StringMatcher(StringMatcherBuilder builder) {
			super(builder);
			this.str = builder.str;
		}

		@Override
		public String buildGrammar() {
			return "'" + str + "'";
		}

		@Override
		public boolean parse(Context context) {
			Marker start = context.marker();
			for (int i = 0; i < str.length(); i++) {
				if (context.nextChar() != str.charAt(i)) {
					return false;
				}
			}

			if (!ignore) {
				context.mark(start);
				context.pushText(start);
				onSuccess.accept(context);
			}

			return true;
		}
	}
}
