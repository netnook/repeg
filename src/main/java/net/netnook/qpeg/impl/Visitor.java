package net.netnook.qpeg.impl;

public interface Visitor {

	void visit(CompoundExpression expression);

	void visit(SimpleExpression expression);

	void visit(ParsingRule expression);
}