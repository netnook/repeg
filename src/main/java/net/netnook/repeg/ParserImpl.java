package net.netnook.repeg;

import java.util.List;

import net.netnook.repeg.exceptions.NoMatchException;
import net.netnook.repeg.exceptions.ParseException;
import net.netnook.repeg.expressions.RootContext;

final class ParserImpl<T> implements Parser<T> {

	private final Expression expression;

	ParserImpl(Expression expression) {
		this.expression = expression;
	}

	@Override
	public Expression getExpression() {
		return expression;
	}

	@Override
	public T parse(CharSequence input, ParseListener listener) throws ParseException {
		RootContext context = new RootContext(input, listener);

		boolean success = expression.parse(context);

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
}
