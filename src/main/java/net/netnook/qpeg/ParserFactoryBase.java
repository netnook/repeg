package net.netnook.qpeg;

import net.netnook.qpeg.expressions.BuildContext;
import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingRule;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;
import net.netnook.qpeg.expressions.chars.CharTesters;
import net.netnook.qpeg.expressions.core.CharMatcher;
import net.netnook.qpeg.expressions.core.Choice;
import net.netnook.qpeg.expressions.core.EoiMatcher;
import net.netnook.qpeg.expressions.core.Optional;
import net.netnook.qpeg.expressions.core.Repetition;
import net.netnook.qpeg.expressions.core.Sequence;
import net.netnook.qpeg.expressions.core.StringMatcher;
import net.netnook.qpeg.expressions.extras.FloatMatcher;
import net.netnook.qpeg.expressions.extras.NewlineMatcher;

public abstract class ParserFactoryBase {

	public ParsingRule build() {
		ParsingRule build = getStartRule().build();
		// FIXME: how can we ensure that alyways cleared event when .build() called directly on ExpressionBuilder
		BuildContext.clear();
		return build;
	}

	protected abstract ParsingRuleBuilder getStartRule();

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

	protected static EoiMatcher.Builder endOfInput() {
		return EoiMatcher.instance();
	}

	protected static ParsingExpressionBuilder endOfLineOrInput() {
		return choice( //
				newLine().ignore(), //
				endOfInput() //
		);
	}

	protected static NewlineMatcher.Builder newLine() {
		return NewlineMatcher.builder();
	}

	protected static CharMatcher.Builder crlf() {
		// TODO: better performing solution
		return CharMatcher.in("\r\n");
	}

	protected static CharMatcher.Builder whitespace() {
		return CharMatcher.whitespace();
	}

	protected static CharMatcher.Builder horizontalWhitespace() {
		return CharMatcher.using(CharTesters.horizontalWhitespace());
	}

	protected static CharMatcher.Builder character(char c) {
		return CharMatcher.character(c);
	}

	protected static CharMatcher.Builder characterIn(String characters) {
		return CharMatcher.in(characters);
	}

	protected static CharMatcher.Builder characterInRange(char from, char to) {
		return CharMatcher.inRange(from, to);
	}

	protected static StringMatcher.Builder string(String string) {
		return StringMatcher.of(string);
	}

	protected static FloatMatcher.Builder parseFloat() {
		return FloatMatcher.builder();
	}

	public static OnSuccessHandler push(Object value) {
		return (Context context) -> context.push(value);
	}

	public static OnSuccessHandler pushIfEmpty(Object defaultValue) {
		return (Context context) -> {
			if (context.stackSize() == 0) {
				context.push(defaultValue);
			}
		};
	}

	public static OnSuccessHandler pushText() {
		return OnSuccessHandler.PUSH_TEXT;
	}

	protected static OnSuccessHandler pushTextAsString() {
		return OnSuccessHandler.PUSH_TEXT_AS_STRING;
	}

	protected static OnSuccessHandler pushTextAsNullableString() {
		return OnSuccessHandler.PUSH_TEXT_AS_NULLABLE_STRING;
	}

	protected static OnSuccessHandler pushTextAsInteger() {
		return OnSuccessHandler.PUSH_TEXT_AS_INTEGER;
	}

	protected static OnSuccessHandler pushTextAsFloat() {
		return OnSuccessHandler.PUSH_TEXT_AS_FLOAT;
	}

	protected static OnSuccessHandler pushTextAsNullableFloat() {
		return OnSuccessHandler.PUSH_TEXT_AS_NULLABLE_FLOAT;
	}
}
