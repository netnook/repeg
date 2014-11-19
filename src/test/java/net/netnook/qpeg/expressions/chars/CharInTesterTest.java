package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharInTesterTest {

	@Test
	public void test_matching() {
		CharInTester tester = new CharInTester("bcd");
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch('b')).isTrue();
		assertThat(tester.isMatch('c')).isTrue();
		assertThat(tester.isMatch('d')).isTrue();
		assertThat(tester.isMatch('e')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharInTester("bcd").buildGrammar()).isEqualTo("[bcd]");
		assertThat(new CharInTester("*-").buildGrammar()).isEqualTo("[*\\-]");
		assertThat(new CharInTester("\t-").buildGrammar()).isEqualTo("[\\t\\-]");
		assertThat(new CharInTester("^x").buildGrammar()).isEqualTo("[\\^x]");
	}

}