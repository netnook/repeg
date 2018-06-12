package net.netnook.repeg.expressions;

/**
 * Exception thrown to indicate some problem when building the expression.
 */
public final class InvalidExpressionException extends RuntimeException {

	public InvalidExpressionException(String message) {
		super(message);
	}
}
