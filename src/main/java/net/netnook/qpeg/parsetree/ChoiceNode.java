package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.Choice;

public class ChoiceNode extends SingleChildNode {

	public ChoiceNode(Context context, Choice choice, int startPos, int endPos, ParseNode child) {
		super(context, choice, startPos, endPos, child);
	}
}
