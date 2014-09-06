package net.netnook.qpeg.impl;

import java.util.Comparator;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.ParseTree;
import net.netnook.qpeg.parsetree.RuleNode;

public interface ParsingRule extends ParsingExpression {

	Comparator<? super ParsingRule> SORT_BY_NAME_WITH_START_FIRST = new Comparator<ParsingRule>() {
		@Override
		public int compare(ParsingRule r1, ParsingRule r2) {
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

	ParsingExpression expression();

	@Override
	default ParsingExpression internalGetExpression() {
		return expression();
	}

	@Override
	default void accept(Visitor visitor) {
		visitor.visit(this);
	}

	String name();

	@Override
	default String buildGrammar() {
		return name();
	}

	default ParseTree parse(CharSequence input) throws NoMatchException {
		Context context = new Context(input);
		ParseNode root = expression().parse(context);
		if (root == null) {
			throw new NoMatchException();
		}
		return new ParseTree(root);
	}

	@Override
	default ParseNode parse(Context context) {
		int startIndex = context.position();
		ParseNode child = expression().parse(context);
		if (child == null) {
			return null;
		}

		RuleNode result = new RuleNode(context, this, startIndex, context.position(), child);

		result.setOutput(child.getOutput());

		return result;
	}
}
