package net.netnook.repeg.util;

import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.expressions.RootContext;

/**
 * Not part of public API
 * FIXME: Listener should only get Context (rather than RootContext) and be in top level package
 */
public interface ParseListener {

	ParseListener NO_OP = new ParseListener() {
		@Override
		public void onExpressionEnter(Expression expression, RootContext context) {
			// no-op
		}

		@Override
		public void onExpressionExit(Expression expression, RootContext context, int startPosition, int startStackIdx, boolean success) {
			// no-op
		}
	};

	void onExpressionEnter(Expression expression, RootContext context);

	void onExpressionExit(Expression expression, RootContext context, int startPosition, int startStackIdx, boolean success);
}
