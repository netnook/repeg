package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NewlineMatcherTest extends BaseMatcherTest {

	@Test
	public void test_crlf() {
		buildContext("-\r\n\r\n-");
		context.consumeChar();

		ParsingExpression expression = NewlineMatcher.builder().build(buildContext);

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

		ParsingExpression expression = NewlineMatcher.builder().build(buildContext);

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
	public void test_lfcr() {
		buildContext("-\n\r\n\r-");
		context.consumeChar();

		ParsingExpression expression = NewlineMatcher.builder().build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\n\r");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("\n\r");
		assertPositionIs(5);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_lf() {
		buildContext("-\n\n-");
		context.consumeChar();

		ParsingExpression expression = NewlineMatcher.builder().build(buildContext);

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

		ParsingExpression expression = NewlineMatcher.builder().ignore().build(buildContext);

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
		buildContext("-\r\n\r\r\n\n\n\r-");
		context.consumeChar();

		ParsingExpression expression = NewlineMatcher.builder().build(buildContext);

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
		assertNewOnStack("\n\r");
		assertPositionIs(9);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_build_grammar() {
		assertThat(NewlineMatcher.builder().build(buildContext).buildGrammar()).isEqualTo("('\\n\\r'|'\\n'|'\\r\\n'|'\\r')");
	}
}