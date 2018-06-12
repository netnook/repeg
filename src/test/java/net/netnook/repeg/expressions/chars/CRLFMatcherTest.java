package net.netnook.repeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CRLFMatcherTest {

	@Test
	public void test_matching() {
		CRLFMatcher tester = CRLFMatcher.INSTANCE;
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch(' ')).isFalse();
		assertThat(tester.isMatch('\t')).isFalse();
		assertThat(tester.isMatch('\n')).isTrue();
		assertThat(tester.isMatch('\r')).isTrue();
	}

	@Test
	public void test_grammar() {
		assertThat(CRLFMatcher.INSTANCE.buildGrammar()).isEqualTo("[\\n\\r]");
	}

}