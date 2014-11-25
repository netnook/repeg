package net.netnook.qpeg.expressions;

public interface ParsingRuleBuilder extends ParsingExpressionBuilder {

	ParsingExpressionBuilder expression();

	String name();

	@Override
	default ParsingRule build() {
		BuildContext ctxt = BuildContext.get();

		ParsingRule expression = ctxt.getRule(this);
		if (expression != null) {
			return expression;
		} else {
			ParsingRule rule = new ParsingRule(this);
			ctxt.putRule(this, rule);

			rule.setExpression(expression().build());

			return rule;
		}
	}

	@Override
	default OnSuccessHandler getOnSuccess() {
		return OnSuccessHandler.NO_OP;
	}
}
