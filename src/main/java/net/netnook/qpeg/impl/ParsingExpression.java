package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;

public interface ParsingExpression extends Visitable {

//	OnSuccessHandler DEFAULT_ON_SUCCESS = (ctxt,start) -> {
//		int stackEndPosition = ctxt.stackPosition();
//		if (start.stackPosition == stackEndPosition) {
//			ctxt.push(new LeafNode2(ctxt, null, -1, -1));
//		} else {
//			List<Object> popped = ctxt.popTo(start.stackPosition);
//			ctxt.push(new TreeNode2(ctxt, null, -1, -1, popped));
//		}
//	};

	default String getName() {
		return getClass().getSimpleName();
	}

	String buildGrammar();

	String alias();

	/**
	 * Parse the next characters as indicated and return the resulting node.  Return null if
	 * there was not match.
	 *
	 * @param context parsing context.
	 * @return {@code true} if match successful
	 */
	boolean parse(Context context);

	default boolean isIgnore() {
		return false;
	}
}
