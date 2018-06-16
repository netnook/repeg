package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;
import java.util.List;

public class Template extends TemplateNode {
	private final List<TemplateNode> children;

	public Template(List<TemplateNode> children) {
		this.children = children;
	}

	@Override
	public void render(Context ctxt, PrintWriter out) {
		for (TemplateNode child : children) {
			child.render(ctxt, out);
		}
	}

	public void preTrim() {
		preTrim(false, false);
	}

	@Override
	void preTrim(boolean trimStart, boolean trimEnd) {
		preTrim(children);
	}
}
