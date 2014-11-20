package net.netnook.qpeg.expressions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.netnook.qpeg.util.ParseListener;

public class Context {

	public static final int END_OF_INPUT = -1;

	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;

	public static class Marker {
		private final int position;
		private final int stackSize;

		private Marker(int position, int stackSize) {
			this.position = position;
			this.stackSize = stackSize;
		}

		public int position() {
			return position;
		}
	}

	private final CharSequence input;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);

	private int position;
	private Marker mark;
	private ParseListener listener = ParseListener.NO_OP;

	public Context(CharSequence input) {
		this.input = input;
		this.position = 0;
	}

	public int position() {
		return position;
	}

	public void setListener(ParseListener listener) {
		this.listener = listener;
	}

	public ParseListener getListener() {
		return listener;
	}

	public Marker updateMark() {
		mark = buildMarker();
		return mark;
	}

	public Marker buildMarker() {
		return new Marker(position, stack.size());
	}

	public Marker mark() {
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

	public CharSequence getInput(int start, int end) {
		return input.subSequence(start, end);
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

	// for testing only
	List<Object> getFullStack() {
		return Collections.unmodifiableList(stack);
	}
}
