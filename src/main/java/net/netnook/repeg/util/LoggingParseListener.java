package net.netnook.repeg.util;

import java.util.function.Consumer;

import net.netnook.repeg.expressions.ParsingExpression;
import net.netnook.repeg.expressions.RootContext;

public class LoggingParseListener implements ParseListener {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Consumer<String> printer = System.out::println;

		public Builder printer(Consumer<String> printer) {
			this.printer = printer;
			return this;
		}

		public LoggingParseListener build() {
			return new LoggingParseListener(this);
		}
	}

	private final Consumer<String> printer;

	private int depth = -1;

	private LoggingParseListener(Builder builder) {
		this.printer = builder.printer;
	}

	@Override
	public void onExpressionEnter(ParsingExpression expression, RootContext context) {
		depth++;

		StringBuilder buf = new StringBuilder();
		appendPrefix(buf);
		buf.append("ENTER ");
		buf.append(expression.buildGrammar());
		buf.append(" start=").append(context.position());
		print(buf.toString());
	}

	@Override
	public void onExpressionExit(ParsingExpression expression, RootContext context, int startPosition, int startStackIdx, boolean success) {
		StringBuilder buf = new StringBuilder();
		appendPrefix(buf);
		buf.append("EXIT ");
		buf.append(expression.buildGrammar());
		buf.append(" start=").append(startPosition);
		buf.append(" end=").append(context.position());
		buf.append(" success=").append(success);
		buf.append(" text='").append(context.getInput(startPosition)).append("'");
		print(buf.toString());

		depth--;
	}

	private void appendPrefix(StringBuilder buf) {
		for (int i = 0; i < depth; i++) {
			buf.append("  ");
		}
	}

	private void print(String line) {
		printer.accept(line);
	}
}