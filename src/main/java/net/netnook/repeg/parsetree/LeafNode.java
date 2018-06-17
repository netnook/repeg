package net.netnook.repeg.parsetree;

import net.netnook.repeg.Context;
import net.netnook.repeg.Expression;

public class LeafNode extends ParseNode {

	public LeafNode(Context context, Expression expression) {
		super(context, expression);
	}

	@Override
	protected void dump(StringBuilder buf, String prefix) {
		buf.append("\n").append(prefix);
		buf.append("LeafNode{") //
				.append("expression=").append(expression.buildGrammar()) //
				.append(", startPos=").append(startPos) //
				.append(", endPos=").append(endPos) //
				.append(", text='").append(text).append("'") //
				.append("}");
	}
}
