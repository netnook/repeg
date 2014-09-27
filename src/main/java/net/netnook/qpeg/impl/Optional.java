package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;

public class Optional extends CompoundExpression {

	public static OptionalBuilder of(ParsingExpressionBuilder expression) {
		return new OptionalBuilder().expression(expression);
	}

	public static class OptionalBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder expression;
		private OnSuccessHandler onSuccess = OnSuccessHandler.NO_OP;

		public OptionalBuilder expression(ParsingExpressionBuilder expression) {
			this.expression = expression;
			return this;
		}

		public OptionalBuilder onSuccess(OnSuccessHandler onSuccess) {
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

	//	private static final Consumer<OptionalNode> NO_OP = r -> {
	//	};

	private final ParsingExpression expression;

	private Optional(ParsingExpression expression, OnSuccessHandler onSuccess, boolean ignore, String alias) {
		super(ignore, alias, onSuccess);
		this.expression = expression;
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
	public boolean parse(Context context) {
		Context.Marker marker = context.marker();

		boolean match = expression.parse(context);
		if (!match) {
			context.reset(marker);
		}

		onSuccess.accept(context, marker);
		//		List<ParseNode> children = context.popTo(stackPosition);
		//		if (!isIgnore()) {
		//			if (children.size() > 1) {
		//				throw new IllegalStateException("More than one child for optional node !!!");
		//			} else if (children.size() == 1) {
		//				context.push(new OptionalNode(context, this, startPosition, context.position(), children.get(0)));
		//			} else {
		//				context.push(new OptionalNode(context, this, startPosition, context.position(), null));
		//			}
		//		}

		// onSuccess.accept(result); FIXME: handle onSuccess
		return true;
	}
}
