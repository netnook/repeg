package net.netnook.repeg.expressions;

import java.util.List;

import net.netnook.repeg.Expression;

public interface CompoundExpression extends Expression {

	List<Expression> parts();

	@Override
	default void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
