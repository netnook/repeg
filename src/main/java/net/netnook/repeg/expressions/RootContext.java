package net.netnook.repeg.expressions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.netnook.repeg.Context;
import net.netnook.repeg.ParseListener;

public final class RootContext {

	public static final int END_OF_INPUT = -1;
	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;

	private final ContextImpl slice = new ContextImpl(this);
	private final CharSequence input;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);
	private final ParseListener listener;

	private int position;

	public RootContext(CharSequence input, ParseListener listener) {
		this.input = input;
		this.position = 0;
		this.listener = listener;
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

	public void setPosition(int position) {
		this.position = position;
	}

	public int charAt(int position) {
		if (position < input.length()) {
			return input.charAt(position);
		} else {
			return END_OF_INPUT;
		}
	}

	public CharSequence getInput(int start) {
		return new CharSequenceImpl(start, position);
	}

	public CharSequence getInput(int start, int end) {
		return new CharSequenceImpl(start, end);
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

	public int stackSize() {
		return stack.size();
	}

	void push(Object o) {
		stack.add(o);
	}

	<T> T get(int index) {
		return (T) stack.get(index);
	}

	/**
	 * Visible for testing
	 *
	 * @param startIdx start index
	 * @return stack contents
	 */
	public Collection<?> getStackFrom(int startIdx) {
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

	// TODO: better way to truncate stack ??
	void truncateStack(int toSize) {
		for (int idx = stack.size() - 1; idx >= toSize; idx--) {
			stack.remove(idx);
		}
	}

	public void resetTo(int position, int stackSize) {
		this.position = position;
		truncateStack(stackSize);
	}

	private class CharSequenceImpl implements CharSequence {

		private final int start;
		private final int end;

		private CharSequenceImpl(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int length() {
			return end - start;
		}

		@Override
		public char charAt(int index) {
			return RootContext.this.input.charAt(start + index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return new CharSequenceImpl(this.start + start, this.start + end);
		}

		@Override
		public String toString() {
			return RootContext.this.input.subSequence(start, end).toString();
		}
	}
}
