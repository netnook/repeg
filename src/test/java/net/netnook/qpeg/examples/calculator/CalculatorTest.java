package net.netnook.qpeg.examples.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.netnook.qpeg.util.GrammarBuilder;
import net.netnook.qpeg.expressions.NoMatchException;
import net.netnook.qpeg.expressions.ParsingRule;

public class CalculatorTest {

	private ParsingRule rule;

	@Before
	public void init() {
		rule = new ParserFactory().build();
	}

	@Test
	public void test_1() throws NoMatchException {
		test("1 + 2", 3);
	}

	@Test
	public void test_2() throws NoMatchException {
		test("12 * 4", 48);
	}

	@Test
	public void test_3() throws NoMatchException {
		test("(1 + 2) * 3", 9);
	}

	@Test
	public void test_4() throws NoMatchException {
		test("1 + (2 * 3)", 7);
	}

	@Test
	public void test_5() throws NoMatchException {
		test("1 + 2 * 3", 7);
	}

	@Test
	public void test_6() throws NoMatchException {
		test("1 * 2 + 3", 5);
	}

	@Test
	public void test_7() throws NoMatchException {
		test("1 * 2 + 3", 5);
	}

	@Test
	public void test_spacing() throws NoMatchException {
		test("(1 + 2) * 3", 9);
		test("(1 +2) * 3", 9);
		test("(1+ 2) * 3", 9);
		test("(1 + 2)* 3", 9);
		test("(1 + 2) *3", 9);
		test("( 1 + 2) * 3 ", 9);
		test("(1 + 2 ) * 3 ", 9);
		test(" (1 + 2) * 3", 9);
		test("(1 + 2) * 3 ", 9);
		test("(1+2)*3", 9);
	}

	private void test(String input, int expected) throws NoMatchException {
		int result = rule.parse(input);
		assertThat(result).isEqualTo(expected);
	}

	@Test
	@Ignore
	public void performance() throws NoMatchException {
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				rule.parse("(1 + 2) * 3");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (endTime - startTime) + " millis");
		}
		System.out.println("#############################################");
	}

	@Test
	@Ignore
	public void printGrammar() {
		String grammar = GrammarBuilder.buildGrammar(rule);
		System.out.println(grammar);
	}
}
