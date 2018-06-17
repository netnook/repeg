package net.netnook.repeg;

/**
 * Common interface for all expression builders.
 */
public interface ExpressionBuilder {

	/**
	 * Build and return a {@link Expression} for this builder.
	 *
	 * @return new expression.
	 */
	Expression build();
}
