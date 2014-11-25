package net.netnook.qpeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions._util.MatcherTestBase;

public class StringMatcherTest extends MatcherTestBase {

	@Before
	public void init() {
		buildContext("-one-two-three-").consumeChar();
	}

	@Test
	public void test_one() {
		ParsingExpression expression = StringMatcher.of("one") //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("one");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_build_grammar() {
		assertThat(StringMatcher.of("one") //
				.build()//
				.buildGrammar()//
		).isEqualTo("'one'");
		assertThat(StringMatcher.of("on'e") //
				.build()//
				.buildGrammar()//
		).isEqualTo("'on\\'e'");
	}
}
