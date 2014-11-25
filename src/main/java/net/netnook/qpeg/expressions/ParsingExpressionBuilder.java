package net.netnook.qpeg.expressions;

public interface ParsingExpressionBuilder {

	ParsingExpression build();

	OnSuccessHandler getOnSuccess();
}
