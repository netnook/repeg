package net.netnook.qpeg.impl;

import java.util.List;

public abstract class CompoundExpression extends ParsingExpressionBase {

	protected CompoundExpression(boolean ignore, String alias, OnSuccessHandler onSuccess) {
		super(ignore, alias, onSuccess);
	}

	public abstract List<ParsingExpression> parts();

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}