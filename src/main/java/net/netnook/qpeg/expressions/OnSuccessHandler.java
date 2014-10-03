package net.netnook.qpeg.expressions;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = (context) -> {
		// no-op
	};

	void accept(Context context);
}
