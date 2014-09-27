package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.ParsingExpression;

public abstract class TreeNode extends ParseNode {

	private final List<ParseNode> children;

	public TreeNode(Context context, ParsingExpression expression, int startPos, int endPos, List<ParseNode> children) {
		super(context, expression, startPos, endPos);
		this.children = children;
	}

	public List<ParseNode> getChildren() {
		return children;
	}

	public <T extends ParseNode> T child(int idx) {
		return (T) children.get(idx);
	}

	@Override
	public void dump(int depth) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			buf.append("  ");
		}

		buf.append(expression.getName());
		buf.append(": ");
		buf.append(expression.buildGrammar());
		buf.append(" matched: ");
		buf.append(context.getInput(startPos, endPos));

		System.out.println(buf.toString());

		if (children != null) {
			children.forEach(c -> c.dump(depth + 1));
		}
	}

}
