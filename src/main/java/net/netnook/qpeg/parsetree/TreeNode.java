package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.ParsingExpression;

public class TreeNode extends ParseNode {

	private final List<Object> children;

	public TreeNode(Context context, ParsingExpression expression, int startPos, int endPos, List<Object> children) {
		super(context, expression, startPos, endPos);
		this.children = children;
	}

	public List<Object> getChildren() {
		return children;
	}
}
