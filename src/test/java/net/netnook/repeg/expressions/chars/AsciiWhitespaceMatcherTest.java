package net.netnook.repeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AsciiWhitespaceMatcherTest {

	@Test
	public void test_matching() {
		AsciiWhitespaceMatcher tester = AsciiWhitespaceMatcher.INSTANCE;
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch(' ')).isTrue();
		assertThat(tester.isMatch('\t')).isTrue();
		assertThat(tester.isMatch('c')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(AsciiWhitespaceMatcher.INSTANCE.buildGrammar()).isEqualTo("[\\s]");
	}
}
