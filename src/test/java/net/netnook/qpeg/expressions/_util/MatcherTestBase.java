package net.netnook.qpeg.expressions._util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import net.netnook.qpeg.expressions.OnSuccessHandler;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.expressions.RootContext;
import net.netnook.qpeg.util.ParseListener;

public abstract class MatcherTestBase {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final class TestParseListener implements ParseListener {

		@Override
		public void onExpressionEnter(ParsingExpression expression, RootContext context) {
			// no-op
		}

		@Override
		public void onExpressionExit(ParsingExpression expression, RootContext context, int startPosition, int startStackIdx, boolean success) {
			newOnStack.clear();
			newOnStack.addAll(context.getStack(startStackIdx));
		}
	}

	private final List<Object> newOnStack = new ArrayList<>();
	private final TestParseListener testParseListener = new TestParseListener();
	protected RootContext context;
	//protected BuildContext buildContext = new BuildContext();
	protected int successCount;

	protected RootContext buildContext(String input) {
		context = new RootContext(input, testParseListener);
		return context;
	}

	protected OnSuccessHandler onSuccessCounter = context -> {
		successCount++;
	};

	protected void assertNewOnStack(Object... o) {
		assertThat(newOnStack).containsExactly(o);
	}

	protected <T> T getNewOnStack(int i) {
		return (T) newOnStack.get(i);
	}

	protected void assertFullStackContains(Object... o) {
		assertThat(context.getStack()).containsExactly(o);
	}

	protected void assertPositionIs(int position) {
		assertThat(context.position()).isEqualTo(position);
	}
}
