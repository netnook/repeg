package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.Optional;

public class OptionalNode extends SingleChildNode {

	public OptionalNode(Context context, Optional optional, int startPos, int endPos, ParseNode child) {
		super(context, optional, startPos, endPos, child);
	}

	public Object getChildValueOrElse(Object defaultValue) {
		if (child == null) {
			return defaultValue;
		} else {
			return child.getOutput();
		}
	}

	public boolean isEmpty() {
		return startPos == endPos;
	}
}
