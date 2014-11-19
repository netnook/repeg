package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharIsWhitespaceTesterTest {

	@Test
	public void test_matching() {
		CharIsWhitespaceTester tester = CharIsWhitespaceTester.INSTANCE;
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch(' ')).isTrue();
		assertThat(tester.isMatch('\t')).isTrue();
		assertThat(tester.isMatch('c')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(CharIsWhitespaceTester.INSTANCE.buildGrammar()).isEqualTo("[\\s]");
	}
}