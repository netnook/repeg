package net.netnook.qpeg.impl;

public interface SimpleExpression extends ParsingExpression {

	default void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
