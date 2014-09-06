package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;

public interface ParsingExpression extends Visitable {

	default ParsingExpression internalGetExpression() {
		return this;
	}

	String buildGrammar();

	/**
	 * Parse the next characters as indicated and return the resulting node.  Return null if
	 * there was not match.
	 * @param context parsing context.
	 * @return resulting node or {@code null} if no match.
	 */
	ParseNode parse(Context context);

	default String name() {
		return getClass().getSimpleName();
	}
}
