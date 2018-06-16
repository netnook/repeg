package net.netnook.repeg.examples.template;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import net.netnook.repeg.examples._utils.ResourceLoader;
import net.netnook.repeg.examples.template.model.Template;
import net.netnook.repeg.expressions.ParsingRule;

public class TemplatePerformance {

	@State(Scope.Thread)
	public static class BenchmarkState {
		CharSequence input = ResourceLoader.load("template/house-list.txt");
		ParsingRule rule = new ParserFactory().build();
	}

	@Benchmark
	public Object run(BenchmarkState state) {
		Template template = state.rule.parse(state.input);
		if (template == null) {
			throw new RuntimeException("Bad result in benchmark");
		}
		return template;
	}
}
