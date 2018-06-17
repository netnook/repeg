package net.netnook.repeg.expressions;

public interface SimpleExpression extends Expression {

	@Override
	default void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
