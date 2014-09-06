package net.netnook.qpeg.impl;

import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.EndOfInputNode;
import net.netnook.qpeg.parsetree.ParseNode;

public abstract class Constant implements SimpleExpression {

	public static final char EOICHAR = (char) -1;

	public static final ParsingExpression EOI = new Constant() {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit(this);
		}

		@Override
		public String buildGrammar() {
			return "EOI";
		}

		@Override
		public ParseNode parse(Context context) {
			if (context.peekChar() != EOICHAR) {
				return null;
			}

			return new EndOfInputNode(context, this, context.position());
		}
	};
}
