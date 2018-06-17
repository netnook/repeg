package net.netnook.repeg.parsetree;

import net.netnook.repeg.Context;
import net.netnook.repeg.Expression;

public abstract class ParseNode {

	protected final Expression expression;
	protected final int startPos;
	protected final int endPos;
	protected final CharSequence text;

	public ParseNode(Context context, Expression expression) {
		this.expression = expression;
		this.startPos = context.getStartPosition();
		this.endPos = context.getCurrentPosition();
		this.text = context.getCharSequence();
	}

	@Override
	public final String toString() {
		StringBuilder buf = new StringBuilder();
		dump(buf, "");
		return buf.toString();
	}

	protected abstract void dump(StringBuilder buf, String prefix);
}
