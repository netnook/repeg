package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.ParsingExpression;

public abstract class TreeNode extends ParseNode {

	private final List<ParseNode> children;

	//	public TreeNode(Context context, ParsingExpression expression, int startPos, int endPos, ParseNode... children) {
	//		this(context, expression, startPos, endPos, Arrays.asList(children));
	//	}

	public TreeNode(Context context, ParsingExpression expression, int startPos, int endPos, List<ParseNode> children) {
		super(context, expression, startPos, endPos);
		this.children = children;
	}

	//	public ParseNode(ParsingExpression expression) {
	//		this(expression, null, null);
	//	}

	//	public ParseNode(ParsingExpression expression, String string) {
	//		this(expression, string, null);
	//	}

	//	public ParseNode(Context context, ParsingExpression expression, int startPos, int endPos, ParseNode... children) {
	//		this(context, expression, startPos, endPos, children == null ? Collections.emptyList() : Arrays.asList(children));
	//	}

	//	public ParseNode(Context context, ParsingExpression expression, int startPos, int endPos, List<ParseNode> children) {
	//		this.context = context;
	//		this.expression = expression;
	//		this.startPos = startPos;
	//		this.endPos = endPos;
	//		this.children = children;
	//	}

	//	public void add(ParseNode child) {
	//		children.add(child);
	//	}

	public List<ParseNode> getChildren() {
		return children;
	}

	@Override
	public void dump(int depth) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			buf.append("  ");
		}

		buf.append(expression.name());
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
