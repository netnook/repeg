package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;

public abstract class Constant extends SimpleExpression {

	public static final char EOICHAR = (char) -1;

	public static final EoiBuilder EOI_BUILDER = new EoiBuilder();

	public static class EoiBuilder extends ParsingExpressionBuilderBase {

		@Override
		public ParsingExpression build(BuildContext ctxt) {
			return EOI;
		}

		@Override
		public ParsingExpressionBuilderBase ignore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ParsingExpressionBuilderBase onSuccess(OnSuccessHandler onSuccess) {
			throw new UnsupportedOperationException();
		}
	}

	public static final ParsingExpression EOI = new Constant(EOI_BUILDER) {

		@Override
		public void accept(Visitor visitor) {
			visitor.visit(this);
		}

		@Override
		public String buildGrammar() {
			return "EOI";
		}

		@Override
		public boolean parse(Context context) {
			if (context.peekChar() != EOICHAR) {
				return false;
			}
			return true;
		}
	};

	private Constant(ParsingExpressionBuilderBase builder) {
		super(builder);
	}
}
