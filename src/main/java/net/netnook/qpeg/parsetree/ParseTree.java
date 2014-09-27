package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.Context;
import net.netnook.qpeg.impl.ParsingExpression;

public class ParseTree extends TreeNode {

	public ParseTree(Context context, ParsingExpression expression, int startPos, int endPos, List<Object> children) {
		super(context, expression, startPos, endPos, children);
	}
}
