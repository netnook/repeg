package net.netnook.repeg.util;

import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.RootContext;

public interface ParseListener {

	ParseListener NO_OP = new ParseListener() {
		@Override
		public void onExpressionEnter(ParsingExpression expression, RootContext context) {
			// no-op
		}

		@Override
		public void onExpressionExit(ParsingExpression expression, RootContext context, int startPosition, int startStackIdx, boolean success) {
			// no-op
		}
	};

	void onExpressionEnter(ParsingExpression expression, RootContext context);

	void onExpressionExit(ParsingExpression expression, RootContext context, int startPosition, int startStackIdx, boolean success);
}
