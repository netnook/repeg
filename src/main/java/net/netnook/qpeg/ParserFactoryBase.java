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
import net.netnook.qpeg.expressions.extras.SkipExpression;

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

	protected static EndOfInput.Builder endOfInput() {
		return EndOfInput.instance();
	}

	protected static ParsingExpressionBuilder endOfLineOrInput() {
		return choice( //
				newLine().ignore(), //
				endOfInput() //
		);
	}

	protected static NewlineExpression.Builder newLine() {
		return NewlineExpression.builder();
	}

	protected static CharacterExpression.Builder crlf() {
		// TODO: better performing solution
		return CharacterExpression.in("\r\n");
	}

	protected static CharacterExpression.Builder whitespace() {
		return CharacterExpression.whitespace();
	}

	protected static CharacterExpression.Builder horizontalWhitespace() {
		return CharacterExpression.using(CharMatcher.horizontalWhitespace());
	}

	protected static CharacterExpression.Builder character(char c) {
		return CharacterExpression.character(c);
	}

	protected static CharacterExpression.Builder characterIn(String characters) {
		return CharacterExpression.in(characters);
	}

	protected static CharacterExpression.Builder characterInRange(char from, char to) {
		return CharacterExpression.inRange(from, to);
	}

	protected static StringExpression.Builder string(String string) {
		return StringExpression.of(string);
	}

	protected static FloatExpression parseFloat() {
		return FloatExpression.instance();
	}

	protected static SkipExpression skip(CharMatcher tester) {
		return SkipExpression.characters(tester);
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
