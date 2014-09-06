package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.OptionalNode;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.ZeroOrModeNode;

public class ZeroOrMore implements CompoundExpression {

	public static ZeroOrMore of(ParsingExpression expression) {
		return new ZeroOrMore(expression);
	}

	private final ParsingExpression expression;

	private ZeroOrMore(ParsingExpression expression) {
		this.expression = expression;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Collections.singletonList(expression);
	}

	@Override
	public String buildGrammar() {
		return "(" + expression.buildGrammar() + ")*";
	}

	@Override
	public ParseNode parse(Context context) {
		int startPosition = context.position();

		List<ParseNode> children = new ArrayList<>();

		while (true) {
			int fallbackPosition = context.position();
			ParseNode child = expression.parse(context);

			if (child == null) {
				context.setPosition(fallbackPosition);
				break;
			}

			if (child instanceof OptionalNode) {
				// FIXME: validate when loading rule
				throw new RuntimeException("OptionalNode returned in zero-or-more ... this will never end");
			}

			children.add(child);
		}

		return new ZeroOrModeNode(context, this, startPosition, context.position(), children);
	}
}
