package net.netnook.qpeg.impl;

import java.util.ArrayList;
import java.util.List;

public class Context {

	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;

	public static class Marker {
		public final int inputPosition;
		public final int stackPosition;

		private Marker(int inputPosition, int stackPosition) {
			this.inputPosition = inputPosition;
			this.stackPosition = stackPosition;
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

	public Marker marker() {
		return new Marker(position, stack.size());
	}

	public void reset(Marker marker) {
		position = marker.inputPosition;
		truncateStack(marker.stackPosition);
	}

	public void mark(Marker marker) {
		this.mark = marker;
	}

	public char nextChar() {
		if (position < input.length()) {
			char c = input.charAt(position);
			position++;
			return c;
		} else {
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

	public int incrementPosition() {
		position++;
		return position;
	}

	public int position() {
		return position;
	}

	//	public Marker lastStart() {
	//		return lastStart;
	//	}
	//
	//	public void lastStart(Marker lastStart) {
	//		this.lastStart = lastStart;
	//	}

	public void setPosition(int position) {
		this.position = position;
	}

	public CharSequence getInput(int start, int end) {
		return input.subSequence(start, end);
	}

	public CharSequence getInputFrom(Marker start) {
		return input.subSequence(start.inputPosition, position);
	}

	public CharSequence getInputFromMark() {
		return getInputFrom(mark);
	}

	public void pushText(Marker start) {
		push(getInputFrom(start));
	}

	public void push(Object o) {
		stack.add(o);
	}

	public <T> T pop() {
		return (T) stack.remove(stack.size() - 1);
	}

	public int stackPosition() {
		return stack.size();
	}

	public <T> T getFromMark(int offset) {
		return (T) stack.get(mark.stackPosition + offset);
	}

	public void replaceFromMark(Object value) {
		resetStackToMark();
		push(value);
	}

	public void resetStackToMark() {
		resetStack(mark);
	}

	public void resetStack(Marker marker) {
		truncateStack(marker.stackPosition);
	}

	public int stackSizeFromMark() {
		return stackPosition() - mark.stackPosition;
	}

	// FIXME: better way to truncate stack ??
	private void truncateStack(int toSize) {
		for (int idx = stack.size() - 1; idx >= toSize; idx--) {
			stack.remove(idx);
		}
	}

	public <T> List<T> getStack() {
		return new ArrayList(stack);
	}
}
