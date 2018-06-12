package net.netnook.repeg.expressions;

public interface Visitor {

	void visit(CompoundExpression expression);

	void visit(SimpleExpression expression);

	void visit(ParsingRule expression);
}
