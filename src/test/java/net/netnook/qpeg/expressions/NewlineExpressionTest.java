package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.netnook.qpeg.expressions._util.MatcherTestBase;
import net.netnook.qpeg.expressions.extras.NewlineExpression;

public class NewlineExpressionTest extends MatcherTestBase {

	@Test
	public void test_crlf() {
		buildContext("-\r\n\r\n-");
		context.consumeChar();

		ParsingExpression expression = NewlineExpression.builder().build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r\n");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r\n");
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_cr() {
		buildContext("-\r\r-");
		context.consumeChar();

		ParsingExpression expression = NewlineExpression.builder().build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_lf() {
		buildContext("-\n\n-");
		context.consumeChar();

		ParsingExpression expression = NewlineExpression.builder().build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\n");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\n");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_ignore() {
		buildContext("-\n\n-");
		context.consumeChar();

		ParsingExpression expression = NewlineExpression.builder().ignore().build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_mix() {
		buildContext("-\r\n\r\r\n\n\r\n-");
		context.consumeChar();

		ParsingExpression expression = NewlineExpression.builder().build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r\n");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r\n");
		assertPositionIs(6);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\n");
		assertPositionIs(7);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\r\n");
		assertPositionIs(9);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_build_grammar() {
		assertThat(NewlineExpression.builder().build().buildGrammar()).isEqualTo("('\\n'|'\\r\\n'|'\\r')");
	}
}