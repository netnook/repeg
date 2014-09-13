package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;

public class ReferenceExpression implements ParsingExpression {

	private final String name;
	private ParsingExpression expression;

	public ReferenceExpression(String name) {
		this.name = name;
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
	public ParseNode parse(Context context) {
		return expression.parse(context);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public void setReference(ParsingExpression expression) {
		this.expression = expression;
	}
}
