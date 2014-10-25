package net.netnook.qpeg.expressions;

public interface ParsingExpressionBuilder {

	ParsingExpression build(BuildContext ctxt);

	boolean isIgnore();

	OnSuccessHandler getOnSuccess();

}
