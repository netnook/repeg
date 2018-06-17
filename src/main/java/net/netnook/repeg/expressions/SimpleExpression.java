package net.netnook.repeg.expressions;

import net.netnook.repeg.Expression;

public interface SimpleExpression extends Expression {

	@Override
	default void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
