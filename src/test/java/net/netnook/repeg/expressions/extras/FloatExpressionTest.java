package net.netnook.repeg.expressions.extras;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.Test;

import net.netnook.repeg.Expression;
import net.netnook.repeg.expressions._util.MatcherTestBase;

public class FloatExpressionTest extends MatcherTestBase {

	@Test
	public void test_positive() {
		buildContext("-1.234-").consumeChar();

		Expression expression = FloatExpression.instance();

		assertThat(expression.parse(context)).isTrue();
		assertThat((Float) getNewOnStack(0)).isCloseTo(1.234f, Offset.offset(0.0001f));
		assertPositionIs(6);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_negative() {
		buildContext("--1.234-").consumeChar();

		Expression expression = FloatExpression.instance();

		assertThat(expression.parse(context)).isTrue();
		assertThat((Float) getNewOnStack(0)).isCloseTo(-1.234f, Offset.offset(0.0001f));
		assertPositionIs(7);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_start_with_dot() {
		buildContext("-.234-").consumeChar();

		Expression expression = FloatExpression.instance();

		assertThat(expression.parse(context)).isTrue();
		assertThat((Float) getNewOnStack(0)).isCloseTo(0.234f, Offset.offset(0.0001f));
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_negative_start_with_dot() {
		buildContext("--.234-").consumeChar();

		Expression expression = FloatExpression.instance();

		assertThat(expression.parse(context)).isTrue();
		assertThat((Float) getNewOnStack(0)).isCloseTo(-0.234f, Offset.offset(0.0001f));
		assertPositionIs(6);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_build_grammar() {
		assertThat(FloatExpression.instance().buildGrammar()).isEqualTo("[:float:]");
	}
}