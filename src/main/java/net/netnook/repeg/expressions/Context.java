package net.netnook.repeg.expressions;

public final class Context {

	private final RootContext context;
	private int inputOffset;
	private int stackOffset;

	Context(RootContext context) {
		this.context = context;
	}

	void setup(int inputOffset, int stackOffset) {
		this.inputOffset = inputOffset;
		this.stackOffset = stackOffset;
	}

	public CharSequence getCharSequence() {
		return context.getInput(inputOffset);
	}

	public int stackSize() {
		return context.stackSize() - stackOffset;
	}

	public <T> T get(int index) {
		return context.get(index + stackOffset);
	}

	public void replaceWith(Object object) {
		clearStack();
		push(object);
	}

	public void push(Object object) {
		context.push(object);
	}

	public void clearStack() {
		context.truncateStack(stackOffset);
	}
}
