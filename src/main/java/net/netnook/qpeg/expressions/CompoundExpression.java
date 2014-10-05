package net.netnook.qpeg.expressions;

import java.util.List;

public abstract class CompoundExpression extends ParsingExpressionBase {

	protected CompoundExpression(ParsingExpressionBuilderBase builder) {
		super(builder);
	}

	public abstract List<ParsingExpression> parts();

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
