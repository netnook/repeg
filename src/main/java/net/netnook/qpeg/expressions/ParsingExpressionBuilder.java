package net.netnook.qpeg.expressions;

public interface ParsingExpressionBuilder {

	ParsingExpression build();

	default OnSuccessHandler getOnSuccess() {
		return OnSuccessHandler.NO_OP;
	}
}
