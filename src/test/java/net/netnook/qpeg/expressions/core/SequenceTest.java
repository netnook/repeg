package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class SequenceTest extends MatcherTestBase {

	private ParsingExpressionBuilder isA;
	private ParsingExpressionBuilder isB;

	@Before
	public void init() {
		isA = CharacterExpression.character('a').onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
		isB = CharacterExpression.character('b').onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING);
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
		ParsingExpression expression = Sequence.of(isA, isB).build();

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

		ParsingExpression expression = Sequence.of(isA, isB).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b");
	}

	@Test
	public void test_sequence_fail_on_second() {
		buildContext("-abaX-").consumeChar();

		ParsingExpression expression = Sequence.of(isA, isB).build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b", "a"); // extra a will have been matched, parent must reset stack
	}

	@Test
	public void test_on_success() {
		ParsingExpression expression = Sequence.of(isA, isB) //
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