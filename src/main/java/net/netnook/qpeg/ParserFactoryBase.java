package net.netnook.qpeg;

import net.netnook.qpeg.expressions.BuildContext;
import net.netnook.qpeg.expressions.CharMatcher;
import net.netnook.qpeg.expressions.Choice;
import net.netnook.qpeg.expressions.Constant;
import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.Optional;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.ParsingRule;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;
import net.netnook.qpeg.expressions.Repetition;
import net.netnook.qpeg.expressions.Sequence;

public abstract class ParserFactoryBase {

	private static final ParsingExpressionBuilderBase IGNORED_WS = CharMatcher.whitespace().minCount(0).maxCountUnbounded().ignore();

	public ParsingRule build() {
		return getStartRule().build(new BuildContext());
	}

	protected abstract ParsingRuleBuilder getStartRule();

	protected static Constant.EoiBuilder endOfInput() {
		return Constant.EOI_BUILDER;
	}

	protected static Sequence.Builder sequence(ParsingExpressionBuilder... expressions) {
		return Sequence.of(expressions);
	}

	protected static Choice.Builder choice(ParsingExpressionBuilder... expressions) {
		return Choice.of(expressions);
	}

	protected static Repetition.Builder zeroOrMore(ParsingExpressionBuilder expression) {
		return Repetition.zeroOrMore(expression);
	}

	protected static Repetition.Builder oneOrMore(ParsingExpressionBuilder expression) {
		return Repetition.oneOrMore(expression);
	}

	protected static Optional.Builder optional(ParsingExpressionBuilder expression) {
		return Optional.of(expression);
	}

	protected static CharMatcher.Builder characterInRange(char from, char to) {
		return CharMatcher.inRange(from, to);
	}

	protected static CharMatcher.Builder character(char c) {
		return CharMatcher.of(c);
	}

	protected static CharMatcher.Builder oneOf(String characters) {
		return CharMatcher.oneOf(characters);
	}

	protected static ParsingExpressionBuilder ignoredWhitespace() {
		return IGNORED_WS;
	}

	public static final OnSuccessHandler TEXT_TO_INTEGER = (context) -> {
		context.clear();
		String text = context.getCurrentText().toString();
		int value = Integer.parseInt(text);
		context.push(value);
	};

	public static OnSuccessHandler orElse(Object defaultValue) {
		return (Context context) -> {
			int len = context.size();
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

	public static OnSuccessHandler replaceWithValueFrom(int childIdx) {
		return (context) -> {
			Object o = context.get(childIdx);
			context.clear();
			context.push(o);
		};
	}

}
