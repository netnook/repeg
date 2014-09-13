package net.netnook.qpeg.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.ChoiceNode;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;

public class Choice extends CompoundExpression {

	public static ChoiceBuilder of(ParsingExpressionBuilder... expressions) {
		return new ChoiceBuilder().expressions(expressions);
	}

	public static class ChoiceBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;
		private Consumer<? super ChoiceNode> onSuccess = DEFAULT_ON_SUCCESS;

		public ChoiceBuilder expressions(ParsingExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		public ChoiceBuilder onSuccess(Consumer<? super ChoiceNode> onSuccess) {
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

	private static final Consumer<ChoiceNode> DEFAULT_ON_SUCCESS = choice -> {
		choice.setOutput(choice.getChild().getOutput());
	};

	private final ParsingExpression[] expressions;
	private final Consumer<? super ChoiceNode> onSuccess;

	//	private Choice(ParsingExpression[] expressions) {
	//		this(expressions, DEFAULT_ON_SUCCESS, false, null);
	//	}

	private Choice(ParsingExpression[] expressions, Consumer<? super ChoiceNode> onSuccess, boolean ignore, String alias) {
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
				.collect(Collectors.joining(" | ", "(", ")"));
	}

	@Override
	public ParseNode parse(Context context) {
		int startPosition = context.position();

		ParseNode child = null;
		for (ParsingExpression expression : expressions) {
			child = expression.parse(context);
			if (child != null) {
				break;
			}
			context.setPosition(startPosition);
		}

		if (child == null) {
			return null;
		} else if (child.isIgnore()) {
			child = null;
		}

		ChoiceNode result = new ChoiceNode(context, this, startPosition, context.position(), child);

		onSuccess.accept(result);

		return result;
	}

	//	public Choice onSuccess(Consumer<? super ChoiceNode> onSuccess) {
	//		return new Choice(expressions, onSuccess, ignore, alias);
	//	}
	//
	//	@Override
	//	public Choice ignore() {
	//		return new Choice(expressions, onSuccess, true, alias);
	//	}
}
