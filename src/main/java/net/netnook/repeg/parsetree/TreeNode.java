package net.netnook.repeg.parsetree;

import java.util.List;

import net.netnook.repeg.Context;
import net.netnook.repeg.Expression;

public class TreeNode extends ParseNode {

	private final List<ParseNode> children;

	public TreeNode(Context context, Expression expression, List<ParseNode> children) {
		super(context, expression);
		this.children = children;
	}

	public List<ParseNode> getChildren() {
		return children;
	}

	@Override
	protected void dump(StringBuilder buf, String prefix) {
		buf.append("\n").append(prefix);
		buf.append("TreeNode{") //
				.append("expression=").append(expression.buildGrammar()) //
				.append(", startPos=").append(startPos) //
				.append(", endPos=").append(endPos) //
				.append(", text='").append(text).append("'"); //

		String childPrefix = prefix + "  ";
		for (ParseNode child : children) {
			child.dump(buf, childPrefix);
		}

		buf.append("\n").append(prefix).append("}");
	}
}
