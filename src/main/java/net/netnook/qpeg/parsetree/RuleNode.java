package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.ParsingRule;

public class RuleNode extends SingleChildNode {

	public RuleNode(Context context, ParsingRule rule, int startPos, int endPos, ParseNode child) {
		super(context, rule, startPos, endPos, child);
	}
}
