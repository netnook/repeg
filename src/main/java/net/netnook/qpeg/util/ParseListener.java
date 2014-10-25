package net.netnook.qpeg.util;

import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.ParsingExpression;

public interface ParseListener {

	ParseListener NO_OP = new ParseListener() {
		@Override
		public void onExpressionEnter(ParsingExpression expression, Context context) {
			// no-op
		}

		@Override
		public void onExpressionExit(ParsingExpression expression, Context context, boolean success) {
			// no-op
		}
	};

	void onExpressionEnter(ParsingExpression expression, Context context);

	void onExpressionExit(ParsingExpression expression, Context context, boolean success);
}
