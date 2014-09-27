package net.netnook.qpeg.parsetree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.netnook.qpeg.impl.Constant;

public class Context {

	public static class Marker {
		public final int inputPosition;
		public final int stackPosition;

		private Marker(int inputPosition, int stackPosition) {
			this.inputPosition = inputPosition;
			this.stackPosition = stackPosition;
		}
	}

	private final CharSequence input;
	private final LinkedList<Object> stack = new LinkedList<>();
	private int position;
	private Marker lastStart;

	public Context(CharSequence input) {
		this.input = input;
		this.position = 0;
	}

	public Marker marker() {
		return new Marker(position, stack.size());
	}

	public void reset(Marker marker) {
		position = marker.inputPosition;
		popTo(marker.stackPosition);
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

	public Marker lastStart() {
		return lastStart;
	}

	public void lastStart(Marker lastStart) {
		this.lastStart = lastStart;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public CharSequence getInput(int start, int end) {
		return input.subSequence(start, end);
	}

	public void push(Object o) {
		stack.addLast(o);
	}

	public <T> T pop() {
		return (T) stack.removeLast();
	}

	public int stackPosition() {
		return stack.size();
	}

	public void resetStack(int stackPosition) {
		int count = stack.size() - stackPosition;
		for (int remaining = count; remaining > 0; remaining--) {
			pop();
		}
	}

	// FIXME: maybe this better of as an array return,
	// FIXME: or even an array subset into stack rather than haveing to create list
	public <T> List<T> popTo(int stackPosition) {
		int count = stack.size() - stackPosition;
		if (count == 0) {
			return Collections.emptyList();
		} else if (count == 1) {
			return Collections.singletonList(pop());
		} else {
			LinkedList<T> result = new LinkedList<>();
			for (int remaining = count; remaining > 0; remaining--) {
				result.addFirst(pop());
			}
			return result;
		}
	}

}
