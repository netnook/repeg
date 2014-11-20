package net.netnook.qpeg.expressions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class BaseMatcherTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	protected Context context;
	protected BuildContext buildContext;

	protected int successCount;
	protected OnSuccessHandler onSuccessCounter = context -> {
		successCount++;
	};

	protected void assertStackContains(Object... o) {
		assertThat(context.getAll()).containsExactly(o);
	}

	protected void assertFullStackContains(Object... o) {
		assertThat(context.getFullStack()).containsExactly(o);
	}

	protected void assertPositionIs(int position) {
		assertThat(context.position()).isEqualTo(position);
	}
}
