package net.netnook.repeg;

import net.netnook.repeg.expressions.Context;
import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions.ParsingRule;
import net.netnook.repeg.expressions.ParsingRuleBuilder;
import net.netnook.repeg.expressions.chars.CharMatcher;
import net.netnook.repeg.expressions.core.CharacterExpression;
import net.netnook.repeg.expressions.core.Choice;
import net.netnook.repeg.expressions.core.EndOfInput;
import net.netnook.repeg.expressions.core.Optional;
import net.netnook.repeg.expressions.core.Repetition;
import net.netnook.repeg.expressions.core.Sequence;
import net.netnook.repeg.expressions.core.StringExpression;
import net.netnook.repeg.expressions.extras.FloatExpression;
import net.netnook.repeg.expressions.extras.NewlineExpression;

public abstract class ParserFactoryBase {

	public ParsingRule build() {
		return getStartRule().build();
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
		return CharMatcher.crlf();
	}

	protected static CharMatcher asciiWhitespace() {
		return CharMatcher.asciiWhitespace();
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
