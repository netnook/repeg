package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CharMatcherTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Context context;
	private BuildContext buildContext;

	@Before
	public void init() {
		context = new Context("-abcdefgh-");
		context.consumeChar();
		buildContext = new BuildContext();
	}

	@Test
	public void test_any() {
		assertThat(CharMatcher.any().build(buildContext).buildGrammar()).isEqualTo("[.]");
	}

	@Test
	public void test_char_range() {
		assertThat(CharMatcher.inRange('a','c').build(buildContext).buildGrammar()).isEqualTo("[a-c]");
	}

	@Test
	public void test_whitespace() {
		assertThat(CharMatcher.whitespace().build(buildContext).buildGrammar()).isEqualTo("[\\s]");
	}

	@Test
	public void test_exact() {
		assertThat(CharMatcher.is('x').build(buildContext).buildGrammar()).isEqualTo("[x]");
	}

	@Test
	public void test_in() {
		assertThat(CharMatcher.in("acxz").build(buildContext).buildGrammar()).isEqualTo("[acxz]");
	}

	@Test
	public void test_one_char() {
		ParsingExpression expression = CharMatcher.inRange('a', 'f') //
				.build(buildContext);
		expression.parse(context);
		assertStackContains("a");
		assertPositionIs(2);
	}

	@Test
	public void test_four_chars() {
		ParsingExpression expression = CharMatcher.inRange('a', 'f') //
				.minCount(2) //
				.maxCount(4) //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("abcd");
		assertPositionIs(5);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("ef");
		assertPositionIs(7);

		assertThat(expression.parse(context)).isFalse();
		assertStackContains();
		assertPositionIs(7);
	}

	@Test
	public void test_max_chars() {
		ParsingExpression expression = CharMatcher.inRange('a', 'f') //
				.minCount(2) //
				.maxCountUnbounded() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("abcdef");
		assertPositionIs(7);

		assertThat(expression.parse(context)).isFalse();
		assertStackContains();
		assertPositionIs(7);
	}

	@Test
	public void test_invert() {
		ParsingExpression expression = CharMatcher.inRange('c', 'e') //
				.maxCountUnbounded() //
				.invert() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains("ab");
		assertPositionIs(3);

		assertThat(expression.parse(context)).isFalse();
		assertStackContains();
		assertPositionIs(3);
	}

	@Test
	public void test_ignore() {
		ParsingExpression expression = CharMatcher.inRange('a', 'f') //
				.maxCount(4) //
				.ignore() //
				.build(buildContext);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(5);

		assertThat(expression.parse(context)).isTrue();
		assertStackContains();
		assertPositionIs(7);

		assertThat(expression.parse(context)).isFalse();
		assertStackContains();
		assertPositionIs(7);
	}

	@Test
	public void test_build_grammar() {
		assertThat(CharMatcher.inRange('a', 'f') //
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]");
		assertThat(CharMatcher.inRange('a', 'f') //
				.minCount(0)
				.maxCount(1)
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]?");
		assertThat(CharMatcher.inRange('a', 'f') //
				.minCount(0)
				.maxCountUnbounded()
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]*");
		assertThat(CharMatcher.inRange('a', 'f') //
				.minCount(1)
				.maxCountUnbounded()
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]+");
		assertThat(CharMatcher.inRange('a', 'f') //
				.minCount(7)
				.maxCount(7)
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]{7}");
		assertThat(CharMatcher.inRange('a', 'f') //
				.minCount(2)
				.maxCount(7)
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]{2,7}");
		assertThat(CharMatcher.inRange('a', 'f') //
				.minCount(2)
				.maxCountUnbounded()
				.build(buildContext)//
				.buildGrammar()//
		).isEqualTo("[a-f]{2,}");
	}

	@Test
	public void invalid_bounds_min_greater_than_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: minCount > maxCount");
		CharMatcher.any().minCount(7).maxCount(3).build(buildContext);
	}

	@Test
	public void invalid_bounds_negative_min() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: min count");
		CharMatcher.any().minCount(-3);
	}

	@Test
	public void invalid_bounds_negative_max() {
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Invalid expression: max count");
		CharMatcher.any().maxCount(-5);
	}

	private void assertStackContains(Object... o) {
		assertThat(context.getAll()).containsExactly(o);
	}

	private void assertPositionIs(int position) {
		assertThat(context.position()).isEqualTo(position);
	}
}
