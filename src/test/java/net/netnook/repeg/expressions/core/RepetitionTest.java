package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.Expression;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.chars.CharMatchers;
import net.netnook.repeg.exceptions.InvalidExpressionException;
import net.netnook.repeg.expressions._util.MatcherTestBase;

public class RepetitionTest extends MatcherTestBase {

	private CharacterExpression.Builder isA;

	@Before
	public void init() {
		isA = CharacterExpression.using(CharMatchers.is('a')).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
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
	public void test_zeroOrMore_a() {
		Repetition expression = (Repetition) Repetition.zeroOrMore(isA).build();
		assertThat(expression.getMinCount()).isEqualTo(0);
		assertThat(expression.getMaxCount()).isEqualTo(Integer.MAX_VALUE);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "a", "a");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(4);

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_oneOrMore_a() {
		Repetition expression = (Repetition) Repetition.oneOrMore(isA).build();
		assertThat(expression.getMinCount()).isEqualTo(1);
		assertThat(expression.getMaxCount()).isEqualTo(Integer.MAX_VALUE);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "a", "a");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_one_a() {
		Repetition expression = (Repetition) Repetition.one(isA).build();
		assertThat(expression.getMinCount()).isEqualTo(1);
		assertThat(expression.getMaxCount()).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();

		assertFullStackContains("a", "a", "a");
	}

	@Test
	public void test_1to2_a() {
		Repetition expression = (Repetition) Repetition.of(isA) //
				.minCount(1) //
				.maxCount(2) //
				.build();
		assertThat(expression.getMinCount()).isEqualTo(1);
		assertThat(expression.getMaxCount()).isEqualTo(2);

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
	public void test_unbounded() {
		Repetition expression = (Repetition) Repetition.of(isA) //
				.minCount(7) //
				.maxCount(9) //
				.maxUnbounded().build();
		assertThat(expression.getMinCount()).isEqualTo(7);
		assertThat(expression.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	public void test_on_success() {
		Expression expression = Repetition.of(isA) //
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

	@Test
	public void test_auto_replacement_by_charexpression() {
		isA = CharacterExpression.using(CharMatchers.is('a'));

		CharacterExpression e;

		e = (CharacterExpression) Repetition.of(isA.clone()).build();
		assertThat(e.getMinCount()).isEqualTo(0);
		assertThat(e.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
		assertThat(e.getOnSuccess()).isNull();

		e = (CharacterExpression) Repetition.of(isA.clone()).minCount(5).build();
		assertThat(e.getMinCount()).isEqualTo(5);
		assertThat(e.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
		assertThat(e.getOnSuccess()).isNull();

		e = (CharacterExpression) Repetition.of(isA.clone()).maxCount(8).build();
		assertThat(e.getMinCount()).isEqualTo(0);
		assertThat(e.getMaxCount()).isEqualTo(8);
		assertThat(e.getOnSuccess()).isNull();

		e = (CharacterExpression) Repetition.of(isA.clone()).onSuccess(OnSuccessHandler.CLEAR_STACK).build();
		assertThat(e.getMinCount()).isEqualTo(0);
		assertThat(e.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
		assertThat(e.getOnSuccess()).isSameAs(OnSuccessHandler.CLEAR_STACK);

		e = (CharacterExpression) Repetition.of(isA.clone()).minCount(3).maxCount(4).onSuccess(OnSuccessHandler.CLEAR_STACK).build();
		assertThat(e.getMinCount()).isEqualTo(3);
		assertThat(e.getMaxCount()).isEqualTo(4);
		assertThat(e.getOnSuccess()).isSameAs(OnSuccessHandler.CLEAR_STACK);

		e = (CharacterExpression) Repetition.of(isA.clone().minCount(1).maxCount(1).onSuccess(null)).onSuccess(OnSuccessHandler.CLEAR_STACK).build();
		assertThat(e.getMinCount()).isEqualTo(0);
		assertThat(e.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
		assertThat(e.getOnSuccess()).isSameAs(OnSuccessHandler.CLEAR_STACK);
	}

	@Test
	public void test_not_auto_replacement_by_charexpression() {
		isA = CharacterExpression.using(CharMatchers.is('a'));

		Repetition e;

		e = (Repetition) Repetition.of(isA.clone().minCount(0)).count(5).build();
		assertThat(e.getMinCount()).isEqualTo(5);
		assertThat(e.getMaxCount()).isEqualTo(5);
		assertThat(e.getOnSuccess()).isNull();
		assertThat(((CharacterExpression) e.parts().get(0)).getMinCount()).isEqualTo(0);
		assertThat(((CharacterExpression) e.parts().get(0)).getMaxCount()).isEqualTo(1);
		assertThat(((CharacterExpression) e.parts().get(0)).getOnSuccess()).isNull();

		e = (Repetition) Repetition.of(isA.clone().maxCount(2)).count(5).build();
		assertThat(e.getMinCount()).isEqualTo(5);
		assertThat(e.getMaxCount()).isEqualTo(5);
		assertThat(e.getOnSuccess()).isNull();
		assertThat(((CharacterExpression) e.parts().get(0)).getMinCount()).isEqualTo(1);
		assertThat(((CharacterExpression) e.parts().get(0)).getMaxCount()).isEqualTo(2);
		assertThat(((CharacterExpression) e.parts().get(0)).getOnSuccess()).isNull();

		e = (Repetition) Repetition.of(isA.clone().onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING)).count(5).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_FLOAT)
				.build();
		assertThat(e.getMinCount()).isEqualTo(5);
		assertThat(e.getMaxCount()).isEqualTo(5);
		assertThat(e.getOnSuccess()).isSameAs(OnSuccessHandler.PUSH_TEXT_AS_FLOAT);
		assertThat(((CharacterExpression) e.parts().get(0)).getMinCount()).isEqualTo(1);
		assertThat(((CharacterExpression) e.parts().get(0)).getMaxCount()).isEqualTo(1);
		assertThat(((CharacterExpression) e.parts().get(0)).getOnSuccess()).isSameAs(OnSuccessHandler.PUSH_TEXT_AS_STRING);
	}

}