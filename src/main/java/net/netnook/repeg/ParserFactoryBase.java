package net.netnook.repeg;

import net.netnook.repeg.chars.CharMatcher;
import net.netnook.repeg.chars.CharMatchers;
import net.netnook.repeg.expressions.core.*;
import net.netnook.repeg.expressions.extras.FloatExpression;
import net.netnook.repeg.expressions.extras.NewlineExpression;

/**
 * Base class for defining parsers.
 */
public abstract class ParserFactoryBase<T> {

    public Parser<T> build() {
        return new ParserImpl<T>(getStartRule().build());
    }

    /**
     * This method should return the start rule for parsing the input.
     *
     * @return start rule.
     */
    protected abstract RuleEnum getStartRule();

    /**
     * Create a new {@link Sequence} expression with the specified sub-expressions.
     * <p>
     * A Sequence matches if all it's sub-expressions match in the specified order.
     *
     * @param expressions sub-expressions to match in sequence
     * @return the new {@link Sequence} expression.
     */
    protected static Sequence.Builder sequence(ExpressionBuilder... expressions) {
        return Sequence.of(expressions);
    }

    /**
     * Create a new {@link Choice} expression for the specified sub-expressions.
     * <p>
     * Each sub-expression is tested in turn and a Choice matches if one of these sub-expressions match.
     *
     * @param expressions the sub-expressions to match in order.
     * @return the new {@link Choice} expression.
     */
    protected static Choice.Builder choice(ExpressionBuilder... expressions) {
        return Choice.of(expressions);
    }

    /**
     * Match a single character in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min and max count of 1 containing as a sub-expression a
     * {@link CharMatcher} for the single character {@code c}.
     *
     * @param c character to match
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder one(char c) {
        return one(CharMatchers.is(c));
    }

    /**
     * Match a single character in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min and max count of 1 containing as a sub-expression the specified
     * {@link CharMatcher}.
     *
     * @param charMatcher character matcher to perform match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder one(CharMatcher charMatcher) {
        return Repetition.one(CharacterExpression.using(charMatcher));
    }

    /**
     * Match a zero or more characters in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min count 0 and max count {@link Integer#MAX_VALUE}
     * containing as a sub-expression a {@link CharMatcher} for the single character {@code c}.
     *
     * @param c character to match
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder zeroOrMore(char c) {
        return zeroOrMore(CharMatchers.is(c));
    }

    /**
     * Match a zero or more characters in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min count 0 and max count {@link Integer#MAX_VALUE}
     * containing as a sub-expression the specified {@link CharMatcher}.
     *
     * @param charMatcher character matcher to perform match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder zeroOrMore(CharMatcher charMatcher) {
        return zeroOrMore(CharacterExpression.using(charMatcher));
    }

    /**
     * Match a sub expression zero or more times.
     * <p>
     * Creates a new {@link Repetition} expression with min count 0 and max count {@link Integer#MAX_VALUE}
     * containing the specified sub-expression.
     *
     * @param expression expression to repeatedly match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder zeroOrMore(ExpressionBuilder expression) {
        return Repetition.zeroOrMore(expression);
    }

    /**
     * Match a one or more characters in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min count 1 and max count {@link Integer#MAX_VALUE}
     * containing as a sub-expression a {@link CharMatcher} for the single character {@code c}.
     *
     * @param c character to match
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder oneOrMore(char c) {
        return oneOrMore(CharMatchers.is(c));
    }

    /**
     * Match a one or more characters in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min count 1 and max count {@link Integer#MAX_VALUE}
     * containing as a sub-expression the specified {@link CharMatcher}.
     *
     * @param charMatcher character matcher to perform match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder oneOrMore(CharMatcher charMatcher) {
        return oneOrMore(CharacterExpression.using(charMatcher));
    }

    /**
     * Match a sub expression one or more times.
     * <p>
     * Creates a new {@link Repetition} expression with min count 1 and max count {@link Integer#MAX_VALUE}
     * containing the specified sub-expression.
     *
     * @param expression expression to repeatedly match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder oneOrMore(ExpressionBuilder expression) {
        return Repetition.oneOrMore(expression);
    }

    /**
     * Match a character a specified number of times in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min and max count of {@code times}
     * containing as a sub-expression the specified {@link CharMatcher}.
     *
     * @param times       number of times to repeat character.
     * @param charMatcher character matcher to perform match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder repeat(int times, CharMatcher charMatcher) {
        return repeat(times, CharacterExpression.using(charMatcher));
    }

    /**
     * Match a sub expression a specified number of times in the input.
     * <p>
     * Creates a new {@link Repetition} expression with min and max count of {@code times}
     * containing the specified sub-expression.
     *
     * @param times      number of times to repeat expression.
     * @param expression expression to repeatedly match.
     * @return the new {@link Repetition} expression.
     */
    protected static Repetition.Builder repeat(int times, ExpressionBuilder expression) {
        return Repetition.of(expression).count(times);
    }

