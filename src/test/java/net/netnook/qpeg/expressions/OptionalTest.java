package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class OptionalTest extends BaseMatcherTest {

	private CharMatcher.Builder isA;

	@Before
	public void init() {
		isA = CharMatcher.is('a');

		context = new Context("-abcd-");
		context.consumeChar();

		buildContext = new BuildContext();
	}

	@Test
	public void test_parts() {
		Optional expression = (Optional) Optional.of(isA).build(buildContext);

		assertThat(expression.parts()).hasSize(1);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
	}

	@Test
	public void test_grammar() {
		assertThat(Optional.of(isA).build(buildContext).buildGrammar()).isEqualTo("([a])?");
	}

	@Test
	public void test_options() {
		ParsingExpression expression = Optional.of(isA).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(2);

		assertFullStackContains("a");
	}

	@Test
	public void test_on_success() {
		context = new Context("-abcd-");
		context.consumeChar();

		ParsingExpression expression = Optional.of(isA) //
				.onSuccess(onSuccessCounter) //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("a");
		assertPositionIs(2);
		assertThat(successCount).isEqualTo(1);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(2);
		assertThat(successCount).isEqualTo(2);

		assertFullStackContains("a");
	}

	@Test
	public void test_ignore() {
		context = new Context("-abcd-");
		context.consumeChar();

		ParsingExpression expression = Optional.of(isA) //
				.ignore() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(2);

		assertFullStackContains();
	}

	@Test
	public void test_success_handler_and_ignore_throws_exception() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Cannot have ignore=true and an onSuccess handler");

		Optional.of(isA) //
				.onSuccess(onSuccessCounter) //
				.ignore() //
				.build(buildContext);
	}
}