package net.netnook.qpeg.impl;

import java.util.function.Consumer;
import java.util.function.Function;

import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.OptionalNode;
import net.netnook.qpeg.parsetree.ParseNode;
import net.netnook.qpeg.parsetree.SequenceNode;
import net.netnook.qpeg.parsetree.ValueNode;

public abstract class ParserFactoryBase {

	public static final ParsingExpressionBuilderBase IGNORED_WS = Optional.of(CharMatcher.whitespace()).ignore(true);

	public static final Function<? super ParseNode, ParseNode> TEXT_TO_INTEGER = node -> {
		int value = Integer.parseInt("" + node.getText());
		node.setOutput(value);
		return node;
	};

	public static final Function<? super ParseNode, ParseNode> REPLACE_WITH_VALUE_NODE = node -> new ValueNode(node, node.getOutput());

	public static Consumer<OptionalNode> orElse(Object defaultValue) {
		return (OptionalNode optional) -> {
			optional.setOutput(optional.getChildValueOrElse(defaultValue));
		};
	}

	public static Function<SequenceNode, ParseNode> setToOutputOfChild(int childIdx) {
		return (SequenceNode sequence) -> {
			sequence.setOutput(sequence.child(childIdx).getOutput());
			return sequence;
		};
	}

	public static Function<SequenceNode, ParseNode> replaceWithValueFrom(int childIdx) {
		return (SequenceNode sequence) -> new ValueNode(sequence, sequence.child(childIdx).getOutput());
	}
}
