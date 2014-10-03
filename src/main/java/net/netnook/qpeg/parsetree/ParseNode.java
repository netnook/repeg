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

	public CharSequence getText() {
		throw new UnsupportedOperationException();
		//return context.getInput(startPos, endPos);
	}
}
