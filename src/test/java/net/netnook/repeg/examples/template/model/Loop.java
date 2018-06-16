package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class Loop extends Node {
	private final String var;
	private final String ref;
	private final List<Node> children;

	public Loop(String var, String ref, List<Node> children) {
		this.var = var;
		this.ref = ref;
		this.children = children;
	}

	@Override
	public void render(Context ctxt, PrintWriter out) {
		Collection collection = ctxt.resolve(ref);
		for (Object element : collection) {
			ctxt.set(var, element);
			for (Node child : children) {
				child.render(ctxt, out);
			}
			ctxt.clear(var);
		}
	}

	@Override
	void preTrim(boolean trimStart, boolean trimEnd) {
		preTrim(children);
	}
}
