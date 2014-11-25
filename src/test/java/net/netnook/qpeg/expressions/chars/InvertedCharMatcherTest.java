package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class InvertedCharMatcherTest {

	@Test
	public void test_matching() {
		CharMatcher tester = new CharIsMatcher('b').invert();
		assertThat(tester.isMatch('a')).isTrue();
		assertThat(tester.isMatch('b')).isFalse();
		assertThat(tester.isMatch('c')).isTrue();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharIsMatcher('b').invert().buildGrammar()).isEqualTo("[^b]");
		assertThat(new CharIsMatcher('^').invert().buildGrammar()).isEqualTo("[^\\^]");
		assertThat(new CharIsMatcher('\t').invert().buildGrammar()).isEqualTo("[^\\t]");
	}

}