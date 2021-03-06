package net.netnook.repeg.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CharInRangeTesterTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_invalid_range() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("character-in-range test with to < from");
		new CharInRangeMatcher('a', '-');
	}

	@Test
	public void test_matching() {
		CharInRangeMatcher tester = new CharInRangeMatcher('b', 'd');
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch('b')).isTrue();
		assertThat(tester.isMatch('c')).isTrue();
		assertThat(tester.isMatch('d')).isTrue();
		assertThat(tester.isMatch('e')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharInRangeMatcher('b', 'd').buildGrammar()).isEqualTo("[b-d]");
		assertThat(new CharInRangeMatcher('*', '-').buildGrammar()).isEqualTo("[*-\\-]");
		assertThat(new CharInRangeMatcher('\t', '-').buildGrammar()).isEqualTo("[\\t-\\-]");
		assertThat(new CharInRangeMatcher('^', 'x').buildGrammar()).isEqualTo("[\\^-x]");
	}
}