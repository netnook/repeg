package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PredicateTest extends BaseMatcherTest {

	private CharMatcher.Builder isA;

	@Before
	public void init() {
		isA = CharMatcher.is('a');
		buildContext("-abcd-").consumeChar();
	}

	@Test
	public void test_parts() {
		Predicate expression = (Predicate) Predicate.match(isA).build(buildContext);

		assertThat(expression.parts()).hasSize(1);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
	}

	@Test
	public void test_grammar() {
		assertThat(Predicate.match(isA).build(buildContext).buildGrammar()).isEqualTo("&([a])");
		assertThat(Predicate.not(isA).build(buildContext).buildGrammar()).isEqualTo("!([a])");
	}

	@Test
	public void test_match() {
		ParsingExpression expression = Predicate.match(isA).build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(1);

		context.consumeChar();

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
		assertPositionIs(2);

		assertFullStackContains();
	}

	@Test
	public void test_not() {
		ParsingExpression expression = Predicate.not(isA).build(buildContext);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
		assertPositionIs(1);

		context.consumeChar();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack();
		assertPositionIs(2);

		assertFullStackContains();
	}

	@Test
	public void test_ignore_unsupported() {
		thrown.expect(UnsupportedOperationException.class);
		Predicate.match(isA).ignore();
	}

	@Test
	public void test_onSuccess_unsupported() {
		thrown.expect(UnsupportedOperationException.class);
		Predicate.match(isA).onSuccess(null);
	}
}