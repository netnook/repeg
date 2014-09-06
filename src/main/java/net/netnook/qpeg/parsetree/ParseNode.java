package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.ParsingExpression;

public abstract class ParseNode {

	protected final Context context;
	protected final ParsingExpression expression;
	protected final int startPos;
	protected final int endPos;
	private Object output;

	public ParseNode(Context context, ParsingExpression expression, int startPos, int endPos) {
		this.context = context;
		this.expression = expression;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public void dump(int depth) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			buf.append("  ");
		}

		buf.append(expression.name());
		buf.append(": ");
		buf.append(expression.buildGrammar());
		buf.append(" matched: ");
		buf.append(getText());

		System.out.println(buf.toString());
	}

	public Object getOutput() {
		return output;
	}

	public void setOutput(Object output) {
		this.output = output;
	}

	public CharSequence getText() {
		return context.getInput(startPos, endPos);
	}
}
