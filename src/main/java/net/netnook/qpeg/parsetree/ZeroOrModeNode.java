package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.ZeroOrMore;

public class ZeroOrModeNode extends TreeNode {

	public ZeroOrModeNode(Context context, ZeroOrMore zeroOrMore, int startPos, int endPos, List<ParseNode> children) {
		super(context, zeroOrMore, startPos, endPos, children);
	}
}
