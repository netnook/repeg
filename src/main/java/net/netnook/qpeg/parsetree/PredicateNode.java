package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.Predicate;

public class PredicateNode extends SingleChildNode {

	public PredicateNode(Context context, Predicate predicate, int startPos, int endPos, ParseNode child) {
		super(context, predicate, startPos, endPos, child);
	}
}