    /**
     * Optionally match a character in the input.
     * <p>
     * Creates a new {@link Optional} expression containing as a sub-expression a {@link CharMatcher} for the single
     * character {@code c}.
     *
     * @param c character to match
     * @return the new {@link Optional} expression.
     */
    protected static Optional.Builder optional(char c) {
        return optional(CharMatchers.is(c));
    }

    /**
     * Optionally match a character in the input.
     * <p>
     * Creates a new {@link Optional} expression containing as a sub-expression the specified {@link CharMatcher}.
     *
     * @param matcher character to match
     * @return the new {@link Optional} expression.
     */
    protected static Optional.Builder optional(CharMatcher matcher) {
        return optional(CharacterExpression.using(matcher));
    }

    /**
     * Optionally match an expression in the input.
     * <p>
     * Creates a new {@link Optional} expression containing the specified expression.
     *
     * @param expression expression to optionally match.
     * @return the new {@link Optional} expression.
     */
    protected static Optional.Builder optional(ExpressionBuilder expression) {
        return Optional.of(expression);
    }

    /**
     * Test the input for match against the specified expression without consuming any input.
     * <p>
     * Creates a new {@link Predicate} expression containing the specified expression.
     *
     * @param expression expression to match.
     * @return the new {@link Predicate} expression.
     */
    protected static Predicate.Builder match(ExpressionBuilder expression) {
        return Predicate.match(expression);
    }

    /**
     * Test the input for no-match against the specified expression without consuming any input.
     * <p>
     * Creates a new {@link Predicate} expression containing the specified expression.
     *
     * @param expression expression which must not match.
     * @return the new {@link Predicate} expression.
     */
    protected static Predicate.Builder not(ExpressionBuilder expression) {
        return Predicate.not(expression);
    }

    /**
     * Match a sequence of characters in the input.
     * <p>
     * Creates a new {@link StringExpression} expression which matches if the input contains the specified string.
     *
     * @param string string to match.
     * @return the new {@link StringExpression} expression.
     */
    protected static StringExpression.Builder string(String string) {
        return StringExpression.of(string);
    }

    /**
     * Match the end of input.
     * <p>
     * The resulting matcher will only match when the end of the input has been reached.
     *
     * @return the new {@link EndOfInput} expression.
     */
    protected static EndOfInput endOfInput() {
        return EndOfInput.instance();
    }

    /**
     * Match the end of input or new line.
     * <p>
     * Equivalent to choice of {@link #newLine()}  or {@link #endOfInput()}.
     *
     * @return the new expression.
     */
    protected static ExpressionBuilder endOfLineOrInput() {
        return choice( //
                newLine(), //
                endOfInput() //
        );
    }

    /**
     * Match the end of line sequence.  See {@link NewlineExpression}.
     *
     * @return the new expression.
     */
    protected static NewlineExpression.Builder newLine() {
        return NewlineExpression.builder();
    }

    /**
     * Create {@link CharMatcher} which matches any character.
     *
     * @return the new character matcher.
     */
    protected static CharMatcher anyChar() {
        return CharMatchers.any();
    }

    /**
     * Create {@link CharMatcher} which matches the specified character.
     *
     * @param c character to match.
     * @return the new character matcher.
     */
    protected static CharMatcher character(char c) {
        return CharMatchers.is(c);
    }

    /**
     * Create {@link CharMatcher} which matches any of the characters in the specified string.
     *
     * @param characters string containing characters to match.
     * @return the new character matcher.
     */
    protected static CharMatcher characterIn(String characters) {
        return CharMatchers.in(characters);
    }

