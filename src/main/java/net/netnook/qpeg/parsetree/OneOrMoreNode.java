package net.netnook.qpeg.parsetree;

import java.util.List;

import net.netnook.qpeg.impl.OneOrMore;

public class OneOrMoreNode extends TreeNode {

	public OneOrMoreNode(Context context, OneOrMore oneOrMore, int startPos, int endPos, List<ParseNode> children) {
		super(context, oneOrMore, startPos, endPos, children);
	}
}
