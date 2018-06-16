package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class TemplateLoop extends TemplateNode {
	private final String var;
	private final String ref;
	private final List<TemplateNode> children;

	public TemplateLoop(String var, String ref, List<TemplateNode> children) {
		this.var = var;
		this.ref = ref;
		this.children = children;
	}

	@Override
	public void render(Context ctxt, PrintWriter out) {
		Collection collection = ctxt.resolve(ref);
		for (Object element : collection) {
			ctxt.set(var, element);
			for (TemplateNode child : children) {
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