    /**
     * Create {@link CharMatcher} which matches any of the characters in the specified range (inclusive).
     *
     * @param from start of character range to match (inclusive).
     * @param to   end of character range to match (inclusive).
     * @return the new character matcher.
     */
    protected static CharMatcher characterInRange(char from, char to) {
        return CharMatchers.inRange(from, to);
    }

    /**
     * Create {@link CharMatcher} which matches with CR (\r) or LF (\n).
     *
     * @return the new character matcher.
     */
    protected static CharMatcher crlf() {
        return CharMatchers.crlf();
    }

    /**
     * Create {@link CharMatcher} which matches any ASCII whitespace. See {@link CharMatchers#asciiWhitespace()}.
     *
     * @return the new character matcher.
     */
    protected static CharMatcher asciiWhitespace() {
        return CharMatchers.asciiWhitespace();
    }

    /**
     * Create {@link CharMatcher} which matches any horizontal whitespace. See {@link CharMatchers#horizontalWhitespace()}.
     *
     * @return the new character matcher.
     */
    protected static CharMatcher horizontalWhitespace() {
        return CharMatchers.horizontalWhitespace();
    }

    /**
     * Create an expression which matches a float in the input (e.g. "1.23").  The expression returned contains
     * an {@link OnSuccessHandler} which converts the matched input to a {@link Float} and adds it to the stack.
     * <p>
     * See {@link FloatExpression}
     *
     * @return the new float expression.
     */
    protected static FloatExpression parseFloat() {
        return FloatExpression.instance();
    }

    /**
     * Create an {@link OnSuccessHandler} which pushes the specified value onto the stack.
     *
     * @param value value to push onto the stack.
     * @return the new handler.
     */
    public static OnSuccessHandler push(Object value) {
        return (Context context) -> context.push(value);
    }

    /**
     * Create an {@link OnSuccessHandler} which pushes the specified value onto the stack if nothing was
     * added to the stack by the expression or any descendants.
     *
     * @param defaultValue to push onto the stack.
     * @return the new handler.
     */
    public static OnSuccessHandler pushIfEmpty(Object defaultValue) {
        return (Context context) -> {
            if (context.size() == 0) {
                context.push(defaultValue);
            }
        };
    }

    /**
     * Create an {@link OnSuccessHandler} which pushes the matched input text onto the stack as a {@link CharSequence}.
     *
     * @return the new handler.
     */
    public static OnSuccessHandler pushText() {
        return OnSuccessHandler.PUSH_TEXT;
    }

    /**
     * Create an {@link OnSuccessHandler} which pushes the matched input text onto the stack as a {@link String}.
     * Will push an empty string if match was of length 0.
     *
     * @return the new handler.
     */
    protected static OnSuccessHandler pushTextAsString() {
        return OnSuccessHandler.PUSH_TEXT_AS_STRING;
    }

    /**
     * Create an {@link OnSuccessHandler} which pushes the matched input text onto the stack as a {@link String}.
     * Will push {@code null} if match was of length 0.
     *
     * @return the new handler.
     */
    protected static OnSuccessHandler pushTextAsNullableString() {
        return OnSuccessHandler.PUSH_TEXT_AS_NULLABLE_STRING;
    }

    /**
     * Create an {@link OnSuccessHandler} which converts the matches input to a {@link Integer} and pushes it onto the stack.
     *
     * @return the new handler.
     */
    protected static OnSuccessHandler pushTextAsInteger() {
        return OnSuccessHandler.PUSH_TEXT_AS_INTEGER;
    }

    /**
     * Create an {@link OnSuccessHandler} which converts the matches input to a {@link Float} and pushes it onto the stack.
     *
     * @return the new handler.
     */
    protected static OnSuccessHandler pushTextAsFloat() {
        return OnSuccessHandler.PUSH_TEXT_AS_FLOAT;
    }

    /**
     * Create an {@link OnSuccessHandler} which converts the matches input to a {@link Float} and pushes it onto the stack,
     * pushing null if the match was of length 0.
     *
     * @return the new handler.
     */
    protected static OnSuccessHandler pushTextAsNullableFloat() {
        return OnSuccessHandler.PUSH_TEXT_AS_NULLABLE_FLOAT;
    }
}
