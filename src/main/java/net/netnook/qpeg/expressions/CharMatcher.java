package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.Context.Marker;

public final class CharMatcher extends SimpleExpression {

	public static Builder of(char c) {
		return new Builder().matcher(CharTester.of(c));
	}

	public static Builder oneOf(String characters) {
		return new Builder().matcher(CharTester.oneOf(characters));
	}

	public static Builder inRange(char from, char to) {
		return new Builder().matcher(CharTester.inRange(from, to));
	}

	public static Builder whitespace() {
		return new Builder().matcher(CharTester.whitespace());
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private CharTester matcher;
		private int minCount = 1;
		private int maxCount = 1;

		public Builder matcher(CharTester matcher) {
			this.matcher = matcher;
			return this;
		}

		@Override
		public Builder ignore() {
			super.ignore();
			return this;
		}

		public Builder minCount(int minCount) {
			this.minCount = minCount;
			return this;
		}

		public Builder maxCount(int maxCount) {
			this.maxCount = maxCount;
			return this;
		}

		public Builder maxCountUnbounded() {
			this.maxCount = Integer.MAX_VALUE;
			return this;
		}

		@Override
		public CharMatcher build(BuildContext ctxt) {
			if (minCount > maxCount) {
				// FIXME: create specific exception
				throw new RuntimeException("Invalid expression: minCount > maxCount");
			}
			return new CharMatcher(this);
		}
	}

	private final CharTester matcher;
	private final int minCount;
	private final int maxCount;

	protected CharMatcher(Builder builder) {
		super(builder);
		this.matcher = builder.matcher;
		this.minCount = builder.minCount;
		this.maxCount = builder.maxCount;
	}

	@Override
	public boolean parse(Context context) {
		Marker startMarker = context.mark();

		int count = 0;
		while (count < maxCount) {
			char found = context.consumeChar();

			boolean match = matcher.isMatch(found);
			if (!match) {
				context.rewindPosition(1);
				break;
			}
			count++;
		}

		boolean success = (count >= minCount); // NOTE: no maxCount check - not possible due to loop condition above

		if (success) {
			if (!ignore) {
				context.pushCurrentText();
			}
			onSuccess(context, startMarker);
		}

		return success;
	}

	@Override
	public String buildGrammar() {
		return "'" + matcher.buildGrammar() + "'";
	}
}
