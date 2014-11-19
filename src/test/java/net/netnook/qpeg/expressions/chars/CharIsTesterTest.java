package net.netnook.qpeg.expressions.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharIsTesterTest {

	@Test
	public void test_matching() {
		CharIsTester tester = new CharIsTester('b');
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch('b')).isTrue();
		assertThat(tester.isMatch('c')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharIsTester('b').buildGrammar()).isEqualTo("[b]");
		assertThat(new CharIsTester('^').buildGrammar()).isEqualTo("[\\^]");
		assertThat(new CharIsTester('\t').buildGrammar()).isEqualTo("[\\t]");
	}
}