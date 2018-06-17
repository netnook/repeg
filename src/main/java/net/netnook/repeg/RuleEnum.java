package net.netnook.repeg;

import net.netnook.repeg.expressions.core.Rule;

/**
 * Specialised {@link ParsingExpressionBuilder} used for defining expression builders using enums.
 */
public interface RuleEnum extends ParsingExpressionBuilder {

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
	 * Default implementation of {@link ParsingExpressionBuilder#build()}.
	 */
	@Override
	default Rule build() {
		return Rule.from(this).build();
	}
}
