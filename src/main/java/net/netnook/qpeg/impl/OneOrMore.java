package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.OneOrMoreNode;
import net.netnook.qpeg.parsetree.ParseNode;

public class OneOrMore extends CompoundExpression {

	public static OneOrMoreBuilder of(ParsingExpressionBuilder expression) {
		return new OneOrMoreBuilder().expression(expression);
	}

	public static class OneOrMoreBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private Function<? super OneOrMoreNode, ParseNode> onSuccess = NO_OP;

		public OneOrMoreBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		public OneOrMoreBuilder onSuccess(Function<? super OneOrMoreNode, ParseNode> onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public OneOrMoreBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public OneOrMoreBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public OneOrMoreBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public OneOrMore build(BuildContext ctxt) {
			return new OneOrMore(expression.build(ctxt), onSuccess, ignore(), alias());
		}
	}

	private static final Function<? super OneOrMoreNode, ParseNode> NO_OP = r -> r;

	private final ParsingExpression expression;
	private final Function<? super OneOrMoreNode, ParseNode> onSuccess;

	private OneOrMore(ParsingExpression expression, Function<? super OneOrMoreNode, ParseNode> onSuccess, boolean ignore, String alias) {
		super(ignore, alias);
		this.expression = expression;
		this.onSuccess = onSuccess;

		if (expression.isIgnore()) {
			throw new IllegalArgumentException("Cannot have ignorable content in repeating construct.");
		}
		if (expression instanceof Optional) {
			throw new IllegalArgumentException("Cannot have optional content in repeating construct.");
		}
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

		int matchCount = 0;
		List<ParseNode> children = new ArrayList<>();

		while (true) {
			int fallbackPosition = context.position();
			ParseNode child = expression.parse(context);

			if (child == null) {
				context.setPosition(fallbackPosition);
				break;
			}

			matchCount++;

			if (!child.isIgnore()) {
				children.add(child);
			}
		}

		if (matchCount == 0) {
			// context.setPosition(startPosition);  Not necessary since will have been reset in while loop
			return null;
		}

		OneOrMoreNode result = new OneOrMoreNode(context, this, startPosition, context.position(), children);

		return onSuccess.apply(result);
	}

	//	public OneOrMore onSuccess(Function<? super OneOrMoreNode, ParseNode> onSuccess) {
	//		return new OneOrMore(expression, onSuccess, ignore, alias);
	//	}
	//
	//	@Override
	//	public ParsingExpression ignore() {
	//		return new OneOrMore(expression, onSuccess, true, alias);
	//	}
}
