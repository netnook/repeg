package net.netnook.repeg.expressions.core;

import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.repeg.expressions.RootContext;
import net.netnook.repeg.expressions.SimpleExpression;

public final class StringExpression extends SimpleExpression {

	public static Builder of(String str) {
		return new Builder(str);
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private String str;

		private Builder(String str) {
			this.str = str;
		}

		@Override
		public Builder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		protected StringExpression doBuild() {
			return new StringExpression(this);
		}
	}

	private final String str;

	private StringExpression(Builder builder) {
		super(builder.getOnSuccess());
		this.str = builder.str;
	}

	@Override
	public String buildGrammar() {
		return "'" + str.replace("'", "\\'") + "'";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int pos = startPosition;
		for (int i = 0; i < str.length(); i++, pos++) {
			if (context.charAt(pos) != str.charAt(i)) {
				return false;
			}
		}
		context.setPosition(pos);
		return true;
	}
}
