package net.netnook.qpeg.expressions;

import java.util.Comparator;
import java.util.List;

import net.netnook.qpeg.util.ParseListener;

public final class ParsingRule extends ParsingExpressionBase {

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

	ParsingRule(ParsingRuleBuilder builder) {
		super(builder);
		this.name = builder.name();
	}

	@Override
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
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public <T> T parse(CharSequence input) throws NoMatchException {
		return parse(input, ParseListener.NO_OP);
	}

	public <T> T parse(CharSequence input, ParseListener listener) throws NoMatchException {
		RootContext context = new RootContext(input);
		context.setListener(listener);

		boolean success = parse(context);

		// FIXME: check that end of input was reached ?
		if (!success) {
			throw new NoMatchException();
		}

		List<Object> stack = context.getStack();

		if (stack.isEmpty()) {
			return null;
		} else if (stack.size() == 1) {
			return (T) stack.get(0);
		} else {
			return (T) stack;
		}
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		return expression.parse(context);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
