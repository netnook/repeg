package net.netnook.qpeg.expressions;

public interface ParsingRuleBuilder extends ParsingExpressionBuilder {

	ParsingExpressionBuilder expression();

	String name();

	@Override
	default ParsingRule build(BuildContext ctxt) {
		ParsingRule expression = ctxt.getRule(this);
		if (expression != null) {
			return expression;
		} else {
			ParsingRule rule = new ParsingRule(name());
			ctxt.putRule(this, rule);

			// FIXME: but the reference expression is then not immutable !!!
			rule.setExpression(expression().build(ctxt));

			return rule;
		}
	}
}
