package net.netnook.qpeg.expressions;

import java.util.HashMap;
import java.util.Map;

public final class BuildContext {

	private Map<ParsingRuleBuilder, ParsingRule> expressions = new HashMap<>();

	ParsingRule getRule(ParsingRuleBuilder key) {
		return expressions.get(key);
	}

	void putRule(ParsingRuleBuilder key, ParsingRule expression) {
		expressions.put(key, expression);
	}
}
