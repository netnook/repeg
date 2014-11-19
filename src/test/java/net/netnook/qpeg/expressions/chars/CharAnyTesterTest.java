package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.netnook.qpeg.expressions.Context;

public class CharAnyTesterTest {

	@Test
	public void test_matching() {
		CharAnyTester tester = CharAnyTester.INSTANCE;
		assertThat(tester.isMatch('a')).isTrue();
		assertThat(tester.isMatch(' ')).isTrue();
		assertThat(tester.isMatch('\t')).isTrue();
		assertThat(tester.isMatch('c')).isTrue();
		assertThat(tester.isMatch(Context.END_OF_INPUT)).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(CharAnyTester.INSTANCE.buildGrammar()).isEqualTo("[.]");
	}
}