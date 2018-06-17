package net.netnook.repeg.examples.isoduration;

import java.time.Duration;
import java.time.Period;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import net.netnook.repeg.Parser;

public class IsoDurationPerformance {

	@State(Scope.Thread)
	public static class BenchmarkState {
		String input = "P1Y2M3DT4H5M6S";
		Parser<IsoDuration> parser = new ParserFactory().build();
		IsoDuration expect = IsoDuration.of(Period.of(1, 2, 3), Duration.ofSeconds((4 * 3600) + (5 * 60) + 6));
	}

	@Benchmark
	@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
	public Object run(BenchmarkState state) {
		IsoDuration result = state.parser.parse(state.input);
		if (!result.equals(state.expect)) {
			throw new RuntimeException("Bad result in benchmark");
		}
		return result;
	}

}
