package net.netnook.qpeg.expressions;

import net.netnook.qpeg.expressions.Context.Marker;

public class StringMatcher extends SimpleExpression {

	public static Builder of(String str) {
		return new Builder(str);
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private String str;

		private Builder(String str) {
			this.str = str;
		}

		@Override
		public StringMatcher doBuild(BuildContext ctxt) {
			if (!isIgnore() && getOnSuccess() == null) {
				onSuccess(OnSuccessHandler.PUSH_TEXT_TO_STACK);
			}
			return new StringMatcher(this);
		}
	}

	private final String str;

	private StringMatcher(Builder builder) {
		super(builder);
		this.str = builder.str;
	}

	@Override
	public String buildGrammar() {
		// FIXME: escape ' character.
		return "'" + str + "'";
	}

	@Override
	protected boolean parseImpl(Context context, Marker startMarker) {
		for (int i = 0; i < str.length(); i++) {
			if (context.consumeChar() != str.charAt(i)) {
				return false;
			}
		}
		return true;
	}
}
