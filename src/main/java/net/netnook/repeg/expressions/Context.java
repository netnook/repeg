package net.netnook.repeg.expressions;

/**
 * Context used by {@link OnSuccessHandler} to process matched input.
 * <p>
 * When an {@link OnSuccessHandler} for an expression is called, that expression is considered the 'current'
 * expression.  When called, the {@link Context} contains both the input position before the expression was evaluated
 * (i.e. the start position) as well as the input position after the expression (and all of it's sub-expressions)
 * was evaluated.  The full input text which was matched by the current expression (including all sub-expressions)
 * can be retrieved using the {@link Context#getCharSequence()} method.
 * <p>
 * An {@link OnSuccessHandler} may be used in expressions to process the current input and add or modify a stack
 * used to build up the output of the parser.  To this end, the {@link Context} also contains the starting position/size
 * of the stack before the expression (or any of it's sub-expressions) were evaluated. An {@link OnSuccessHandler} can
 * simply add an element to the stack (e.g. convert the current input sequence from {@link Context#getCharSequence()} to
 * an Integer and {@link Context#push(Object) push} it onto the stack) or retrieve elements from the stack which were added
 * by sub-expressions using {@link Context#get(int) get(index)}, process them in some way, and then replace them with some
 * computed value using {@link Context#replaceWith(Object) replace(object)}.
 * <p>
 * Important:  A {@link Context} object may be re-used across calls to multiple {@link OnSuccessHandler OnSuccessHandlers}.
 * You should therefore not keep a reference for use later but instead do all processing immediately in the
 * {@link OnSuccessHandler} recipient.
 */
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

	/**
	 * Get the input sequence matched by the expression.
	 *
	 * @return matched char sequence
	 */
	public CharSequence getCharSequence() {
		return context.getInput(inputOffset);
	}

	/**
	 * Get the number of elements on the stack produced/added by the current expression and all it's
	 * descendents.
	 *
	 * @return size of the stack.
	 */
	public int stackSize() {
		return context.stackSize() - stackOffset;
	}

	/**
	 * Get an element from the stack which was added by the current expression or one of it's descendents.
	 * <p>
	 * Note: it is possible or generally not advised to use negative index as this would make the current
	 * expression dependent on it's ancestor expressions.
	 *
	 * @param index the stack index, relative to the point when the current expression started.
	 * @return element from the stack.
	 */
	public <T> T get(int index) {
		return context.get(index + stackOffset);
	}

	/**
	 * Replace all elements on the stack which were added by the current expression or one of it's descendents
	 * by the specified object.
	 *
	 * @param object to add to the stack as a replacement for current/descendent elements.
	 */
	public void replaceWith(Object object) {
		clearStack();
		push(object);
	}

	/**
	 * Push an object onto the stack.
	 *
	 * @param object to add to the stack.
	 */
	public void push(Object object) {
		context.push(object);
	}

	/**
	 * Remove all elements on the stack which were added by the current expression or one of it's descendents.
	 */
	public void clearStack() {
		context.truncateStack(stackOffset);
	}
}
