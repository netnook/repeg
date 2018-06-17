package net.netnook.repeg.parsetree;

import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.expressions.RootContext;

public abstract class ParseNode {

	protected final RootContext context;
	protected final Expression expression;
	protected final int startPos;
	protected final int endPos;

	public ParseNode(RootContext context, Expression expression, int startPos, int endPos) {
		this.context = context;
		this.expression = expression;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	@Override
	public final String toString() {
		StringBuilder buf = new StringBuilder();
		dump(buf, "");
		return buf.toString();
	}

	protected abstract void dump(StringBuilder buf, String prefix);
}
