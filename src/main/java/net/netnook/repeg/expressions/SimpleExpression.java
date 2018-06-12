package net.netnook.repeg.expressions;

// TODO: switch to interface ?
public abstract class SimpleExpression extends ParsingExpressionBase {

	protected SimpleExpression(OnSuccessHandler onSuccess) {
		super(onSuccess);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
