package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class InvertedCharTesterTest {

	@Test
	public void test_matching() {
		CharTester tester = new CharIsTester('b').invert();
		assertThat(tester.isMatch('a')).isTrue();
		assertThat(tester.isMatch('b')).isFalse();
		assertThat(tester.isMatch('c')).isTrue();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharIsTester('b').invert().buildGrammar()).isEqualTo("[^b]");
		assertThat(new CharIsTester('^').invert().buildGrammar()).isEqualTo("[^\\^]");
		assertThat(new CharIsTester('\t').invert().buildGrammar()).isEqualTo("[^\\t]");
	}

}