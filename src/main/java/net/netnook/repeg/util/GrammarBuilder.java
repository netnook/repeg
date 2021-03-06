package net.netnook.repeg.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.netnook.repeg.Parser;
import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.SimpleExpression;
import net.netnook.repeg.expressions.Visitor;
import net.netnook.repeg.expressions.core.Rule;

public class GrammarBuilder {

	private static final Comparator<? super Rule> SORT_BY_NAME_WITH_START_FIRST = (Comparator<Rule>) (r1, r2) -> {
		String name1 = r1.getName();
		String name2 = r2.getName();

		if ("START".equals(name1)) {
			return -1;
		} else if ("START".equals(name2)) {
			return 1;
		} else {
			return name1.compareTo(name2);
		}
	};

	public static String buildGrammar(Parser parser) {
		StringBuilder buf = new StringBuilder();

		RuleCollector ruleCollector = new RuleCollector();
		parser.getExpression().accept(ruleCollector);

		List<Rule> rules = ruleCollector.getRules();
		Collections.sort(rules, SORT_BY_NAME_WITH_START_FIRST);

		ruleCollector.getRules().stream().forEach(r -> {
			buf.append(r.getName()).append(" <- ");
			buf.append(r.getExpression().buildGrammar()).append("\n\n");
		});

		return buf.toString();
	}

	private static class RuleCollector implements Visitor {

		private final List<Rule> rules = new ArrayList<>();

		@Override
		public void visit(Rule rule) {
			if (rules.contains(rule)) {
				return;
			}

			rules.add(rule);

			rule.getExpression().accept(this);
		}

		@Override
		public void visit(CompoundExpression expression) {
			expression.parts().forEach(p -> p.accept(this));
		}

		@Override
		public void visit(SimpleExpression expression) {
			// no-op
		}

		public List<Rule> getRules() {
			return rules;
		}
	}

}
