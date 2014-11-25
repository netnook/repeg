package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

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
		ParsingExpression expression = EndOfInput.instance().build();
		assertThat(expression.parse(context)).isFalse();
		context.consumeChar();
		assertThat(expression.parse(context)).isFalse();
		context.consumeChar();
		assertThat(expression.parse(context)).isTrue();
	}

	@Test
	public void test_onSuccess_unsupported() {
		thrown.expect(UnsupportedOperationException.class);
		EndOfInput.instance().onSuccess(null);
	}
}
