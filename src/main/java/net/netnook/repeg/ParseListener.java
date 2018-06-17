package net.netnook.repeg;

/**
 * Listener for parser events
 */
public interface ParseListener {

	ParseListener NO_OP = new ParseListener() {
		@Override
		public void onExpressionEnter(Expression expression, Context context) {
			// no-op
		}

		@Override
		public void onExpressionExit(Expression expression, Context context, boolean success) {
			// no-op
		}
	};

	void onExpressionEnter(Expression expression, Context context);

	void onExpressionExit(Expression expression, Context context, boolean success);
}
