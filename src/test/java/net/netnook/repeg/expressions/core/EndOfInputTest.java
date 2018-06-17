package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.expressions._util.MatcherTestBase;

public class EndOfInputTest extends MatcherTestBase {

	@Before
	public void init() {
		buildContext("ab");
	}

	@Test
	public void test_grammar() {
		assertThat(EndOfInput.instance().build().buildGrammar()).isEqualTo("EOI");
	}

	@Test
	public void test_eoi() {
		Expression expression = EndOfInput.instance().build();
		assertThat(expression.parse(context)).isFalse();
		context.consumeChar();
		assertThat(expression.parse(context)).isFalse();
		context.consumeChar();
		assertThat(expression.parse(context)).isTrue();
	}
}
