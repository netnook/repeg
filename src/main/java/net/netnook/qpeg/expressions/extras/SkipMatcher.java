package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.chars.CharTester;

public final class SkipMatcher extends SimpleExpression {

	public static Builder using(CharTester charTester) {
		return new Builder().matcher(charTester);
	}

	public static class Builder extends ParsingExpressionBuilderBase {
		private CharTester matcher;

		public Builder matcher(CharTester matcher) {
			this.matcher = matcher;
			return this;
		}

		@Override
		public Builder ignore() {
			super.ignore();
			return this;
		}

		@Override
		protected SkipMatcher doBuild() {
			if (getOnSuccess() != null) {
				throw new RuntimeException();
			}
			return new SkipMatcher(this);
		}
	}

	private final CharTester matcher;

	protected SkipMatcher(Builder builder) {
		super(builder);
		this.matcher = builder.matcher;
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		while (true) {
			int found = context.peekChar();

			boolean match = (found != RootContext.END_OF_INPUT) && matcher.isMatch(found);
			if (!match) {
				break;
			}

			context.incrementPosition();
		}

		return true;
	}

	@Override
	public String buildGrammar() {
		throw new UnsupportedOperationException();
	}
}
