package net.netnook.repeg.examples.calculator;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import net.netnook.repeg.Parser;

public class CalculatorPerformance {

	@State(Scope.Thread)
	public static class BenchmarkState {
		public String input = "(1 + 2) * 3";
		public Parser<Integer> parser = new ParserFactory().build();
		Integer expect = 9;

	}

	@Benchmark
	@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
	public Object run(BenchmarkState state) {
		Integer result = state.parser.parse(state.input);
		if (!result.equals(state.expect)) {
			throw new RuntimeException("Bad result in benchmark");
		}
		return result;
	}
}
