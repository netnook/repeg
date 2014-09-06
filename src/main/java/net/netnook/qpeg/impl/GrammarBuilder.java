package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrammarBuilder {

	public static String buildGrammar(ParsingRule rule) {
		StringBuilder buf = new StringBuilder();

		RuleCollector ruleCollector = new RuleCollector();
		rule.accept(ruleCollector);

		List<ParsingRule> rules = ruleCollector.getRules();
		Collections.sort(rules, ParsingRule.SORT_BY_NAME_WITH_START_FIRST);

		ruleCollector.getRules().stream().forEach(r -> {
			buf.append(r.name()).append(" <- ");
			buf.append(r.expression().buildGrammar()).append("\n\n");
		});

		return buf.toString();
	}

	private static class RuleCollector implements Visitor {

		private final List<ParsingRule> rules = new ArrayList<>();

		@Override
		public void visit(ParsingRule rule) {
			if (rules.contains(rule)) {
				return;
			}

			rules.add(rule);

			ParsingExpression expression = rule.internalGetExpression();
			if (expression != this) {
				expression.accept(this);
			}
		}

		@Override
		public void visit(CompoundExpression expression) {
			expression.parts().forEach(p -> p.accept(this));
		}

		@Override
		public void visit(SimpleExpression expression) {
			// no-op
		}

		public List<ParsingRule> getRules() {
			return rules;
		}
	}

}
