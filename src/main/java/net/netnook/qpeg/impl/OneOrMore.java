package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.OneOrMoreNode;
import net.netnook.qpeg.parsetree.OptionalNode;
import net.netnook.qpeg.parsetree.ParseNode;

public class OneOrMore implements CompoundExpression {

	private static final Consumer<? super OneOrMoreNode> NO_OP = r -> {
	};

	public static OneOrMore of(ParsingExpression expression) {
		return new OneOrMore(expression);
	}

	private final ParsingExpression expression;
	private final Consumer<? super OneOrMoreNode> onSuccess;

	private OneOrMore(ParsingExpression expression) {
		this(expression, NO_OP);
	}

	private OneOrMore(ParsingExpression expression, Consumer<? super OneOrMoreNode> onSuccess) {
		this.expression = expression;
		this.onSuccess = onSuccess;
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

		if (children.isEmpty()) {
			// context.setPosition(startPosition);  Not necessary since will have been reset in while loop
			return null;
		}

		OneOrMoreNode result = new OneOrMoreNode(context, this, startPosition, context.position(), children);

		onSuccess.accept(result);

		return result;
	}

	public OneOrMore onSuccess(Consumer<? super OneOrMoreNode> onSuccess) {
		return new OneOrMore(expression, onSuccess);
	}
}
