package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StringMatcherTest extends BaseMatcherTest {

	@Before
	public void init() {
		buildContext("-one-two-three-").consumeChar();
	}

	@Test
	public void test_one() {
		ParsingExpression expression = StringMatcher.of("one") //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("one");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_build_grammar() {
		assertThat(StringMatcher.of("one") //
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("'one'");
		assertThat(StringMatcher.of("on'e") //
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("'on\\'e'");
	}
}
