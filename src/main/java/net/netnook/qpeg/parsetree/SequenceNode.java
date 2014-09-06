package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.Sequence;

public class SequenceNode extends TreeNode {

	public SequenceNode(Context context, Sequence sequence, int startPos, int endPos, List<ParseNode> children) {
		super(context, sequence, startPos, endPos, children);
	}
}
