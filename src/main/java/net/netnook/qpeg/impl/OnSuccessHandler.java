package net.netnook.qpeg.impl;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = (context) -> {
		// no-op
	};

	void accept(Context context);
}
