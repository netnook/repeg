package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.InvalidExpressionException;
import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class RepetitionTest extends MatcherTestBase {

	private ParsingExpressionBuilder isA;
	private ParsingExpressionBuilder isB;

	@Before
	public void init() {
		isA = CharacterExpression.character('a').onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		isB = CharacterExpression.character('b').onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		buildContext("-aaabbb-").consumeChar();
	}

	@Test
	public void test_parts() {
		Repetition expression = (Repetition) Repetition.of(isA).build();

		assertThat(expression.parts()).hasSize(1);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
	}

	@Test
	public void test_grammar() {
		assertThat(Repetition.of(isA).minCount(0).build().buildGrammar()).isEqualTo("([a])*");
		assertThat(Repetition.of(isA).minCount(1).build().buildGrammar()).isEqualTo("([a])+");
		assertThat(Repetition.of(isA).minCount(2).build().buildGrammar()).isEqualTo("([a]){2,}");
		assertThat(Repetition.of(isA).minCount(2).maxCount(4).build().buildGrammar()).isEqualTo("([a]){2,4}");
		assertThat(Repetition.of(isA).minCount(4).maxCount(4).build().buildGrammar()).isEqualTo("([a]){4}");
	}

	@Test
	public void test_0orMore_a() {
		ParsingExpression expression = Repetition.zeroOrMore(isA).build();

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
		ParsingExpression expression = Repetition.oneOrMore(isA).build();

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
				.build();

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
				.build();

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
	public void invalid_bounds_min_greater_than_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: minCount > maxCount");
		Repetition.of(isA).minCount(7).maxCount(3).build();
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
		Repetition.of(Optional.of(isA)).build();
	}
}