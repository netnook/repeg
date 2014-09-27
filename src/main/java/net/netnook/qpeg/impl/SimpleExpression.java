package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;

public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(ParsingExpressionBuilderBase builder) {
		super(builder);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
