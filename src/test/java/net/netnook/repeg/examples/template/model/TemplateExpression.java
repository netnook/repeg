package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;

public class TemplateExpression extends TemplateNode {
	private final String ref;

	public TemplateExpression(String ref) {
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
