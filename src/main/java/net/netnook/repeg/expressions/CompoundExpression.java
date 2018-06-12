package net.netnook.repeg.expressions;

import java.util.List;

public interface CompoundExpression extends ParsingExpression {

	List<ParsingExpression> parts();

	@Override
	default void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
