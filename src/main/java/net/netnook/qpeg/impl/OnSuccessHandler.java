package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;

@FunctionalInterface
public interface OnSuccessHandler {

	OnSuccessHandler NO_OP = (context, start) -> {
		// no-op
	};


	void accept(Context context, Context.Marker start);
}
