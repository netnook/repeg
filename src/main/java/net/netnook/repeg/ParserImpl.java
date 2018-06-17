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

		if (!success) {
			throw new NoMatchException("Input does not match start expression.");
		}

		if (context.position() < input.length()) {
			throw new NoMatchException("Trailing characters after " + context.position());
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
