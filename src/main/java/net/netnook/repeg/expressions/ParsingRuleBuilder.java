package net.netnook.repeg.expressions;

/**
 * Specialised {@link ParsingExpressionBuilder} used for defining expression builders using enums.
 */
public interface ParsingRuleBuilder extends ParsingExpressionBuilder {

	/**
	 * Get a name for this rule from the enum's name
	 *
	 * @return rule name
	 */
	String name();

	/**
	 * Get the expression builder used to create the expression for this instance.
	 *
	 * @return expression builder.
	 */
	ParsingExpressionBuilder expression();

	/**
	 * Default {@link ParsingExpressionBuilder#build()} implementation.
	 * <p>
	 * Note: Due to the possibility of cyclic dependencies, this method is not guaranteed to return a fully build {@link ParsingRule} when called.  In order
	 * to handle such cycles, this method internally makes use of a {@link ThreadLocal} context to keep track of which rules have already
	 * been processed.  When a rule builder's build method is re-entered, an 'empty' Rule is returned which is populated later.  A {@link ParsingRule} is guaranteed
	 * to be filled in and ready by the time the consumer's call to the first {@link ParsingRule#build()} method returns.
	 *
	 * @return new {@link ParsingRule} rule expression.
	 */
	@Override
	default ParsingRule build() {
		boolean iCreatedContext = false;
		ParsingRuleBuilderContext ctxt = ParsingRuleBuilderContext.get();

		if (ctxt == null) {
			ctxt = ParsingRuleBuilderContext.create();
			iCreatedContext = true;
		}

		ParsingRule rule = ctxt.getRule(this);
		if (rule != null) {
			if (iCreatedContext) {
				throw new IllegalStateException("Eh ? Cannot already have rule if I created context.");
			}
		} else {
			rule = new ParsingRule(this);
			ctxt.putRule(this, rule);
			rule.setExpression(expression().build());
		}

		if (iCreatedContext) {
			ParsingRuleBuilderContext.clear();
		}

		return rule;
	}
}
