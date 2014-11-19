package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.Context.Marker;
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
	protected boolean parseImpl(Context context, Marker startMarker) {
		int count = 0;
		while (count < maxCount) {
			int found = context.consumeChar();

			// FIXME: unicode handling ?
			boolean match = matcher.isMatch(found);
			if (!match) {
				context.rewindPosition(1);
				break;
			}
			count++;
		}

		boolean success = (count >= minCount); // NOTE: no maxCount check - not possible due to loop condition above

		if (success && !ignore) {
			context.pushCurrentText();
		}

		return success;
	}

	@Override
	public String buildGrammar() {
		StringBuilder buf = new StringBuilder();
		buf.append(matcher.buildGrammar());

		if (minCount == 1 && maxCount == 1) {
			// no-op
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
