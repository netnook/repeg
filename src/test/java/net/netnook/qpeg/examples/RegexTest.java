package net.netnook.qpeg.examples;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RegexTest {

	@Test
	public void test1() {
		assertThat("bbb".matches("[a-b]+")).isTrue();
		assertThat("bbb".matches("[^a-b]+")).isFalse();
		assertThat("ddd".matches("[^a-bd-e]+")).isFalse();
		assertThat("^".matches("[a-b^d-e]+")).isTrue();
		assertThat("^".matches("[\\^]")).isTrue();
	}
}
