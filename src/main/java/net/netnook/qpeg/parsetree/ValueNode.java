package net.netnook.qpeg.parsetree;

public class ValueNode extends ParseNode {

	public ValueNode(ParseNode node, Object value) {
		super(node.context, node.expression, node.startPos, node.endPos);
		setOutput(value);
	}
}
