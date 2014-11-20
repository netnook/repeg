package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class EoiMatcherTest extends BaseMatcherTest {

	@Before
	public void init() {
		buildContext("ab");
	}

	@Test
	public void test_grammar() {
		assertThat(EoiMatcher.instance().build(buildContext).buildGrammar()).isEqualTo("EOI");
	}

	@Test
	public void test_eoi() {
		ParsingExpression expression = EoiMatcher.instance().build(buildContext);
		assertThat(expression.parse(context)).isFalse();
		context.consumeChar();
		assertThat(expression.parse(context)).isFalse();
		context.consumeChar();
		assertThat(expression.parse(context)).isTrue();
	}

	@Test
	public void test_ignore_unsupported() {
		thrown.expect(UnsupportedOperationException.class);
		EoiMatcher.instance().ignore();
	}

	@Test
	public void test_onSuccess_unsupported() {
		thrown.expect(UnsupportedOperationException.class);
		EoiMatcher.instance().onSuccess(null);
	}
}
