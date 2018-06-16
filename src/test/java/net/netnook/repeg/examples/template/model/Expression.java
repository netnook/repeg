package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;

public class Expression extends Node {
	private final String ref;

	public Expression(String ref) {
		this.ref = ref;
	}

	@Override
	public void render(Context ctxt, PrintWriter out) {
		Object value = ctxt.resolve(ref);

		if (value != null) {
			out.print(value.toString());
		}
	}

	@Override
	void preTrim(boolean trimStart, boolean trimEnd) {
		// no-op
	}
}
