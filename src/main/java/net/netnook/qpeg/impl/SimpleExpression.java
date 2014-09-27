package net.netnook.qpeg.impl;

public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(boolean ignore, String alias, OnSuccessHandler onSuccess) {
		super(ignore, alias, onSuccess);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
