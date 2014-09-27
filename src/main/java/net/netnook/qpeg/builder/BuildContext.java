package net.netnook.qpeg.builder;

import java.util.HashMap;
import java.util.Map;

import net.netnook.qpeg.impl.ParsingRule;
import net.netnook.qpeg.impl.ParsingRuleBuilder;

public class BuildContext {

	private Map<ParsingRuleBuilder, ParsingRule> expressions = new HashMap<>();

	public ParsingRule getRule(ParsingRuleBuilder key) {
		return expressions.get(key);
	}

	public void putRule(ParsingRuleBuilder key, ParsingRule expression) {
		expressions.put(key, expression);
	}
}
