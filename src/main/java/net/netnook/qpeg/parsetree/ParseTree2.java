package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.ParsingExpression;

public class ParseTree2 extends TreeNode2 {

	public ParseTree2(Context context, ParsingExpression expression, int startPos, int endPos, List<Object> children) {
		super(context, expression, startPos, endPos, children);
	}
}