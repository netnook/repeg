package net.netnook.repeg.expressions;

import java.util.Collections;
import java.util.List;

import net.netnook.repeg.util.ParseListener;

public final class ParsingRule extends ParsingExpressionBase implements CompoundExpression {

	private final String name;
	// TODO: is there a way to make this final too ?
	private ParsingExpression expression;

	ParsingRule(ParsingRuleBuilder builder) {
		super(null);
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

	public <T> T parse(CharSequence input) throws ParseException {
		return parse(input, ParseListener.NO_OP);
	}

	public <T> T parse(CharSequence input, ParseListener listener) throws ParseException {
		RootContext context = new RootContext(input, listener);

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
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public List<ParsingExpression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
