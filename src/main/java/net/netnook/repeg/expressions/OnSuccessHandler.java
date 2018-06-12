package net.netnook.repeg.expressions;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = context -> {
		// no-op
	};

	OnSuccessHandler CLEAR_STACK = Context::clearStack;

	OnSuccessHandler PUSH_TEXT = context -> context.push(context.getCharSequence());

	OnSuccessHandler PUSH_TEXT_AS_STRING = context -> {
		context.push(context.getCharSequence().toString());
	};

	OnSuccessHandler PUSH_TEXT_AS_NULLABLE_STRING = context -> {
		CharSequence text = context.getCharSequence();
		if (text.length() > 0) {
			context.push(text.toString());
		} else {
			context.push(null);
		}
	};

	OnSuccessHandler PUSH_TEXT_AS_INTEGER = context -> {
		String text = context.getCharSequence().toString();
		context.push(new Integer(text));
	};

	OnSuccessHandler PUSH_TEXT_AS_FLOAT = context -> {
		String text = context.getCharSequence().toString();
		context.push(new Float(text));
	};

	OnSuccessHandler PUSH_TEXT_AS_NULLABLE_FLOAT = context -> {
		CharSequence text = context.getCharSequence();
		if (text.length() > 0) {
			context.push(new Float(text.toString()));
		} else {
			context.push(null);
		}
	};

	void accept(Context context);
}
