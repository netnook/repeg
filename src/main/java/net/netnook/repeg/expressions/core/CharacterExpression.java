package net.netnook.repeg.expressions.core;

import net.netnook.repeg.expressions.InvalidExpressionException;
import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpressionBase;
import net.netnook.repeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;
import net.netnook.repeg.expressions.SimpleExpression;
import net.netnook.repeg.expressions.chars.CharMatcher;

/**
 * Character expression e.g. '{@code '[a]'}' (character 'a'), '{@code '[a-z]'}' (lower case ascii letter).
 * <p>
 * This expression matches individual characters in the input.  By default a single character is matched
 * but the minCount and maxCount properties can be used to match repeating characters.
 * This expression will match only if the minimum number of repeats are matched (default 1) and will
 * never consume more than the maximum number of repetitions (default 1).
 * <p>
 * This expression has no default {@link net.netnook.repeg.expressions.OnSuccessHandler}.
 */
public final class CharacterExpression extends ParsingExpressionBase implements SimpleExpression {

	public static Builder using(CharMatcher matcher) {
		return new Builder().matcher(matcher);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private CharMatcher matcher;
		private int minCount = 1;
		private int maxCount = 1;

		public Builder matcher(CharMatcher matcher) {
			this.matcher = matcher;
			return this;
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		Builder zeroOrMore() {
			minCount(0);
			maxUnbounded();
			return this;
		}

		Builder oneOrMore() {
			minCount(1);
			maxUnbounded();
			return this;
		}

		Builder count(int count) {
			validate(count >= 1, "Invalid expression: count must be >= 1");
			this.minCount = count;
			this.maxCount = count;
			return this;
		}

		Builder minCount(int minCount) {
			validate(minCount >= 0, "Invalid expression: min count must be >= 0");
			this.minCount = minCount;
			return this;
		}

		Builder maxCount(int maxCount) {
			validate(maxCount >= 1, "Invalid expression: max count must be >= 1");
			this.maxCount = maxCount;
			return this;
		}

		Builder maxUnbounded() {
			this.maxCount = Integer.MAX_VALUE;
			return this;
		}

		@Deprecated
			// FIXME: not good ????
		boolean hasDefaults() {
			return minCount == 1 //
					&& maxCount == 1 //
					&& getOnSuccess() == null;
		}

		@Override
		protected CharacterExpression doBuild() {
			if (minCount > maxCount) {
				throw new InvalidExpressionException("Invalid expression: minCount > maxCount");
			}
			return new CharacterExpression(this);
		}
	}

	private final CharMatcher matcher;
	private final int minCount;
	private final int maxCount;

	protected CharacterExpression(Builder builder) {
		super(builder.getOnSuccess());
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

		int pos = startPosition;
		// TODO: better way to handle int overrun ? (see java ArrayList index checking for solution)
		int maxPos = (maxCount == Integer.MAX_VALUE) ? Integer.MAX_VALUE : pos + maxCount;
		while (pos < maxPos) {
			int found = context.charAt(pos);

			// TODO: unicode handling ?
			boolean match = (found != RootContext.END_OF_INPUT) && matcher.isMatch(found);
			if (!match) {
				break;
			}
			pos++;
		}

		context.setPosition(pos);

		return ((pos - startPosition) >= minCount); // NOTE: no maxCount check - not necessary due to loop condition above
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