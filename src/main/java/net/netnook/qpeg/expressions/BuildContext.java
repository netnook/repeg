package net.netnook.qpeg.expressions;

import java.util.HashMap;
import java.util.Map;

public class BuildContext {

	private Map<ParsingRuleBuilder, ParsingRule> expressions = new HashMap<>();

	public ParsingRule getRule(ParsingRuleBuilder key) {
		return expressions.get(key);
	}

	public void putRule(ParsingRuleBuilder key, ParsingRule expression) {
		expressions.put(key, expression);
	}
}
