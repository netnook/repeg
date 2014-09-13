package net.netnook.qpeg.builder;

import java.util.HashMap;
import java.util.Map;

import net.netnook.qpeg.impl.ParsingExpression;

public class BuildContext {

	private Map<Object, ParsingExpression> expressions = new HashMap<>();

	public ParsingExpression getExpression(Object key) {
		return expressions.get(key);
	}

	public void putExpression(Object key, ParsingExpression expression) {
		expressions.put(key, expression);
	}
}
