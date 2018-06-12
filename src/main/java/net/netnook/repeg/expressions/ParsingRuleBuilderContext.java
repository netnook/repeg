package net.netnook.repeg.expressions;

import java.util.HashMap;
import java.util.Map;

final class ParsingRuleBuilderContext {

	private static final ThreadLocal<ParsingRuleBuilderContext> threadLocal = new ThreadLocal<>();

	static ParsingRuleBuilderContext create() {
		if (threadLocal.get() != null) {
			throw new IllegalStateException("Context already exists");
		}
		ParsingRuleBuilderContext ctxt = new ParsingRuleBuilderContext();
		threadLocal.set(ctxt);
		return ctxt;
	}

	static ParsingRuleBuilderContext get() {
		return threadLocal.get();
	}

	static void clear() {
		threadLocal.remove();
	}

	private ParsingRuleBuilderContext() {
		// defeat instantiation
	}

	private Map<ParsingRuleBuilder, ParsingRule> expressions = new HashMap<>();

	ParsingRule getRule(ParsingRuleBuilder key) {
		return expressions.get(key);
	}

	void putRule(ParsingRuleBuilder key, ParsingRule expression) {
		expressions.put(key, expression);
	}
}
