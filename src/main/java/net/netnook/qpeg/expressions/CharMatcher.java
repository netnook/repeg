package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.chars.CharTester;

public final class CharMatcher extends SimpleExpression {

	public static Builder any() {
		return new Builder().matcher(CharTester.any());
	}

	public static Builder whitespace() {
		return new Builder().matcher(CharTester.isWhitespace());
	}

	public static Builder is(char c) {
		return new Builder().matcher(CharTester.is(c));
	}

	public static Builder in(String characters) {
		return new Builder().matcher(CharTester.in(characters));
	}

	public static Builder inRange(char from, char to) {
		return new Builder().matcher(CharTester.inRange(from, to));
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

		public Builder invert() {
			this.matcher = this.matcher.invert();
			return this;
		}

		public Builder minCount(int minCount) {
			validate(minCount >= 0, "Invalid expression: min count must be >= 0");
			this.minCount = minCount;
			return this;
		}

		public Builder maxCount(int maxCount) {
			validate(maxCount >= 1, "Invalid expression: max count must be >= 1");
			this.maxCount = maxCount;
			return this;
		}

		public Builder maxCountUnbounded() {
			this.maxCount = Integer.MAX_VALUE;
			return this;
		}

		@Override
		public CharMatcher doBuild(BuildContext ctxt) {
			if (minCount > maxCount) {
				throw new InvalidExpressionException("Invalid expression: minCount > maxCount");
			}

			if (getOnSuccess() == null) {
				onSuccess(OnSuccessHandler.PUSH_TEXT_TO_STACK);
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
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int count = 0;
		while (count < maxCount) {
			int found = context.consumeChar();

			// FIXME: unicode handling ?
			boolean match = matcher.isMatch(found);
			if (!match) {
				context.rewindInput();
				break;
			}
			count++;
		}

		return (count >= minCount); // NOTE: no maxCount check - not necessary due to loop condition above
	}

	@Override
	public String buildGrammar() {
		StringBuilder buf = new StringBuilder();
		buf.append(matcher.buildGrammar());

		if (minCount == 1 && maxCount == 1) {
			// no-op
		} else if (minCount == 0 && maxCount == 1) {
			buf.append('?');
		} else if (maxCount == Integer.MAX_VALUE) {
			if (minCount == 0) {
				buf.append('*');
			} else if (minCount == 1) {
				buf.append('+');
			} else {
				buf.append('{').append(minCount).append(",}");
			}
		} else if (minCount == maxCount) {
			buf.append('{').append(minCount).append('}');
		} else {
			buf.append('{').append(minCount).append(',').append(maxCount).append('}');
		}

		return buf.toString();
	}
}
