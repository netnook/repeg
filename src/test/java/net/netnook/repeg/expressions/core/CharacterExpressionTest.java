package net.netnook.repeg.expressions.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.netnook.repeg.expressions.InvalidExpressionException;
import net.netnook.repeg.expressions.OnSuccessHandler;
import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions._util.MatcherTestBase;
import net.netnook.repeg.expressions.chars.CharMatcher;

public class CharacterExpressionTest extends MatcherTestBase {

	@Before
	public void init() {
		buildContext("-abcdefgh-").consumeChar();
	}

	@Test
	public void test_one_char() {
		ParsingExpression expression = CharacterExpression.using(CharMatcher.inRange('a', 'f')) //
				.onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("b");
		assertPositionIs(3);
	}

	@Test
	public void test_four_chars() {
		ParsingExpression expression = CharacterExpression.using(CharMatcher.inRange('a', 'f')) //
				.minCount(2) //
				.maxCount(4) //
				.onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("abcd");
		assertPositionIs(5);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("ef");
		assertPositionIs(7);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_max_chars() {
		ParsingExpression expression = CharacterExpression.using(CharMatcher.inRange('a', 'f')) //
				.minCount(2) //
				.maxUnbounded() //
				.onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("abcdef");
		assertPositionIs(7);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
	}

	@Test
	public void test_no_match_eoi() {
		buildContext("-");

		ParsingExpression expression = CharacterExpression.using(CharMatcher.any()) //
				.onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("-");
		assertPositionIs(1);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
		assertPositionIs(1);

		assertThat(expression.parse(context)).isFalse();
		assertNewOnStack();
		assertPositionIs(1);
	}

	@Test
	public void test_build_grammar() {
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.build()//
				.buildGrammar()//
		).isEqualTo("[.]");
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.minCount(0).maxCount(1).build()//
				.buildGrammar()//
		).isEqualTo("[.]?");
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.minCount(0).maxUnbounded().build()//
				.buildGrammar()//
		).isEqualTo("[.]*");
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.minCount(1).maxUnbounded().build()//
				.buildGrammar()//
		).isEqualTo("[.]+");
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.minCount(7).maxCount(7).build()//
				.buildGrammar()//
		).isEqualTo("[.]{7}");
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.minCount(2).maxCount(7).build()//
				.buildGrammar()//
		).isEqualTo("[.]{2,7}");
		assertThat(CharacterExpression.using(CharMatcher.any()) //
				.minCount(2).maxUnbounded().build()//
				.buildGrammar()//
		).isEqualTo("[.]{2,}");
	}

	@Test
	public void test_min_max_count() {
		CharacterExpression expression = (CharacterExpression) CharacterExpression.using(CharMatcher.any()).minCount(7).maxCount(10).build();
		assertThat(expression.getMinCount()).isEqualTo(7);
		assertThat(expression.getMaxCount()).isEqualTo(10);
	}

	@Test
	public void test_zeroOrMore() {
		CharacterExpression expression = (CharacterExpression) CharacterExpression.using(CharMatcher.any()).zeroOrMore().build();
		assertThat(expression.getMinCount()).isEqualTo(0);
		assertThat(expression.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	public void test_oneOrMore() {
		CharacterExpression expression = (CharacterExpression) CharacterExpression.using(CharMatcher.any()).oneOrMore().build();
		assertThat(expression.getMinCount()).isEqualTo(1);
		assertThat(expression.getMaxCount()).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	public void test_count() {
		CharacterExpression expression = (CharacterExpression) CharacterExpression.using(CharMatcher.any()).count(42).build();
		assertThat(expression.getMinCount()).isEqualTo(42);
		assertThat(expression.getMaxCount()).isEqualTo(42);
	}

	@Test
	public void invalid_bounds_min_greater_than_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: minCount > maxCount");
		CharacterExpression.using(CharMatcher.any()).minCount(7).maxCount(3).build();
	}

	@Test
	public void invalid_bounds_negative_min() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: min count");
		CharacterExpression.using(CharMatcher.any()).minCount(-3);
	}

	@Test
	public void invalid_bounds_negative_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: max count");
		CharacterExpression.using(CharMatcher.any()).maxCount(-5);
	}

	@Test
	public void invalid_bounds_negative_count() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: count");
		CharacterExpression.using(CharMatcher.any()).count(-3);
	}

	@Test
	public void test_eoi() {
		ParsingExpression expression = CharacterExpression.using(CharMatcher.any()) //
				.onSuccess(OnSuccessHandler.PUSH_TEXT_AS_STRING) //
				.build();

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("a");
		assertPositionIs(2);

		assertThat(expression.parse(context)).isTrue();
		assertNewOnStack("b");
		assertPositionIs(3);
	}
}
