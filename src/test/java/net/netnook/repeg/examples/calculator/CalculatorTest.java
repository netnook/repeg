package net.netnook.repeg.examples.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.netnook.repeg.Parser;
import net.netnook.repeg.parsetree.ParseTreeBuilder;
import net.netnook.repeg.util.GrammarBuilder;
import net.netnook.repeg.util.LoggingParseListener;

public class CalculatorTest {

	private Parser<Integer> parser;

	@Before
	public void init() {
		parser = new ParserFactory().build();
	}

	@Test
	public void test_1() {
		test("1 + 2", 3);
	}

	@Test
	public void test_2() {
		test("12 * 4", 48);
	}

	@Test
	public void test_3() {
		test("(1 + 2) * 3", 9);
	}

	@Test
	public void test_4() {
		test("1 + (2 * 3)", 7);
	}

	@Test
	public void test_5() {
		test("1 + 2 * 3", 7);
	}

	@Test
	public void test_6() {
		test("1 * 2 + 3", 5);
	}

	@Test
	public void test_7() {
		test("1 * 2 + 3", 5);
	}

	@Test
	public void test_spacing() {
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

	private void test(String input, int expected) {
		int result = parser.parse(input);
		assertThat(result).isEqualTo(expected);
	}

	@Test
	@Ignore
	public void parseTree() {
		ParseTreeBuilder parseTreeBuilder = new ParseTreeBuilder();
		int result = parser.parse("11 * 2 + 333", parseTreeBuilder);
		assertThat(result).isEqualTo(355);
		System.out.println("tree: " + parseTreeBuilder.getParseTree());
	}

	@Test
	@Ignore
	public void parseWithLogging() {
		parser.parse("1 * 2 + 3", LoggingParseListener.builder().build());
	}

	@Test
	@Ignore
	public void performance() {
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				parser.parse("(1 + 2) * 3");
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (endTime - startTime) + " millis");
		}
		System.out.println("#############################################");
	}

	@Test
	@Ignore
	public void printGrammar() {
		String grammar = GrammarBuilder.buildGrammar(parser);
		System.out.println(grammar);
	}
}
