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
	public int getStartPosition() {
		return inputOffset;
	}

	@Override
	public int getCurrentPosition() {
		return context.position();
	}

	@Override
	public CharSequence getCharSequence() {
		return context.getInput(inputOffset);
	}

	@Override
	public int inputLength() {
		return context.position() - inputOffset;
	}

	@Override
	public int stackSize() {
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
		int count = stackSize();
		List<T> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(get(i));
		}
		return result;
	}

	@Override
	public void replaceWith(Object object) {
		clearStack();
		push(object);
	}

	@Override
	public void push(Object object) {
		context.push(object);
	}

	@Override
	public void clearStack() {
		context.truncateStack(stackOffset);
	}
}
