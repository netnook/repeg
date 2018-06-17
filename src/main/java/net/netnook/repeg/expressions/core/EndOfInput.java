package net.netnook.repeg.expressions.core;

import net.netnook.repeg.expressions.ExpressionBase;
import net.netnook.repeg.expressions.RootContext;
import net.netnook.repeg.expressions.SimpleExpression;
import net.netnook.repeg.expressions.Visitor;

/**
 * An expression which matches when the end of the input has been reached.
 */
public final class EndOfInput extends ExpressionBase implements SimpleExpression {

	private static final EndOfInput EOI = new EndOfInput();

	public static EndOfInput instance() {
		return EOI;
	}

	private EndOfInput() {
		super(null);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String buildGrammar() {
		return "EOI";
	}

	@Override
	protected boolean parseImpl(RootContext context, int startPosition, int startStackIdx) {
		return context.charAt(startPosition) == RootContext.END_OF_INPUT;
	}
}
