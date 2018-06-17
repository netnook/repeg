package net.netnook.repeg;

/**
 * An {@link OnSuccessHandler} is the primary way to handle and process matched input in order to create a parse result.  An {@link OnSuccessHandler} for an
 * expression will only be called when the expression matches.  When called, a {@link Context} object will be passed which can be used to retrieve the
 * matched input as well as modify a stack in order to build a parse result.  See {@link Context} for more information.
 * <p>
 * Important:  A {@link Context} object may be re-used across calls to multiple {@link OnSuccessHandler OnSuccessHandlers}.
 * You should therefore not keep a reference for use later but instead do all processing immediately in the
 * {@link OnSuccessHandler#accept(Context)} method.
 */
@FunctionalInterface
public interface OnSuccessHandler {

	/**
	 * Clear current stack content added by current expression of descendents.  See {@link Context#clearStack()}.
	 */
	OnSuccessHandler CLEAR_STACK = Context::clearStack;

	/**
	 * Get a {@link CharSequence} of the current input and push it onto the stack.
	 */
	OnSuccessHandler PUSH_TEXT = context -> context.push(context.getCharSequence());

	/**
	 * Convert current input to a {@link String} and push it onto the stack.
	 */
	OnSuccessHandler PUSH_TEXT_AS_STRING = context -> {
		context.push(context.getCharSequence().toString());
	};

	/**
	 * Convert current input to a {@link String} and push it onto the stack.  Pushes null if current input has 0 length.
	 */
	OnSuccessHandler PUSH_TEXT_AS_NULLABLE_STRING = context -> {
		CharSequence text = context.getCharSequence();
		if (text.length() > 0) {
			context.push(text.toString());
		} else {
			context.push(null);
		}
	};

	/**
	 * Convert current input to an {@link Integer} using {@link Integer new Integer(String)} and push it onto the stack.
	 */
	OnSuccessHandler PUSH_TEXT_AS_INTEGER = context -> {
		String text = context.getCharSequence().toString();
		context.push(new Integer(text));
	};

	/**
	 * Convert current input to an {@link Float} using {@link Float new Float(String)} and push it onto the stack.
	 */
	OnSuccessHandler PUSH_TEXT_AS_FLOAT = context -> {
		String text = context.getCharSequence().toString();
		context.push(new Float(text));
	};

	/**
	 * Convert current input to an {@link Float} using {@link Float new Float(String)} and push it onto the stack.  Pushes null if current input has 0 length.
	 */
	OnSuccessHandler PUSH_TEXT_AS_NULLABLE_FLOAT = context -> {
		CharSequence text = context.getCharSequence();
		if (text.length() > 0) {
			context.push(new Float(text.toString()));
		} else {
			context.push(null);
		}
	};

	/**
	 * Callback called when the associated expression matches.
	 *
	 * @param context context for match.
	 */
	void accept(Context context);
}
