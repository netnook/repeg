package net.netnook.repeg.expressions.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netnook.repeg.Expression;
import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.RuleEnum;
import net.netnook.repeg.expressions.CompoundExpression;
import net.netnook.repeg.expressions.ExpressionBase;
import net.netnook.repeg.expressions.RootContext;
import net.netnook.repeg.expressions.Visitor;

public final class Rule extends ExpressionBase implements CompoundExpression {

	/**
	 * Create a new {@link Rule} expression for the specified expression.
	 *
	 * @param ruleEnum the rule enum to use as key.
	 * @return the new {@link Rule} expression.
	 */
	public static Builder from(RuleEnum ruleEnum) {
		return new Builder(ruleEnum);
	}

	public final static class Builder implements ExpressionBuilder {

		private final RuleEnum ruleEnum;

		public Builder(RuleEnum ruleEnum) {
			this.ruleEnum = ruleEnum;
		}

		@Override
		public Rule build() {
			boolean iCreatedContext = false;
			BuildContext ctxt = BuildContext.get();

			if (ctxt == null) {
				ctxt = BuildContext.create();
				iCreatedContext = true;
			}

			Rule rule = ctxt.getRule(ruleEnum);
			if (rule != null) {
				if (iCreatedContext) {
					throw new IllegalStateException("Eh ? Cannot already have rule if I created context.");
				}
			} else {
				rule = new Rule(ruleEnum.name());
				ctxt.putRule(ruleEnum, rule);
				rule.setExpression(ruleEnum.expression().build());
			}

			if (iCreatedContext) {
				BuildContext.clear();
			}

			return rule;
		}
	}

	private static final class BuildContext {

		private static final ThreadLocal<BuildContext> threadLocal = new ThreadLocal<>();

		static BuildContext create() {
			if (threadLocal.get() != null) {
				throw new IllegalStateException("Context already exists");
			}
			BuildContext ctxt = new BuildContext();
			threadLocal.set(ctxt);
			return ctxt;
		}

		static BuildContext get() {
			return threadLocal.get();
		}

		static void clear() {
			threadLocal.remove();
		}

		private BuildContext() {
			// defeat instantiation
		}

		private Map<RuleEnum, Rule> expressions = new HashMap<>();

		Rule getRule(RuleEnum key) {
			return expressions.get(key);
		}

		void putRule(RuleEnum key, Rule expression) {
			expressions.put(key, expression);
		}
	}

	private final String name;
	// TODO: is there a way to make this final too ?
	private Expression expression;

	Rule(String name) {
		super(null);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public String buildGrammar() {
		return name;
	}

	@Override
	protected boolean doParse(RootContext context, int startPosition, int startStackIdx) {
		return expression.parse(context);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<Expression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
