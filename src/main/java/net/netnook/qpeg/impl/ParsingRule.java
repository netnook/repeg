package net.netnook.qpeg.impl;

import java.util.Comparator;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseTree2;

public class ParsingRule implements ParsingExpression {

	public static final Comparator<? super ParsingRule> SORT_BY_NAME_WITH_START_FIRST = (Comparator<ParsingRule>) (r1, r2) -> {
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

	private final String name;
	// FIXME: is there a way to make this final too ?
	private ParsingExpression expression;

	public ParsingRule(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ParsingExpression getExpression() {
		return expression;
	}

	public void setExpression(ParsingExpression expression) {
		this.expression = expression;
	}

	@Override
	public String buildGrammar() {
		return name;
	}

	@Override
	public String alias() {
		return null;
	}

	@Override
	public boolean parse(Context context) {
		return expression.parse(context);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public ParseTree2 parse(CharSequence input) throws NoMatchException {
		Context context = new Context(input);
		boolean match = parse(context);

		if (!match) {
			throw new NoMatchException();
		}

		return new ParseTree2(context, this, 0, context.position(), context.popTo(0));
		//		ParseNode root;
		//
		//		List<Object> children = context.popTo(0);
		//		if (children.size() > 1) {
		//			throw new IllegalStateException("More than one child for parseTree !!!");
		//		} else if (children.size() == 1) {
		//			root = children.get(0);
		//		} else {
		//			root = null;
		//		}
		//
		//		return new ParseTree(root);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
