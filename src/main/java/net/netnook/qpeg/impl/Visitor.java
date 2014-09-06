package net.netnook.qpeg.impl;

public interface Visitor {

	void visit(ParsingRule rule);

	void visit(CompoundExpression expression);

	void visit(SimpleExpression expression);
}
