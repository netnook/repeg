package net.netnook.qpeg.impl;

import java.util.List;

public interface CompoundExpression extends ParsingExpression {

	List<ParsingExpression> parts();

	default void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
