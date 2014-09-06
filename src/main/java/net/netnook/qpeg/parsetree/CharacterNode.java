package net.netnook.qpeg.parsetree;

import net.netnook.qpeg.impl.CharMatcher;

public class CharacterNode extends ParseNode {

	public CharacterNode(Context context, CharMatcher charMatcher, int startPos, int endPos) {
		super(context, charMatcher, startPos, endPos);
	}
}
