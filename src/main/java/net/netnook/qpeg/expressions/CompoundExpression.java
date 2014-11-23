package net.netnook.qpeg.expressions;

import java.util.List;

// TODO switch to interface ??
public abstract class CompoundExpression extends ParsingExpressionBase {

	protected CompoundExpression(ParsingExpressionBuilder builder) {
		super(builder);
	}

	public abstract List<ParsingExpression> parts();

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
