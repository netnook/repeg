package net.netnook.repeg.expressions._util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import net.netnook.repeg.Context;
import net.netnook.repeg.Expression;
import net.netnook.repeg.OnSuccessHandler;
import net.netnook.repeg.ParseListener;
import net.netnook.repeg.expressions.RootContext;

public abstract class MatcherTestBase {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final class TestParseListener implements ParseListener {

		@Override
		public void onExpressionEnter(Expression expression, Context context) {
			// no-op
		}

		@Override
		public void onExpressionExit(Expression expression, Context context, boolean success) {
			newOnStack.clear();
			newOnStack.addAll(context.getAll());
		}
	}

	private final List<Object> newOnStack = new ArrayList<>();
	private final TestParseListener testParseListener = new TestParseListener();
	protected RootContext context;
	protected int successCount;

	protected RootContext buildContext(String input) {
		context = new RootContext(input, testParseListener);
		return context;
	}

	protected OnSuccessHandler onSuccessCounter = context -> {
		successCount++;
	};

	protected void assertNewOnStack(String... o) {
		assertStackContains(newOnStack, Arrays.asList(o));
	}

	protected void assertFullStackContains(Object... o) {
		assertStackContains(context.getStack(), Arrays.asList(o));
	}

	protected <T> T getNewOnStack(int i) {
		return (T) newOnStack.get(i);
	}

	private void assertStackContains(List<Object> stack, List<Object> expecteds) {
		assertThat(stack).hasSize(expecteds.size());
		for (int i = 0; i < expecteds.size(); i++) {
			Object actual = stack.get(i);
			Object expected = expecteds.get(i);
			assertThat(actual) //
					.isEqualTo(expected) //
					.overridingErrorMessage("Expected '" + expected + "' but found '" + actual + "' at index " + i);
		}
	}

	protected void assertPositionIs(int position) {
		assertThat(context.position()).isEqualTo(position);
	}
}
