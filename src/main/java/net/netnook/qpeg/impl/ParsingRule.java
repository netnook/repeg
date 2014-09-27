package net.netnook.qpeg.impl;

import java.util.Comparator;

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
	public boolean parse(Context context) {
		return expression.parse(context);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public <T> T parse(CharSequence input) throws NoMatchException {
		Context context = new Context(input);
		Context.Marker start = context.mark();

		boolean success = parse(context);

		if (!success) {
			throw new NoMatchException();
		}

		context.mark(start);

		int count = context.size();

		if (count == 0) {
			return null;
		} else if (count == 1) {
			return context.get(0);
		} else {
			return (T) context.getAll();
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
