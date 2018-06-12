package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.ParsingExpressionBuilder;
import net.netnook.repeg.expressions._util.MatcherTestBase;
import net.netnook.repeg.expressions.chars.CharMatcher;

public class OptionalTest extends MatcherTestBase {

	private ParsingExpressionBuilder isA;

	@Before
	public void init() {
		isA = CharacterExpression.using(CharMatcher.is('a')).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		buildContext("-abcd-").consumeChar();
	}

	@Test
	public void test_parts() {
		Optional expression = (Optional) Optional.of(isA).build();

		assertThat(expression.parts()).hasSize(1);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
	}

	@Test
	public void test_grammar() {
		assertThat(Optional.of(isA).build().buildGrammar()).isEqualTo("([a])?");
	}

	@Test
	public void test_options() {
		ParsingExpression expression = Optional.of(isA).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(2);

		assertFullStackContains("a");
	}

	@Test
	public void test_on_success() {
		ParsingExpression expression = Optional.of(isA) //
				.onSuccess(onSuccessCounter) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);
		assertThat(successCount).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(2);
		assertThat(successCount).isEqualTo(2);

		assertFullStackContains("a");
	}
}