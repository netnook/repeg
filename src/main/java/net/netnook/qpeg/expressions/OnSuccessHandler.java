package net.netnook.qpeg.expressions;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = (context) -> {
		// no-op
	};

	OnSuccessHandler PUSH_TEXT_TO_STACK = context -> context.push(context.getCharSequence());

	OnSuccessHandler CLEAR_STACK = Context::clearStack;

	void accept(Context context);
}
