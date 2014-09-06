package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.OptionalNode;
import net.netnook.qpeg.parsetree.ParseNode;

public class Optional implements CompoundExpression {

	private static final Consumer<OptionalNode> NO_OP = r -> {};

	public static Optional of(ParsingExpression expression) {
		return new Optional(expression);
	}

	private final ParsingExpression expression;
	private final Consumer<? super OptionalNode> onSuccess;

	private Optional(ParsingExpression expression) {
		this(expression, NO_OP);
	}

	private Optional(ParsingExpression expression, Consumer<? super OptionalNode> onSuccess) {
		this.expression = expression;
		this.onSuccess = onSuccess;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Arrays.asList(expression);
	}

	@Override
	public String buildGrammar() {
		return "(" + expression.buildGrammar() + ")?";
	}

	@Override
	public ParseNode parse(Context context) {
		int startPosition = context.position();

		ParseNode child = expression.parse(context);
		if (child == null) {
			context.setPosition(startPosition);
		}

		OptionalNode result = new OptionalNode(context, this, startPosition, context.position(), child);
		onSuccess.accept(result);
		return result;
	}

	public Optional onSuccess(Consumer<? super OptionalNode> onSuccess) {
		return new Optional(expression, onSuccess);
	}
}
