package net.netnook.qpeg.expressions.core;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;

public final class StringMatcher extends SimpleExpression {

	public static Builder of(String str) {
		return new Builder(str);
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private String str;

		private Builder(String str) {
			this.str = str;
		}

		@Override
		protected StringMatcher doBuild() {
			if (getOnSuccess() == null) {
				onSuccess(OnSuccessHandler.PUSH_TEXT);
			}
			return new StringMatcher(this);
		}
	}

	private final String str;

	private StringMatcher(Builder builder) {
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
