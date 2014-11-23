package net.netnook.qpeg.expressions;

// TODO: switch to interface ?
public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(ParsingExpressionBuilder builder) {
		super(builder);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
