package net.netnook.qpeg.util;

import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.ParsingExpression;

public class LoggingParseListener implements ParseListener {

	public static LoggingParseListener skipIgnore() {
		return new LoggingParseListener(true);
	}

	private final boolean skipIgnore;

	private LoggingParseListener(boolean skipIgnore) {
		this.skipIgnore = skipIgnore;
	}

	private int depth = -1;

	@Override
	public void onExpressionEnter(ParsingExpression expression, Context context) {
		depth++;

		if (!skipIgnore || !expression.isIgnore()) {
			StringBuilder buf = new StringBuilder();
			appendPrefix(buf);
			buf.append("ENTER ");
			buf.append(expression.buildGrammar());
			buf.append(" start=").append(context.position());
			System.out.println(buf);
		}
	}

	@Override
	public void onExpressionExit(ParsingExpression expression, Context context, boolean success) {
		if (!skipIgnore || !expression.isIgnore()) {
			StringBuilder buf = new StringBuilder();
			appendPrefix(buf);
			buf.append("EXIT ");
			buf.append(expression.buildGrammar());
			buf.append(" start=").append(context.mark().position());
			buf.append(" end=").append(context.position());
			buf.append(" success=").append(success);
			buf.append(" text='").append(context.getCurrentText()).append("'");
			System.out.println(buf);
		}
		depth--;
	}

	private void appendPrefix(StringBuilder buf) {
		for (int i = 0; i < depth; i++) {
			buf.append("  ");
		}
	}
}
