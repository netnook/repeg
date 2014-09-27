package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilder;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;

public abstract class ParserFactoryBase {

	public ParsingRule build() {
		return getStartRule().build(new BuildContext());
	}

	protected abstract ParsingRuleBuilder getStartRule();

	protected static Constant.EoiBuilder endOfInput() {
		return Constant.EOI_BUILDER;
	}

	protected static Sequence.SequenceBuilder sequence(ParsingExpressionBuilder... expressions) {
		return Sequence.of(expressions);
	}

	protected static Choice.ChoiceBuilder choice(ParsingExpressionBuilder... expressions) {
		return Choice.of(expressions);
	}

	protected static Repetition.RepetitionBuilder zeroOrMore(ParsingExpressionBuilder expression) {
		return Repetition.zeroOrMore(expression);
	}

	protected static Repetition.RepetitionBuilder oneOrMore(ParsingExpressionBuilder expression) {
		return Repetition.oneOrMore(expression);
	}

	protected static Optional.OptionalBuilder optional(ParsingExpressionBuilder expression) {
		return Optional.of(expression);
	}

	protected static CharMatcher.CharRangeMatcherBuilder characterInRange(char from, char to) {
		return CharMatcher.inRange(from, to);
	}

	protected static CharMatcher.SingleCharMatcherBuilder character(char c) {
		return CharMatcher.of(c);
	}

	protected static CharMatcher.OneOfCharMatcherBuilder oneOf(String characters) {
		return CharMatcher.oneOf(characters);
	}

	protected static ParsingExpressionBuilder ignoredWhitespace() {
		return IGNORED_WS;
	}

	private static final ParsingExpressionBuilderBase IGNORED_WS = Optional.of(CharSequenceMatcher.whitespace().ignore()).ignore();

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
