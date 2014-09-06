package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.Constant;

public class EndOfInputNode extends ParseNode {

	public EndOfInputNode(Context context, Constant constant, int position) {
		super(context, constant, position, position);
	}
}
