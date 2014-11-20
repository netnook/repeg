package net.netnook.qpeg.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.util.ParseListener;

public class RootContext {

	public static final int END_OF_INPUT = -1;
	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;

	private final Context slice = new Context(this);
	private final CharSequence input;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);

	private int position;
	private ParseListener listener = ParseListener.NO_OP;

	public RootContext(CharSequence input) {
		this.input = input;
		this.position = 0;
	}

	public Context slice(int inputOffset, int stackOffset) {
		slice.setup(inputOffset, stackOffset);
		return slice;
	}

	public void setListener(ParseListener listener) {
		this.listener = listener;
	}

	public ParseListener getListener() {
		return listener;
	}

	public int position() {
		return position;
	}

	public CharSequence getInput(int start) {
		return input.subSequence(start, position);
	}

	public CharSequence getInput(int start, int end) {
		return input.subSequence(start, end);
	}

	public int consumeChar() {
		if (position < input.length()) {
			char c = input.charAt(position);
			position++;
			return c;
		} else {
			position++;
			return END_OF_INPUT;
		}
	}

	public int peekChar() {
		if (position < input.length()) {
			return input.charAt(position);
		} else {
			return END_OF_INPUT;
		}
	}

	public void rewindInput() {
		position--;
		if (position < 0) {
			throw new IllegalArgumentException("Cannot rewind beyond start !");
		}
	}

	public int stackSize() {
		return stack.size();
	}

	public void push(Object o) {
		stack.add(o);
	}

	public <T> T get(int index) {
		return (T) stack.get(index);
	}

	// FIXME: naming
	public Collection<?> getStack(int startIdx) {
		int len = stack.size() - startIdx;
		if (len == 0) {
			return Collections.emptyList();
		} else if (len == 1) {
			return Collections.singletonList(stack.get(startIdx));
		} else {
			List<Object> results = new ArrayList<>(len);
			for (int i = startIdx; i < stack.size(); i++) {
				results.add(stack.get(i));
			}
			return results;
		}
	}

	// FIXME: better way to truncate stack ??
	void truncateStack(int toSize) {
		for (int idx = stack.size() - 1; idx >= toSize; idx--) {
			stack.remove(idx);
		}
	}

	List<Object> getStack() {
		return Collections.unmodifiableList(stack);
	}

	public void resetTo(int position, int stackSize) {
		this.position = position;
		truncateStack(stackSize);
	}
}
