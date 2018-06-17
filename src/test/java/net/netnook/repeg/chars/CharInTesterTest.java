package net.netnook.repeg.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharInTesterTest {

	@Test
	public void test_matching() {
		CharInMatcher tester = new CharInMatcher("bcd");
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch('b')).isTrue();
		assertThat(tester.isMatch('c')).isTrue();
		assertThat(tester.isMatch('d')).isTrue();
		assertThat(tester.isMatch('e')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharInMatcher("bcd").buildGrammar()).isEqualTo("[bcd]");
		assertThat(new CharInMatcher("*-").buildGrammar()).isEqualTo("[*\\-]");
		assertThat(new CharInMatcher("\t-").buildGrammar()).isEqualTo("[\\t\\-]");
		assertThat(new CharInMatcher("^x").buildGrammar()).isEqualTo("[\\^x]");
	}

}