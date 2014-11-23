package net.netnook.qpeg;

import net.netnook.qpeg.expressions.BuildContext;
import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingExpressionBuilderBase;
import net.netnook.qpeg.expressions.ParsingRule;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;
import net.netnook.qpeg.expressions.chars.CharTesters;
import net.netnook.qpeg.expressions.core.CharMatcher;
import net.netnook.qpeg.expressions.core.Choice;
import net.netnook.qpeg.expressions.core.EoiMatcher;
import net.netnook.qpeg.expressions.core.Optional;
import net.netnook.qpeg.expressions.core.Predicate;
import net.netnook.qpeg.expressions.core.Repetition;
import net.netnook.qpeg.expressions.core.Sequence;
import net.netnook.qpeg.expressions.core.StringMatcher;
import net.netnook.qpeg.expressions.extras.NewlineMatcher;

public abstract class ParserFactoryBase {

	private static final ParsingExpressionBuilderBase IGNORED_WS = CharMatcher.whitespace().minCount(0).maxUnbounded().ignore();

	private static final OnSuccessHandler TEXT_TO_INTEGER = (context) -> {
		context.clearStack();
		String text = context.getCharSequence().toString();
		int value = Integer.parseInt(text);
		context.push(value);
	};

	public ParsingRule build() {
		return getStartRule().build(new BuildContext());
	}

	protected abstract ParsingRuleBuilder getStartRule();

	protected static NewlineMatcher.Builder newLine() {
		return NewlineMatcher.builder();
	}

	protected static EoiMatcher.Builder endOfInput() {
		return EoiMatcher.instance();
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

	protected static Predicate.Builder not(ParsingExpressionBuilder expression) {
		return Predicate.not(expression);
	}

	protected static ParsingExpressionBuilder endOfLineOrInput() {
		return choice( //
				newLine().ignore(), //
				endOfInput() //
		);
	}

	protected static CharMatcher.Builder crlf() {
		// FIXME: better performing solution
		return CharMatcher.in("\r\n").minCount(1).maxCount(2);
	}

	protected static CharMatcher.Builder anyCharacter() {
		return CharMatcher.any();
	}

	protected static CharMatcher.Builder characterInRange(char from, char to) {
		return CharMatcher.inRange(from, to);
	}

	protected static CharMatcher.Builder character(char c) {
		return CharMatcher.is(c);
	}

	protected static StringMatcher.Builder string(String string) {
		return StringMatcher.of(string);
	}

	protected static CharMatcher.Builder characterIn(String characters) {
		return CharMatcher.in(characters);
	}

	protected static ParsingExpressionBuilder ignoredWhitespace() {
		return IGNORED_WS;
	}

	protected static CharMatcher.Builder horizontalWhitespace() {
		return CharMatcher.of(CharTesters.horizontalWhitespace());
	}

	protected static OnSuccessHandler textToInteger() {
		return TEXT_TO_INTEGER;
	}

	public static OnSuccessHandler pushText() {
		return (Context context) -> context.push(context.getCharSequence());
	}

	public static OnSuccessHandler push(Object value) {
		return (Context context) -> context.push(value);
	}

	public static OnSuccessHandler orElsePush(Object defaultValue) {
		return (Context context) -> {
			if (context.stackSize() == 0) {
				context.push(defaultValue);
			}
		};
	}
}
