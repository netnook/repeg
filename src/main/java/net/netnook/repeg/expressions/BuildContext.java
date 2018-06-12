package net.netnook.repeg.expressions;

import java.util.HashMap;
import java.util.Map;

/**
 * Build context used by expressions builders during the build phase to handle cyclic dependencies amongst rules.
 */
public final class BuildContext {

	private static final ThreadLocal<BuildContext> threadLocal = ThreadLocal.withInitial(BuildContext::new);

	static BuildContext get() {
		return threadLocal.get();
	}

	public static void clear() {
		threadLocal.remove();
	}

	private BuildContext() {
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
