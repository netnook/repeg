package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions._util.MatcherTestBase;
import net.netnook.repeg.expressions.chars.CharMatcher;

public class ChoiceTest extends MatcherTestBase {

	private ParsingExpressionBuilder isA;
	private ParsingExpressionBuilder isB;

	@Before
	public void init() {
		isA = CharacterExpression.using(CharMatcher.is('a')).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		isB = CharacterExpression.using(CharMatcher.is('b')).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		buildContext("-abcd-").consumeChar();
	}

	@Test
	public void test_parts() {
		Choice expression = (Choice) Choice.of(isA, isB).build();

		assertThat(expression.parts()).hasSize(2);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
		assertThat(expression.parts().get(1).buildGrammar()).isEqualTo("[b]");
	}

	@Test
	public void test_grammar() {
		assertThat(Choice.of(isA, isB).build().buildGrammar()).isEqualTo("([a] | [b])");
	}

	@Test
	public void test_a_or_b() {
		ParsingExpression expression = Choice.of(isA, isB).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b");
	}

	@Test
	public void test_b_or_a() {
		ParsingExpression expression = Choice.of(isB, isA).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b");
	}

	@Test
	public void test_on_success() {
		ParsingExpression expression = Choice.of(isA, isB) //
				.onSuccess(onSuccessCounter) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);
		assertThat(successCount).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("b");
		assertPositionIs(3);
		assertThat(successCount).isEqualTo(2);

		assertThat(expression.parse(context)).isFalse();
		assertThat(successCount).isEqualTo(2);

		assertFullStackContains("a", "b");
	}
}