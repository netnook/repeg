package net.netnook.qpeg.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.util.ParseListener;

public final class RootContext {

	public static final int END_OF_INPUT = -1;
	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;

	private final Context slice = new Context(this);
	private final CharSequence input;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);
	private final ParseListener listener;

	private int position;

	public RootContext(CharSequence input, ParseListener listener) {
		this.input = input;
		this.position = 0;
		this.listener = (listener == null) ? ParseListener.NO_OP : listener;
	}

	public ParseListener getListener() {
		return listener;
	}

	Context slice(int inputOffset, int stackOffset) {
		slice.setup(inputOffset, stackOffset);
		return slice;
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

	void push(Object o) {
		stack.add(o);
	}

	<T> T get(int index) {
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

	public List<Object> getStack() {
		return Collections.unmodifiableList(stack);
	}

	// FIXME: better way to truncate stack ??
	void truncateStack(int toSize) {
		for (int idx = stack.size() - 1; idx >= toSize; idx--) {
			stack.remove(idx);
		}
	}

	public void resetTo(int position, int stackSize) {
		this.position = position;
		truncateStack(stackSize);
	}
}
