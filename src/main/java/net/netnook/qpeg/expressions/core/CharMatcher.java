package net.netnook.qpeg.expressions.core;

import net.netnook.qpeg.expressions.InvalidExpressionException;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.chars.CharTester;
import net.netnook.qpeg.expressions.chars.CharTesters;

public final class CharMatcher extends SimpleExpression {

	public static Builder using(CharTester charTester) {
		return new Builder().matcher(charTester);
	}

	public static Builder any() {
		return new Builder().matcher(CharTesters.any());
	}

	public static Builder whitespace() {
		return new Builder().matcher(CharTesters.isWhitespace());
	}

	public static Builder character(char c) {
		return new Builder().matcher(CharTesters.is(c));
	}

	public static Builder in(String characters) {
		return new Builder().matcher(CharTesters.in(characters));
	}

	public static Builder inRange(char from, char to) {
		return new Builder().matcher(CharTesters.inRange(from, to));
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

		public Builder zeroOrMore() {
			minCount(0);
			maxUnbounded();
			return this;
		}

		public Builder oneOrMore() {
			minCount(1);
			maxUnbounded();
			return this;
		}

		public Builder count(int count) {
			validate(count >= 1, "Invalid expression: count must be >= 1");
			this.minCount = count;
			this.maxCount = count;
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

		public Builder maxUnbounded() {
			this.maxCount = Integer.MAX_VALUE;
			return this;
		}

		@Override
		protected CharMatcher doBuild() {
			if (minCount > maxCount) {
				throw new InvalidExpressionException("Invalid expression: minCount > maxCount");
			}

			if (getOnSuccess() == null) {
				onSuccess(OnSuccessHandler.PUSH_TEXT);
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

	int getMinCount() {
		return minCount;
	}

	int getMaxCount() {
		return maxCount;
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int count = 0;
		while (count < maxCount) {
			int found = context.peekChar();

			// FIXME: unicode handling ?
			boolean match = (found != RootContext.END_OF_INPUT) && matcher.isMatch(found);
			if (!match) {
				//context.rewindInput();
				break;
			}
			context.incrementPosition();
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
