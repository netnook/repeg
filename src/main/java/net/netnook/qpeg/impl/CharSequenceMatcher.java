package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;

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
			context.mark();

			while (true) {
				char found = context.consumeChar();
				if (!isMatch(found)) {
					context.rewindPosition(1);
					break;
				}
			}

			if (context.inputLength() == 0) {
				return false;
			}

			if (!ignore) {
				context.pushCurrentText();
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
			context.mark();
			for (int i = 0; i < str.length(); i++) {
				if (context.consumeChar() != str.charAt(i)) {
					return false;
				}
			}

			if (!ignore) {
				context.pushCurrentText();
				onSuccess.accept(context);
			}

			return true;
		}
	}
}
