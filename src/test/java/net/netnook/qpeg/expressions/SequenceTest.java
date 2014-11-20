package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SequenceTest extends BaseMatcherTest {

	private CharMatcher.Builder isA;
	private CharMatcher.Builder isB;

	@Before
	public void init() {
		isA = CharMatcher.is('a');
		isB = CharMatcher.is('b');

		context = new Context("-ababXX-");
		context.consumeChar();

		buildContext = new BuildContext();
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
		assertStackContains("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("a", "b");
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b", "a", "b");
	}

	@Test
	public void test_sequence_fail_on_first() {
		context = new Context("-abX-");
		context.consumeChar();

		ParsingExpression expression = Sequence.of(isA, isB).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("a", "b");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains("a", "b");
	}

	@Test
	public void test_sequence_fail_on_second() {
		context = new Context("-abaX-");
		context.consumeChar();

		ParsingExpression expression = Sequence.of(isA, isB).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("a", "b");
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
		assertStackContains("a", "b");
		assertPositionIs(3);
		assertThat(successCount).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("a", "b");
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
		assertStackContains();
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains();
	}
}