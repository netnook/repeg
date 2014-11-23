package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class SequenceTest extends MatcherTestBase {

	private CharMatcher.Builder isA;
	private CharMatcher.Builder isB;

	@Before
	public void init() {
		isA = CharMatcher.character('a');
		isB = CharMatcher.character('b');
		buildContext("-ababXX-").consumeChar();
	}

	@Test
	public void test_parts() {
		Sequence expression = (Sequence) Sequence.of(isA, isB).build(buildContext);

		assertThat(expression.parts()).hasSize(2);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
		assertThat(expression.parts().get(1).buildGrammar()).isEqualTo("[b]");
	}

	@Test
	public void test_grammar() {
		assertThat(Sequence.of(isA, isB).build(buildContext).buildGrammar()).isEqualTo("([a] [b])");
	}

	@Test
	public void test_sequence() {
		ParsingExpression expression = Sequence.of(isA, isB).build(buildContext);

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

		ParsingExpression expression = Sequence.of(isA, isB).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b");
	}

	@Test
	public void test_sequence_fail_on_second() {
		buildContext("-abaX-").consumeChar();

		ParsingExpression expression = Sequence.of(isA, isB).build(buildContext);

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
				.build(buildContext);

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

	@Test
	public void test_ignore() {
		ParsingExpression expression = Sequence.of(isA, isB) //
				.ignore() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains();
	}
}