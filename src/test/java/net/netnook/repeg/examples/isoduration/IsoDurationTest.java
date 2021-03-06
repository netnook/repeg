package net.netnook.repeg.examples.isoduration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Period;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.netnook.repeg.Parser;
import net.netnook.repeg.parsetree.ParseTreeBuilder;
import net.netnook.repeg.util.GrammarBuilder;

public class IsoDurationTest {

	private Parser<IsoDuration> parser;

	@Before
	public void init() {
		parser = new ParserFactory().build();
	}

	@Test
	public void test_1() {
		test("P1Y", isoDuration(1, 0, 0, 0, 0, 0));
	}

	@Test
	public void test_2() {
		test("P2M", isoDuration(0, 2, 0, 0, 0, 0));
	}

	@Test
	public void test_3() {
		test("P3D", isoDuration(0, 0, 3, 0, 0, 0));
	}

	@Test
	public void test_4() {
		test("PT4H", isoDuration(0, 0, 0, 4, 0, 0));
	}

	@Test
	public void test_5() {
		test("PT5M", isoDuration(0, 0, 0, 0, 5, 0));
	}

	@Test
	public void test_6() {
		test("PT6S", isoDuration(0, 0, 0, 0, 0, 6));
	}

	@Test
	public void test_7() {
		test("P1Y2M3D", isoDuration(1, 2, 3, 0, 0, 0));
	}

	@Test
	public void test_8() {
		test("PT4H5M6S", isoDuration(0, 0, 0, 4, 5, 6));
	}

	@Test
	public void test_9() {
		test("P1Y2M3DT4H5M6S", isoDuration(1, 2, 3, 4, 5, 6));
	}

	private void test(String input, IsoDuration expected) {
		IsoDuration result = parser.parse(input);
		assertThat(result).isEqualTo(expected);
	}

	private IsoDuration isoDuration(int years, int months, int days, int hours, int minutes, int seconds) {
		Period period = Period.of(years, months, days);
		Duration duration = Duration.ofSeconds((hours * 3600) + (minutes * 60) + seconds);
		return IsoDuration.of(period, duration);
	}

	@Test
	@Ignore
	public void parseTree() {
		ParseTreeBuilder parseTreeBuilder = new ParseTreeBuilder();
		IsoDuration result = parser.parse("P1Y2M3DT4H5M6S", parseTreeBuilder);
		assertThat(result).isEqualTo(isoDuration(1, 2, 3, 4, 5, 6));
		System.out.println("tree: " + parseTreeBuilder.getParseTree());
	}

	@Test
	@Ignore
	public void performance() {
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				parser.parse("P1Y2M3DT4H5M6S");
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
