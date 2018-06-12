package net.netnook.repeg.parsetree;

import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.RootContext;

public class LeafNode extends ParseNode {

	public LeafNode(RootContext context, ParsingExpression expression, int startPos, int endPos) {
		super(context, expression, startPos, endPos);
	}

	@Override
	protected void dump(StringBuilder buf, String prefix) {
		buf.append("\n").append(prefix);
		buf.append("LeafNode{") //
				.append("expression=").append(expression.buildGrammar()) //
				.append(", startPos=").append(startPos) //
				.append(", endPos=").append(endPos) //
				.append(", text='").append(context.getInput(startPos, endPos)).append("'") //
				.append("}");
	}
}
