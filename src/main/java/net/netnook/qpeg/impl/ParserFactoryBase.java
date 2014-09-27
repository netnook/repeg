package net.netnook.qpeg.impl;

import java.util.List;
import java.util.function.Function;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.ValueNode;

public abstract class ParserFactoryBase {

	public ParsingRule build() {
		return getStartRule().build(new BuildContext());
	}

	protected abstract ParsingRuleBuilder getStartRule();

	public static final ParsingExpressionBuilderBase IGNORED_WS = Optional.of(CharMatcher.whitespace()).ignore(true);

	public static final OnSuccessHandler TEXT_TO_INTEGER = (context, start) -> {
		context.resetStack(start.stackPosition);
		String text = context.getInput(start.inputPosition, context.position()).toString();
		int value = Integer.parseInt(text);
		context.push(value);
	};

	public static final Function<? super ParseNode, ParseNode> REPLACE_WITH_VALUE_NODE = node -> new ValueNode(node, node.getOutput());

	public static OnSuccessHandler orElse(Object defaultValue) {
		return (Context context, Context.Marker start) -> {
			int len = context.stackPosition() - start.stackPosition;
			Object value;
			if (len == 0) {
				value = defaultValue;
			} else if (len == 1) {
				value = context.pop();
			} else {
				throw new IllegalStateException("Expected 0 or 1 elements on stack.  Found " + len);
			}
			context.push(value);
		};
	}

	public static OnSuccessHandler setToOutputOfChild(int childIdx) {
		return (Context context, Context.Marker start) -> {
			List<Object> children = context.popTo(start.stackPosition);
			context.push(children.get(childIdx));
		};
	}

	public static OnSuccessHandler replaceWithValueFrom(int childIdx) {
		return (context, start) -> {
			List<Object> results = context.popTo(start.stackPosition);
			context.push(results.get(childIdx));
		};
	}

}
