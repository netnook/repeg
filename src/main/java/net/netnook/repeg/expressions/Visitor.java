package net.netnook.repeg.expressions;

import net.netnook.repeg.expressions.core.Rule;

public interface Visitor {

	void visit(Rule expression);

	void visit(CompoundExpression expression);

	void visit(SimpleExpression expression);

}
