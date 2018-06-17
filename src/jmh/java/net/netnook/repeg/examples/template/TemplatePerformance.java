package net.netnook.repeg.examples.template;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import net.netnook.repeg.Parser;
import net.netnook.repeg.examples._utils.ResourceLoader;
import net.netnook.repeg.examples.template.model.Template;

public class TemplatePerformance {

	@State(Scope.Thread)
	public static class BenchmarkState {
		CharSequence input = ResourceLoader.load("template/house-list.txt");
		Parser<Template> parser = new ParserFactory().build();
	}

	@Benchmark
	public Object run(BenchmarkState state) {
		Template template = state.parser.parse(state.input);
		if (template == null) {
			throw new RuntimeException("Bad result in benchmark");
		}
		return template;
	}
}
