package net.netnook.qpeg.expressions;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = context -> {
		// no-op
	};

	OnSuccessHandler CLEAR_STACK = Context::clearStack;

	OnSuccessHandler PUSH_TEXT = context -> context.push(context.getCharSequence());

	OnSuccessHandler PUSH_TEXT_AS_INTEGER = context -> {
		context.clearStack();
		String text = context.getCharSequence().toString();
		int value = Integer.parseInt(text);
		context.push(value);
	};

	void accept(Context context);
}
