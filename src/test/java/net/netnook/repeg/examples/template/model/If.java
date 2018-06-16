package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;
import java.util.List;

public class If extends Node {
	private final String ref;
	private final List<Node> children;

	public If(String ref, List<Node> children) {
		this.ref = ref;
		this.children = children;
	}

	@Override
	public void render(Context ctxt, PrintWriter out) {
		Object value = ctxt.resolve(ref);

		boolean isTrue = Boolean.TRUE.equals(value);
		if (isTrue) {
			for (Node child : children) {
				child.render(ctxt, out);
			}
		}
	}

	@Override
	void preTrim(boolean trimStart, boolean trimEnd) {
		preTrim(children);
	}
}
