package net.netnook.qpeg.expressions;

public abstract class CharMatcher extends SimpleExpression {

	protected CharMatcher(ParsingExpressionBuilderBase builder) {
		super(builder);
	}

	@Override
	public boolean parse(Context context) {
		context.mark();
		char found = context.consumeChar();
		if (!isMatch(found)) {
			return false;
		}

		if (!ignore) {
			context.pushCurrentText();
			onSuccess.accept(context);
		}

		return true;
	}

	protected abstract boolean isMatch(char found);

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
		public SingleCharMatcherBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public SingleCharMatcher build(BuildContext ctxt) {
			return new SingleCharMatcher(this);
		}
	}

	private static class SingleCharMatcher extends CharMatcher {
		private final char c;

		private SingleCharMatcher(SingleCharMatcherBuilder builder) {
			super(builder);
			this.c = builder.c;
		}

		@Override
		public String buildGrammar() {
			return "'" + c + "'";
		}

		@Override
		protected boolean isMatch(char found) {
			return found == c;
		}
	}

	public static OneOfCharMatcherBuilder oneOf(String characters) {
		return new OneOfCharMatcherBuilder().characters(characters);
	}

	public static class OneOfCharMatcherBuilder extends ParsingExpressionBuilderBase {
		private String characters;

		public OneOfCharMatcherBuilder characters(String characters) {
			this.characters = characters;
			return this;
		}

		@Override
		public OneOfCharMatcherBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public OneOfCharMatcher build(BuildContext ctxt) {
			return new OneOfCharMatcher(this);
		}
	}

	private static class OneOfCharMatcher extends CharMatcher {
		private final String characters;

		private OneOfCharMatcher(OneOfCharMatcherBuilder builder) {
			super(builder);
			this.characters = builder.characters;
		}

		@Override
		public String buildGrammar() {
			return "[" + characters + "]";
		}

		@Override
		protected boolean isMatch(char found) {
			return characters.indexOf(found) >= 0;
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
		public CharRangeMatcherBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public CharRangeMatcher build(BuildContext ctxt) {
			return new CharRangeMatcher(this);
		}
	}

	private static class CharRangeMatcher extends CharMatcher {
		private final char from;
		private final char to;

		private CharRangeMatcher(CharRangeMatcherBuilder builder) {
			super(builder);
			this.from = builder.from;
			this.to = builder.to;
		}

		@Override
		protected boolean isMatch(char found) {
			return found >= from && found <= to;
		}

		@Override
		public String buildGrammar() {
			return "[" + from + "-" + to + "]";
		}
	}
}
