package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.ParsingExpression;

public abstract class ParseNode {

	protected final Context context;
	protected final ParsingExpression expression;
	protected final int startPos;
	protected final int endPos;

	public ParseNode(Context context, ParsingExpression expression, int startPos, int endPos) {
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
