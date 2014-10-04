package net.netnook.qpeg.expressions;

import java.util.ArrayList;
import java.util.List;

public class Context {

	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;

	public static class Marker {
		private final int position;
		private final int stackSize;

		private Marker(int position, int stackSize) {
			this.position = position;
			this.stackSize = stackSize;
		}
	}

	private final CharSequence input;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);
	private int position;

	private Marker mark;

	public Context(CharSequence input) {
		this.input = input;
		this.position = 0;
	}

	public Marker mark() {
		mark = new Marker(position, stack.size());
		return mark;
	}

	public void mark(Marker marker) {
		this.mark = marker;
	}

	public void resetTo(Marker marker) {
		resetPositionTo(marker);
		resetStackTo(marker);
		mark = null;
	}

	/**
	 * Mark relative operation
	 *
	 * @return
	 */
	public CharSequence getCurrentText() {
		return getInputFrom(mark);
	}

	private CharSequence getInputFrom(Marker start) {
		return input.subSequence(start.position, position);
	}

	/**
	 * Mark relative operation
	 *
	 * @return
	 */
	public int inputLength() {
		return inputLengthFrom(mark);
	}

	private int inputLengthFrom(Marker marker) {
		int result = position - marker.position;
		if (result < 0) {
			throw new IllegalArgumentException("Marker position is after current position.");
		}
		return result;
	}

	public char consumeChar() {
		if (position < input.length()) {
			char c = input.charAt(position);
			position++;
			return c;
		} else {
			position++;
			return Constant.EOICHAR;
		}
	}

	public char peekChar() {
		if (position < input.length()) {
			return input.charAt(position);
		} else {
			return Constant.EOICHAR;
		}
	}

	public void incrementPosition() {
		position++;
	}

	public void rewindPosition(int count) {
		position -= count;
		if (position < 0 || position < mark.position) {
			throw new IllegalArgumentException("Cannot rewind beyond start !");
		}
	}

	public void resetPositionTo(Marker marker) {
		this.position = marker.position;
	}

	/**
	 * Mark relative operation
	 *
	 * @return
	 */
	public int size() {
		return sizeFrom(mark);
	}

	private int sizeFrom(Marker marker) {
		return stack.size() - marker.stackSize;
	}

	/**
	 * Mark relative operation
	 */
	public void pushCurrentText() {
		push(getCurrentText());
	}

	public void push(Object o) {
		stack.add(o);
	}

	/**
	 * Mark relative operation
	 */
	public <T> T pop() {
		if (size() <= 0) {
			throw new IllegalStateException("Cannot pop beyond current mark");
		}
		return (T) stack.remove(stack.size() - 1);
	}

	/**
	 * Mark relative operation
	 */
	public <T> T get(int offset) {
		return (T) stack.get(mark.stackSize + offset);
	}

	/**
	 * Mark relative operation
	 */
	public void replaceWith(Object value) {
		clear();
		push(value);
	}

	/**
	 * Mark relative operation
	 */
	public void clear() {
		resetStackTo(mark);
	}

	private void resetStackTo(Marker marker) {
		truncateStack(marker.stackSize);
	}

	// FIXME: better way to truncate stack ??
	private void truncateStack(int toSize) {
		for (int idx = stack.size() - 1; idx >= toSize; idx--) {
			stack.remove(idx);
		}
	}

	/**
	 * Mark relative operation
	 */
	public <T> List<T> getAll() {
		int count = size();
		List<T> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(get(i));
		}
		return result;
	}
}