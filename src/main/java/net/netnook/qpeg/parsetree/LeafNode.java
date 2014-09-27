package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.Context;
import net.netnook.qpeg.impl.ParsingExpression;

public class LeafNode extends ParseNode {

	public LeafNode(Context context, ParsingExpression expression, int startPos, int endPos) {
		super(context, expression, startPos, endPos);
	}
}
