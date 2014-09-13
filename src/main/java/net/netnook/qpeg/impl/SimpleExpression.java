package net.netnook.qpeg.impl;

public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(boolean ignore, String alias) {
		super(ignore, alias);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
