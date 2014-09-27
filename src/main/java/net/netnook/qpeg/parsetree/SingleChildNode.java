package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.ParsingExpression;

public abstract class SingleChildNode extends ParseNode {

	protected final ParseNode child;

	public SingleChildNode(Context context, ParsingExpression expression, int startPos, int endPos, ParseNode child) {
		super(context, expression, startPos, endPos);
		this.child = child;
	}

	public ParseNode getChild() {
		return child;
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

		if (child != null) {
			child.dump(depth + 1);
		}
	}
}
