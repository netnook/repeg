package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.InvalidExpressionException;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class RepetitionTest extends MatcherTestBase {

	private CharMatcher.Builder isA;
	private CharMatcher.Builder isB;

	@Before
	public void init() {
		isA = CharMatcher.is('a');
		isB = CharMatcher.is('b');
		buildContext("-aaabbb-").consumeChar();
	}

	@Test
	public void test_parts() {
		Repetition expression = (Repetition) Repetition.of(isA).build(buildContext);

		assertThat(expression.parts()).hasSize(1);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
	}

	@Test
	public void test_grammar() {
		assertThat(Repetition.of(isA).minCount(0).build(buildContext).buildGrammar()).isEqualTo("([a])*");
		assertThat(Repetition.of(isA).minCount(1).build(buildContext).buildGrammar()).isEqualTo("([a])+");
		assertThat(Repetition.of(isA).minCount(2).build(buildContext).buildGrammar()).isEqualTo("([a]){2,}");
		assertThat(Repetition.of(isA).minCount(2).maxCount(4).build(buildContext).buildGrammar()).isEqualTo("([a]){2,4}");
		assertThat(Repetition.of(isA).minCount(4).maxCount(4).build(buildContext).buildGrammar()).isEqualTo("([a]){4}");
	}

	@Test
	public void test_0orMore_a() {
		ParsingExpression expression = Repetition.zeroOrMore(isA).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "a", "a");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(4);

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_1orMore_a() {
		ParsingExpression expression = Repetition.oneOrMore(isA).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "a", "a");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_1to2_a() {
		ParsingExpression expression = Repetition.of(isA) //
				.minCount(1) //
				.maxCount(2) //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "a");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_on_success() {
		ParsingExpression expression = Repetition.of(isA) //
				.minCount(1) //
				.maxCount(2) //
				.onSuccess(onSuccessCounter) //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "a");
		assertPositionIs(3);
		assertThat(successCount).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(4);
		assertThat(successCount).isEqualTo(2);

		assertThat(expression.parse(context)).isFalse();
		assertThat(successCount).isEqualTo(2);

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_ignore() {
		ParsingExpression expression = Repetition.of(isA) //
				.minCount(1) //
				.maxCount(2) //
				.ignore() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains();
	}

	@Test
	public void invalid_bounds_min_greater_than_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: minCount > maxCount");
		Repetition.of(isA).minCount(7).maxCount(3).build(buildContext);
	}

	@Test
	public void invalid_bounds_negative_min() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: min count");
		Repetition.of(isA).minCount(-3);
	}

	@Test
	public void invalid_bounds_negative_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: max count");
		Repetition.of(isA).maxCount(-5);
	}

	@Test
	public void invalid_optional_expression_in_repetition() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Cannot have optional content in repeating construct");
		Repetition.of(Optional.of(isA)).build(buildContext);
	}
}