package net.netnook.qpeg.expressions;

public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(ParsingExpressionBuilderBase builder) {
		super(builder);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
