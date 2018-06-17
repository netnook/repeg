package net.netnook.repeg.expressions;

import java.util.ArrayList;
import java.util.List;

import net.netnook.repeg.Context;

final class ContextImpl implements Context {

	private final RootContext context;
	private int inputOffset;
	private int stackOffset;

	ContextImpl(RootContext context) {
		this.context = context;
	}

	void setup(int inputOffset, int stackOffset) {
		this.inputOffset = inputOffset;
		this.stackOffset = stackOffset;
	}

	@Override
	public int getInputStartPosition() {
		return inputOffset;
	}

	@Override
	public int getInputEndPosition() {
		return context.position();
	}

	@Override
	public CharSequence getCurrentText() {
		return context.getInput(inputOffset);
	}

	@Override
	public int getCurrentTextLength() {
		return context.position() - inputOffset;
	}

	@Override
	public int size() {
		return context.stackSize() - stackOffset;
	}

	@Override
	public <T> T get(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Invalid index.  Must be >= 0");
		}
		return context.get(index + stackOffset);
	}

	@Override
	public <T> List<T> getAll() {
		int count = size();
		List<T> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(get(i));
		}
		return result;
	}

	@Override
	public void replaceWith(Object object) {
		clear();
		push(object);
	}

	@Override
	public void push(Object object) {
		context.push(object);
	}

	@Override
	public void clear() {
		context.truncateStack(stackOffset);
	}
}
