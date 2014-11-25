package net.netnook.qpeg.expressions.core;

import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.expressions.SimpleExpression;
import net.netnook.qpeg.expressions.Visitor;

public final class EndOfInput extends SimpleExpression {

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
