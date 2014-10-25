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
		public StringMatcher build(BuildContext ctxt) {
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
		return "'" + str + "'";
	}

	@Override
	public boolean parse(Context context) {
		onExpressionEnter(context);

		Marker startMarker = context.updateMark();

		boolean success = true;
		for (int i = 0; i < str.length(); i++) {
			if (context.consumeChar() != str.charAt(i)) {
				success = false;
				break;
			}
		}

		if (success && !ignore) {
			context.pushCurrentText();
		}

		onExpressionExit(context, startMarker, success);

		return success;
	}
}
