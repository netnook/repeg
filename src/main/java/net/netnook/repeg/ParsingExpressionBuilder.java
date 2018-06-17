package net.netnook.repeg;

import net.netnook.repeg.expressions.Expression;

/**
 * Common interface for all expression builders.
 */
public interface ParsingExpressionBuilder {

	/**
	 * Build and return a {@link Expression} for this builder.
	 *
	 * @return new expression.
	 */
	Expression build();
}
