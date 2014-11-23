package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class ChoiceTest extends MatcherTestBase {

	private CharMatcher.Builder isA;
	private CharMatcher.Builder isB;

	@Before
	public void init() {
		isA = CharMatcher.character('a');
		isB = CharMatcher.character('b');
		buildContext("-abcd-").consumeChar();
	}

	@Test
	public void test_parts() {
		Choice expression = (Choice) Choice.of(isA, isB).build(buildContext);

		assertThat(expression.parts()).hasSize(2);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
		assertThat(expression.parts().get(1).buildGrammar()).isEqualTo("[b]");
	}

	@Test
	public void test_grammar() {
		assertThat(Choice.of(isA, isB).build(buildContext).buildGrammar()).isEqualTo("([a] | [b])");
	}

	@Test
	public void test_a_or_b() {
		ParsingExpression expression = Choice.of(isA, isB).build(buildContext);

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
		ParsingExpression expression = Choice.of(isB, isA).build(buildContext);

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
				.build(buildContext);

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

	@Test
	public void test_ignore() {
		ParsingExpression expression = Choice.of(isA, isB) //
				.ignore() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();

		assertFullStackContains();
	}
}