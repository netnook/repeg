package net.netnook.qpeg.impl;

import java.util.Comparator;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;

public interface ParsingRuleBuilder extends ParsingExpressionBuilder {

	Comparator<? super ParsingRuleBuilder> SORT_BY_NAME_WITH_START_FIRST = new Comparator<ParsingRuleBuilder>() {
		@Override
		public int compare(ParsingRuleBuilder r1, ParsingRuleBuilder r2) {
			String name1 = r1.name();
			String name2 = r2.name();

			if ("START".equals(name1)) {
				return -1;
			} else if ("START".equals(name2)) {
				return 1;
			} else {
				return name1.compareTo(name2);
			}
		}
	};

	ParsingExpressionBuilder expression();

	@Override
	default ParsingExpression build(BuildContext ctxt) {
		ParsingExpression expression = ctxt.getExpression(this);
		if (expression != null) {
			return expression;
		} else {
			ReferenceExpression ref = new ReferenceExpression(name());
			ctxt.putExpression(this, ref);

			// FIXME: but the reference expression is then not immutable !!!
			ref.setReference(expression().build(ctxt));

			return ref;
		}
	}

	//	@Override
	//	default ParsingExpression internalGetExpression() {
	//		return expression();
	//	}
	//
	//	@Override
	//	default void accept(Visitor visitor) {
	//		visitor.visit(this);
	//	}

	String name();

	//	@Override
	//	default String buildGrammar() {
	//		return name();
	//	}

	//	default ParseTree parse(CharSequence input) throws NoMatchException {
	//		Context context = new Context(input);
	//		ParseNode root = expression().parse(context);
	//		if (root == null) {
	//			throw new NoMatchException();
	//		}
	//		return new ParseTree(root);
	//	}

	//	@Override
	//	default ParseNode parse(Context context) {
	//		int startIndex = context.position();
	//		ParseNode child = expression().parse(context);
	//		if (child == null) {
	//			return null;
	//		}
	//
	//		RuleNode result = new RuleNode(context, this, startIndex, context.position(), child);
	//
	//		result.setOutput(child.getOutput());
	//
	//		return result;
	//	}

	//	@Override
	//	default ParsingExpression ignore() {
	//		throw new UnsupportedOperationException();
	//	}

	//	@Override
	//	default boolean isIgnore() {
	//		return false;
	//	}
	//
	//	@Override
	//	default String alias() {
	//		return null;
	//	}
}
