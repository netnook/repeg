package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.SequenceNode;

public class Sequence extends CompoundExpression {

	public static SequenceBuilder of(ParsingExpressionBuilder... expressions) {
		return new SequenceBuilder().expressions(expressions);
	}

	public static class SequenceBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;
		private Function<? super SequenceNode, ParseNode> onSuccess = NO_OP;

		public SequenceBuilder expressions(ParsingExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		public SequenceBuilder onSuccess(Function<? super SequenceNode, ParseNode> onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public SequenceBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public SequenceBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public SequenceBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public Sequence build(BuildContext ctxt) {
			return new Sequence(build(ctxt, expressions), onSuccess, ignore(), alias());
		}
	}

	private static final Function<? super SequenceNode, ParseNode> NO_OP = r -> r;

	//	public static Sequence of(ParsingExpression... expressions) {
	//		return new Sequence(expressions);
	//	}

	private final ParsingExpression[] expressions;
	private final Function<? super SequenceNode, ParseNode> onSuccess;

	//	private Sequence(ParsingExpression[] expressions) {
	//		this(expressions, NO_OP, false, null);
	//	}

	private Sequence(ParsingExpression[] expressions, Function<? super SequenceNode, ParseNode> onSuccess, boolean ignore, String alias) {
		super(ignore, alias);
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
			if (!child.isIgnore()) {
				children.add(child);
			}
		}

		SequenceNode result = new SequenceNode(context, this, startPosition, context.position(), children);

		return onSuccess.apply(result);
	}

	//	public Sequence onSuccess(Function<? super SequenceNode, ParseNode> onSuccess) {
	//		return new Sequence(expressions, onSuccess, ignore, alias);
	//	}
	//
	//	@Override
	//	public Sequence ignore() {
	//		return new Sequence(expressions, onSuccess, true, alias);
	//	}
}
