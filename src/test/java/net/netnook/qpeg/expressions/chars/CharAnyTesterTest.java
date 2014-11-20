package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharAnyTesterTest {

	@Test
	public void test_matching() {
		CharAnyTester tester = CharAnyTester.INSTANCE;
		assertThat(tester.isMatch('a')).isTrue();
		assertThat(tester.isMatch(' ')).isTrue();
		assertThat(tester.isMatch('\t')).isTrue();
		assertThat(tester.isMatch('c')).isTrue();
	}

	@Test
	public void test_grammar() {
		assertThat(CharAnyTester.INSTANCE.buildGrammar()).isEqualTo("[.]");
	}
}