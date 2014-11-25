package net.netnook.qpeg.expressions.extras;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;

public final class NewlineExpression extends SimpleExpression {

	private static final int LF = '\n';
	private static final int CR = '\r';

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends ParsingExpressionBuilderBase {

		private Builder() {
			// no-op
		}

		@Override
		protected NewlineExpression doBuild() {
			return new NewlineExpression(this);
		}
	}

	private NewlineExpression(Builder builder) {
		super(builder.getOnSuccess());
	}

	@Override
	public String buildGrammar() {
		return "('\\n'|'\\r\\n'|'\\r')";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		int c1 = context.charAt(startPosition);

		// TODO: compare this to unicode line termination
		if (c1 == LF) {
			context.setPosition(startPosition + 1);
			return true;
		} else if (c1 == CR) {
			int pos = startPosition + 1;
			if (context.charAt(pos) == LF) {
				context.setPosition(pos + 1);
			} else {
				context.setPosition(pos);
			}
			return true;
		}

		return false;
	}
}
