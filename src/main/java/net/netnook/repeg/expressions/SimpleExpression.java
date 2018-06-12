package net.netnook.repeg.expressions;

public interface SimpleExpression extends ParsingExpression {

	@Override
	default void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
