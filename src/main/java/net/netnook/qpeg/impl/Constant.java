package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.parsetree.Context;
import net.netnook.qpeg.parsetree.EndOfInputNode;
import net.netnook.qpeg.parsetree.ParseNode;

public abstract class Constant extends SimpleExpression {

	public static final char EOICHAR = (char) -1;

	public static final EoiBuilder EOI_BUILDER = new EoiBuilder();

	public static class EoiBuilder extends ParsingExpressionBuilderBase {

		@Override
		public ParsingExpression build(BuildContext ctxt) {
			return EOI;
		}
	}

	public static final ParsingExpression EOI = new Constant(true, null) {

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

		//		@Override
		//		public ParsingExpression ignore() {
		//			throw new UnsupportedOperationException();
		//		}
	};

	private Constant(boolean ignore, String alias) {
		super(ignore, alias);
	}
}
