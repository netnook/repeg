package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class OptionalTest extends MatcherTestBase {

	private ParsingExpressionBuilder isA;

	@Before
	public void init() {
		isA = CharacterExpression.character('a').onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
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