package net.netnook.qpeg.builder;

import net.netnook.qpeg.impl.ParsingExpression;

public interface ParsingExpressionBuilder {

	default ParsingExpression build2() {
		return build(new BuildContext());
	}

	ParsingExpression build(BuildContext ctxt);

	//	public String name() {
	//		return name;
	//	}
	//
	//	public ParsingExpressionBuilder name(String name) {
	//		this.name = name;
	//		return this;
	//	}
	//
	//	public String alias() {
	//		return alias;
	//	}
	//
	//	public ParsingExpressionBuilder alias(String alias) {
	//		this.alias = alias;
	//		return this;
	//	}
	//
	//	public boolean ignore() {
	//		return ignore;
	//	}
	//
	//	public ParsingExpressionBuilder ignore(boolean ignore) {
	//		this.ignore = ignore;
	//		return this;
	//	}
	//
	//	public abstract ParsingExpression build();
	//
	//	protected static ParsingExpression[] build(ParsingExpressionBuilder[] builders) {
	//		ParsingExpression[] results = new ParsingExpression[builders.length];
	//		for (int i = 0; i < builders.length; i++) {
	//			results[i] = builders[i].build();
	//		}
	//		return results;
	//	}
}
