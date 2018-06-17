package net.netnook.repeg;

import net.netnook.repeg.exceptions.ParseException;
import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.util.ParseListener;

/**
 * Parser interface.  Parsers are thread safe and can (should) be re-used when possible.
 */
public interface Parser<T> {

	default T parse(CharSequence input) throws ParseException {
		return parse(input, ParseListener.NO_OP);
	}

	T parse(CharSequence input, ParseListener listener) throws ParseException;

	/**
	 * Get start expression for this parser
	 *
	 * @return start expression.
	 */
	Expression getExpression();
}
