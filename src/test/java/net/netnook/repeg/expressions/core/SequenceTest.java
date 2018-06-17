package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.ParsingExpressionBuilder;
import net.netnook.repeg.chars.CharMatchers;
import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.expressions._util.MatcherTestBase;

public class SequenceTest extends MatcherTestBase {

	private ParsingExpressionBuilder isA;
	private ParsingExpressionBuilder isB;

	@Before
	public void init() {
		isA = CharacterExpression.using(CharMatchers.is('a')).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		isB = CharacterExpression.using(CharMatchers.is('b')).onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		buildContext("-ababXX-").consumeChar();
	}

	@Test
	public void test_parts() {
		Sequence expression = (Sequence) Sequence.of(isA, isB).build();

		assertThat(expression.parts()).hasSize(2);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
		assertThat(expression.parts().get(1).buildGrammar()).isEqualTo("[b]");
	}

	@Test
	public void test_grammar() {
		assertThat(Sequence.of(isA, isB).build().buildGrammar()).isEqualTo("([a] [b])");
	}

	@Test
	public void test_sequence() {
		Expression expression = Sequence.of(isA, isB).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b", "a", "b");
	}

	@Test
	public void test_sequence_fail_on_first() {
		buildContext("-abX-").consumeChar();

		Expression expression = Sequence.of(isA, isB).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b");
	}

	@Test
	public void test_sequence_fail_on_second() {
		buildContext("-abaX-").consumeChar();

		Expression expression = Sequence.of(isA, isB).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b", "a"); // extra a will have been matched, parent must reset stack
	}

	@Test
	public void test_on_success() {
		Expression expression = Sequence.of(isA, isB) //
				.onSuccess(onSuccessCounter) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);
		assertThat(successCount).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(5);
		assertThat(successCount).isEqualTo(2);

		assertThat(expression.parse(context)).isFalse();
		assertThat(successCount).isEqualTo(2);

		assertFullStackContains("a", "b", "a", "b");
	}
}