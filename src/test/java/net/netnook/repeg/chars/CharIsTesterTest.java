package net.netnook.repeg.chars;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharIsTesterTest {

	@Test
	public void test_matching() {
		CharIsMatcher tester = new CharIsMatcher('b');
		assertThat(tester.isMatch('a')).isFalse();
		assertThat(tester.isMatch('b')).isTrue();
		assertThat(tester.isMatch('c')).isFalse();
	}

	@Test
	public void test_grammar() {
		assertThat(new CharIsMatcher('b').buildGrammar()).isEqualTo("[b]");
		assertThat(new CharIsMatcher('^').buildGrammar()).isEqualTo("[\\^]");
		assertThat(new CharIsMatcher('\t').buildGrammar()).isEqualTo("[\\t]");
	}
}