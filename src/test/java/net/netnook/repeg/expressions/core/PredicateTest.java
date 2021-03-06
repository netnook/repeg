package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.Expression;
import net.netnook.repeg.chars.CharMatchers;
import net.netnook.repeg.expressions._util.MatcherTestBase;

public class PredicateTest extends MatcherTestBase {

	private CharacterExpression.Builder isA;

	@Before
	public void init() {
		isA = CharacterExpression.using(CharMatchers.is('a'));
		buildContext("-abcd-").consumeChar();
	}

	@Test
	public void test_parts() {
		Predicate expression = (Predicate) Predicate.match(isA).build();

		assertThat(expression.parts()).hasSize(1);
		assertThat(expression.parts().get(0).buildGrammar()).isEqualTo("[a]");
	}

	@Test
	public void test_grammar() {
		assertThat(Predicate.match(isA).build().buildGrammar()).isEqualTo("&([a])");
		assertThat(Predicate.not(isA).build().buildGrammar()).isEqualTo("!([a])");
	}

	@Test
	public void test_match() {
		Expression expression = Predicate.match(isA).build();

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
		Expression expression = Predicate.not(isA).build();

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
	public void test_onSuccess_unsupported() {
		thrown.expect(UnsupportedOperationException.class);
		Predicate.match(isA).onSuccess(null);
	}
}