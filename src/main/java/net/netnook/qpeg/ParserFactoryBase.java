package net.netnook.qpeg;

import net.netnook.qpeg.expressions.BuildContext;
import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingRule;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;
import net.netnook.qpeg.expressions.chars.CharMatcher;
import net.netnook.qpeg.expressions.core.CharacterExpression;
import net.netnook.qpeg.expressions.core.Choice;
import net.netnook.qpeg.expressions.core.EndOfInput;
import net.netnook.qpeg.expressions.core.Optional;
import net.netnook.qpeg.expressions.core.Repetition;
import net.netnook.qpeg.expressions.core.Sequence;
import net.netnook.qpeg.expressions.core.StringExpression;
import net.netnook.qpeg.expressions.extras.FloatExpression;
import net.netnook.qpeg.expressions.extras.NewlineExpression;

public abstract class ParserFactoryBase {

	public ParsingRule build() {
		ParsingRule build = getStartRule().build();
		// FIXME: how can we ensure that always cleared event when .build() called directly on ExpressionBuilder
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

	protected static Repetition.Builder one(char c) {
		return one(CharMatcher.is(c));
	}

	protected static Repetition.Builder one(CharMatcher charMatcher) {
		return Repetition.one(CharacterExpression.using(charMatcher));
	}

	protected static Repetition.Builder zeroOrMore(char c) {
		return zeroOrMore(CharMatcher.is(c));
	}

	protected static Repetition.Builder zeroOrMore(CharMatcher charMatcher) {
		return zeroOrMore(CharacterExpression.using(charMatcher));
	}

	protected static Repetition.Builder zeroOrMore(ParsingExpressionBuilder expression) {
		return Repetition.zeroOrMore(expression);
	}

	protected static Repetition.Builder oneOrMore(char c) {
		return oneOrMore(CharMatcher.is(c));
	}

	protected static Repetition.Builder oneOrMore(CharMatcher charMatcher) {
		return oneOrMore(CharacterExpression.using(charMatcher));
	}

	protected static Repetition.Builder oneOrMore(ParsingExpressionBuilder expression) {
		return Repetition.oneOrMore(expression);
	}

	protected static Repetition.Builder repeat(int times, CharMatcher charMatcher) {
		return repeat(times, CharacterExpression.using(charMatcher));
	}

	protected static Repetition.Builder repeat(int times, ParsingExpressionBuilder expression) {
		return Repetition.of(expression).count(times);
	}

	protected static Optional.Builder optional(char c) {
		return optional(CharMatcher.is(c));
	}

	protected static Optional.Builder optional(CharMatcher matcher) {
		return optional(CharacterExpression.using(matcher));
	}

	protected static Optional.Builder optional(ParsingExpressionBuilder expression) {
		return Optional.of(expression);
	}

	protected static StringExpression.Builder string(String string) {
		return StringExpression.of(string);
	}

	protected static EndOfInput endOfInput() {
		return EndOfInput.instance();
	}

	protected static ParsingExpressionBuilder endOfLineOrInput() {
		return choice( //
				newLine(), //
				endOfInput() //
		);
	}

	protected static NewlineExpression.Builder newLine() {
		return NewlineExpression.builder();
	}

	protected static CharMatcher character(char c) {
		return CharMatcher.is(c);
	}

	protected static CharMatcher characterIn(String characters) {
		return CharMatcher.in(characters);
	}

	protected static CharMatcher characterInRange(char from, char to) {
		return CharMatcher.inRange(from, to);
	}

	protected static CharMatcher crlf() {
		// TODO: better performing solution
		return CharMatcher.in("\r\n");
	}

	protected static CharMatcher whitespace() {
		return CharMatcher.whitespace();
	}

	protected static CharMatcher horizontalWhitespace() {
		return CharMatcher.horizontalWhitespace();
	}

	protected static FloatExpression parseFloat() {
		return FloatExpression.instance();
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
