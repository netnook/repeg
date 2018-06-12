package net.netnook.repeg.expressions;

/**
 * Common interface for all expression builders.
 */
public interface ParsingExpressionBuilder {

	/**
	 * Build and return a {@link ParsingExpression} for this builder.
	 *
	 * @return new expression.
	 */
	ParsingExpression build();

	/**
	 * Utility method to convert an array of builders to an array of expressions, calling each
	 * builder's {@link ParsingExpressionBuilder#build()} method in turn.
	 *
	 * @param builders for which to build expressions.
	 * @return resulting list of expressions.
	 */
	static ParsingExpression[] build(ParsingExpressionBuilder[] builders) {
		ParsingExpression[] results = new ParsingExpression[builders.length];
		for (int i = 0; i < builders.length; i++) {
			results[i] = builders[i].build();
		}
		return results;
	}
}
