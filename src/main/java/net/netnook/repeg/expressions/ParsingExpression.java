package net.netnook.repeg.expressions;

/**
 * Common interface for expressions.  All expressions used in this library must implement this interface.
 */
public interface ParsingExpression extends Visitable {

	/**
	 * Get a name for this expression.  Used for debugging.
	 *
	 * @return expression name.
	 */
	default String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * Parse the next input characters as indicated by the {@code context} and return whether or not the expression
	 * matches.
	 * <p>
	 * If a match has occurred (i.e. when this method returns {@code true}), the {@code context} must have been
	 * moved to the first character following the match.  An expression may optionally add or manipulate elements
	 * on the stack held by the {@code context}.  A well behaved expression should however never attempt to modify any
	 * stack contents not resulting from itself or any of it's descendent expressions.
	 * <p>
	 * If no match occurred (i.e. when this method returns {@code false}, the input position and stack must be
	 * considered invalid by the caller.  It is the caller's responsibility to reset the input position and stack
	 * (via the {@code context}) such that parsing may resume at the required point.
	 *
	 * @param context parsing context.
	 * @return {@code true} if match successful, {@code false otherwise}
	 */
	boolean parse(RootContext context);

	/**
	 * Build the grammar for this expression.
	 *
	 * @return grammar.
	 */
	String buildGrammar();
}
