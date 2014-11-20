package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.ParsingExpression;

public class TreeNode extends ParseNode {

	private final List<ParseNode> children;

	public TreeNode(RootContext context, ParsingExpression expression, int startPos, int endPos, List<ParseNode> children) {
		super(context, expression, startPos, endPos);
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
				.append(", text='").append(context.getInput(startPos, endPos)).append("'"); //

		String childPrefix = prefix + "  ";
		for (ParseNode child : children) {
			child.dump(buf, childPrefix);
		}

		buf.append("\n").append(prefix).append("}");
	}
}
