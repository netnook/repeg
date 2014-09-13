package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.ParseTree;

public interface ParsingExpression extends Visitable {

	default ParsingExpression internalGetExpression() {
		return this;
	}

	String buildGrammar();

	String alias();

	/**
	 * Parse the next characters as indicated and return the resulting node.  Return null if
	 * there was not match.
	 *
	 * @param context parsing context.
	 * @return resulting node or {@code null} if no match.
	 */
	ParseNode parse(Context context);

	default ParseTree parse(CharSequence input) throws NoMatchException {
		Context context = new Context(input);
		ParseNode root = parse(context);
		if (root == null) {
			throw new NoMatchException();
		}
		return new ParseTree(root);
	}

	//	ParsingExpression onSuccess(Consumer<? super ParsingExpression> onSuccess);

	default String name() {
		return getClass().getSimpleName();
	}

	default boolean isIgnore() {
		return false;
	}

	//	ParsingExpression ignore();
}
