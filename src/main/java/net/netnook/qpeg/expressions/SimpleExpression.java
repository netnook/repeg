package net.netnook.qpeg.expressions;

public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(ParsingExpressionBuilder builder) {
		super(builder);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
