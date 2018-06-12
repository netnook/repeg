package net.netnook.repeg.expressions;

public interface Visitor {

	void visit(ParsingRule expression);

	void visit(CompoundExpression expression);

	void visit(SimpleExpression expression);

}
