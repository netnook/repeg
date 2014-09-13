package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.OptionalNode;
import net.netnook.qpeg.parsetree.ParseNode;

public class Optional extends CompoundExpression {

	public static OptionalBuilder of(ParsingExpressionBuilder expression) {
		return new OptionalBuilder().expression(expression);
	}

	public static class OptionalBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private Consumer<OptionalNode> onSuccess = NO_OP;

		public OptionalBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		public OptionalBuilder onSuccess(Consumer<OptionalNode> onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public OptionalBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public OptionalBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public OptionalBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public Optional build(BuildContext ctxt) {
			return new Optional(expression.build(ctxt), onSuccess, ignore(), alias());
		}
	}

	private static final Consumer<OptionalNode> NO_OP = r -> {
	};

	//	public static Optional of(ParsingExpression expression) {
	//		return new Optional(expression);
	//	}

	private final ParsingExpression expression;
	private final Consumer<? super OptionalNode> onSuccess;

	//	private Optional(ParsingExpression expression) {
	//		this(expression, NO_OP, false, null);
	//	}

	private Optional(ParsingExpression expression, Consumer<? super OptionalNode> onSuccess, boolean ignore, String alias) {
		super(ignore, alias);
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
		} else if (child.isIgnore()) {
			child = null;
		}

		OptionalNode result = new OptionalNode(context, this, startPosition, context.position(), child);
		onSuccess.accept(result);
		return result;
	}

	//	public Optional onSuccess(Consumer<? super OptionalNode> onSuccess) {
	//		return new Optional(expression, onSuccess, ignore, alias);
	//	}
	//
	//	public Optional ignore() {
	//		return new Optional(expression, onSuccess, true, alias);
	//	}
}
