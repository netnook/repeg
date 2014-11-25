package net.netnook.qpeg.expressions;

public interface ParsingExpressionBuilder {

	ParsingExpression build();

	static ParsingExpression[] build(ParsingExpressionBuilder[] builders) {
		ParsingExpression[] results = new ParsingExpression[builders.length];
		for (int i = 0; i < builders.length; i++) {
			results[i] = builders[i].build();
		}
		return results;
	}
}
