package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.ParsingExpression;

public class TreeNode2 extends ParseNode2 {

	private final List<Object> children;

	public TreeNode2(Context context, ParsingExpression expression, int startPos, int endPos, List<Object> children) {
		super(context, expression, startPos, endPos);
		this.children = children;
	}

//	public void dump() {
//		int depth = 0;
//		for (Object)
//		root.dump(depth);
//	}

	public List<Object> getChildren() {
		return children;
	}
}
