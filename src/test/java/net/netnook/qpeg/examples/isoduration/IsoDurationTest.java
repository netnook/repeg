package net.netnook.qpeg.examples.isoduration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Period;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import net.netnook.qpeg.expressions.GrammarBuilder;
import net.netnook.qpeg.expressions.NoMatchException;
import net.netnook.qpeg.expressions.ParsingRule;

public class IsoDurationTest {

	private ParsingRule rule;

	@Before
	public void init() {
		rule = new ParserFactory().build();
	}

	@Test
	public void test_1() throws NoMatchException {
		test("P1Y", isoDuration(1, 0, 0, 0, 0, 0));
	}

	@Test
	public void test_2() throws NoMatchException {
		test("P2M", isoDuration(0, 2, 0, 0, 0, 0));
	}

	@Test
	public void test_3() throws NoMatchException {
		test("P3D", isoDuration(0, 0, 3, 0, 0, 0));
	}

	@Test
	public void test_4() throws NoMatchException {
		test("PT4H", isoDuration(0, 0, 0, 4, 0, 0));
	}

	@Test
	public void test_5() throws NoMatchException {
		test("PT5M", isoDuration(0, 0, 0, 0, 5, 0));
	}

	@Test
	public void test_6() throws NoMatchException {
		test("PT6S", isoDuration(0, 0, 0, 0, 0, 6));
	}

	@Test
	public void test_7() throws NoMatchException {
		test("P1Y2M3D", isoDuration(1, 2, 3, 0, 0, 0));
	}

	@Test
	public void test_8() throws NoMatchException {
		test("PT4H5M6S", isoDuration(0, 0, 0, 4, 5, 6));
	}

	@Test
	public void test_9() throws NoMatchException {
		test("P1Y2M3DT4H5M6S", isoDuration(1, 2, 3, 4, 5, 6));
	}

	private void test(String input, IsoDuration expected) throws NoMatchException {
		IsoDuration result = rule.parse(input);
		assertThat(result).isEqualTo(expected);
	}

	private IsoDuration isoDuration(int years, int months, int days, int hours, int minutes, int seconds) {
		Period period = Period.of(years, months, days);
		Duration duration = Duration.ofSeconds((hours * 3600) + (minutes * 60) + seconds);
		return IsoDuration.of(period, duration);
	}

	@Test
	@Ignore
	public void performance() throws NoMatchException {
		System.out.println("#############################################");
		for (int round = 0; round < 10; round++) {
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				rule.parse("123456");
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
