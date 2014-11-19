package net.netnook.qpeg.expressions;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = (context) -> {
		// no-op
	};

	OnSuccessHandler PUSH_TEXT_TO_STACK = Context::pushCurrentText;

	void accept(Context context);
}
