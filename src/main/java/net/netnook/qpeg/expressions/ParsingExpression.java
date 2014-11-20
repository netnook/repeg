package net.netnook.qpeg.expressions;

public interface ParsingExpression extends Visitable {

	default String getName() {
		return getClass().getSimpleName();
	}

	String buildGrammar();

	/**
	 * Parse the next characters as indicated and return the resulting node.  Return null if
	 * there was not match.
	 *
	 * @param context parsing context.
	 * @return {@code true} if match successful
	 */
	boolean parse(RootContext context);

	default boolean isIgnore() {
		return false;
	}
}
