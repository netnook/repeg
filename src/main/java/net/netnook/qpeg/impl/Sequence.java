package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.SequenceNode;

public class Sequence implements CompoundExpression {

	private static final Consumer<SequenceNode> NO_OP = r -> {
	};

	public static Sequence of(ParsingExpression... expressions) {
		return new Sequence(expressions);
	}

	private final ParsingExpression[] expressions;
	private final Consumer<? super SequenceNode> onSuccess;

	private Sequence(ParsingExpression[] expressions) {
		this(expressions, NO_OP);
	}

	private Sequence(ParsingExpression[] expressions, Consumer<? super SequenceNode> onSuccess) {
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
				.collect(Collectors.joining(" ", "(", ")"));
	}

	@Override
	public ParseNode parse(Context context) {
		int startPosition = context.position();

		List<ParseNode> children = new ArrayList<>();

		for (ParsingExpression expression : expressions) {
			ParseNode child = expression.parse(context);
			if (child == null) {
				context.setPosition(startPosition);
				return null;
			}

			//			if (child instanceof OptionalNode) {
			//				throw new RuntimeException("OptionalNode returned in sequence ... this will never end");
			//			}
			children.add(child);
		}

		SequenceNode result = new SequenceNode(context, this, startPosition, context.position(), children);

		onSuccess.accept(result);

		return result;
	}

	public Sequence onSuccess(Consumer<SequenceNode> onSuccess) {
		return new Sequence(expressions, onSuccess);
	}
}
