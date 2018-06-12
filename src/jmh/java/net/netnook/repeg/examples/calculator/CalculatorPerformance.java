package net.netnook.repeg.examples.calculator;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import net.netnook.repeg.expressions.ParsingRule;

public class CalculatorPerformance {

	@State(Scope.Thread)
	public static class BenchmarkState {
		public String input = "(1 + 2) * 3";
		public ParsingRule rule = new ParserFactory().build();
		Integer expect = 9;

	}

	@Benchmark
	public Object run(BenchmarkState state) {
		Integer result = state.rule.parse(state.input);
		if (!result.equals(state.expect)) {
			throw new RuntimeException("Bad result in benchmark");
		}
		return result;
	}
}
