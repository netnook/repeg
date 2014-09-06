package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.parsetree.ChoiceNode;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;

public class Choice implements CompoundExpression {

	private static final Consumer<ChoiceNode> NO_OP = r -> {};

	public static Choice of(ParsingExpression... expressions) {
		return new Choice(expressions);
	}

	private final ParsingExpression[] expressions;
	private final Consumer<? super ChoiceNode> onSuccess;

	private Choice(ParsingExpression[] expressions) {
		this(expressions, NO_OP);
	}

	private Choice(ParsingExpression[] expressions, Consumer<? super ChoiceNode> onSuccess) {
		this.expressions = expressions;
		this.onSuccess = onSuccess;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Arrays.asList(expressions);
	}

	@Override
	public String buildGrammar() {
		return Stream.of(expressions) //
				.map(ParsingExpression::buildGrammar) //
				.collect(Collectors.joining(" | ", "(", ")"));
	}

	@Override
	public ParseNode parse(Context context) {
		int startPosition = context.position();
		ChoiceNode result = null;
		for (ParsingExpression expression : expressions) {
			ParseNode child = expression.parse(context);
			if (child != null) {
				result = new ChoiceNode(context, this, startPosition, context.position(), child);
				break;
			}
			context.setPosition(startPosition);
		}

		if (result != null) {
			onSuccess.accept(result);
		}

		return result;
	}

	public Choice onSuccess(Consumer<? super ChoiceNode> onSuccess) {
		return new Choice(expressions, onSuccess);
	}
}
