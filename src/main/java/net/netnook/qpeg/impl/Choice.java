package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;

public class Choice extends CompoundExpression {

	public static ChoiceBuilder of(ParsingExpressionBuilder... expressions) {
		return new ChoiceBuilder().expressions(expressions);
	}

	public static class ChoiceBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;
		private OnSuccessHandler onSuccess = OnSuccessHandler.NO_OP;

		public ChoiceBuilder expressions(ParsingExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		public ChoiceBuilder onSuccess(OnSuccessHandler onSuccess) {
			this.onSuccess = onSuccess;
			return this;
		}

		@Override
		public ChoiceBuilder name(String name) {
			super.name(name);
			return this;
		}

		@Override
		public ChoiceBuilder ignore(boolean ignore) {
			super.ignore(ignore);
			return this;
		}

		@Override
		public ChoiceBuilder alias(String alias) {
			super.alias(alias);
			return this;
		}

		@Override
		public Choice build(BuildContext ctxt) {
			return new Choice(build(ctxt, expressions), onSuccess, ignore(), alias());
		}
	}

//	private static final Consumer<ChoiceNode> DEFAULT_ON_SUCCESS = choice -> {
//		choice.setOutput(choice.getChild().getOutput());
//	};

	private final ParsingExpression[] expressions;

	private Choice(ParsingExpression[] expressions, OnSuccessHandler onSuccess, boolean ignore, String alias) {
		super(ignore, alias, onSuccess);
		this.expressions = expressions;
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
	public boolean parse(Context context) {
		Context.Marker marker = context.marker();

		//ParseNode child = null;
		boolean match = false;
		for (ParsingExpression expression : expressions) {
			match = match || expression.parse(context);
			if (match) {
				break;
			}
			context.reset(marker);
		}

		if (!match) {
			// Note: not necessary as done as part of loop above
			// context.setPosition(stackPosition);
			// context.resetStack(stackPosition);
			return false;
		}

//		List<Object> children = context.popTo(stackPosition);
		onSuccess.accept(context, marker);

//		context.resetStack(stackPosition);
//		if (!isIgnore()) {
//			onSuccess
//			context.push(new TreeNode2(context, this, startPosition, context.position(), children));
////			if (children.size() > 1) {
////				throw new IllegalStateException("More than one child for choice node !!!");
////			} else if (children.size() == 1) {
////				context.push(new TreeNode2(context, this, startPosition, context.position(), children.get(0)));
////			} else {
////				context.push(new ChoiceNode(context, this, startPosition, context.position(), null));
////			}
//		}

		// FIXME: todo
		//onSuccess.accept(result);

		return true;
	}
}
