package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.expressions.Expression;
import net.netnook.repeg.expressions._util.MatcherTestBase;

public class StringExpressionTest extends MatcherTestBase {

	@Before
	public void init() {
		buildContext("-one-two-three-").consumeChar();
	}

	@Test
	public void test_one() {
		Expression expression = StringExpression.of("one") //
				.onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("one");
		assertPositionIs(4);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_build_grammar() {
		assertThat(StringExpression.of("one") //
				.build()//
				.buildGrammar()//
		).isEqualTo("'one'");
		assertThat(StringExpression.of("on'e") //
				.build()//
				.buildGrammar()//
		).isEqualTo("'on\\'e'");
	}
}
