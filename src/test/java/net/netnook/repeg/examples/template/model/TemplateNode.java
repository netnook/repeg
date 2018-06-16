package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;
import java.util.List;

public abstract class TemplateNode {

	private final Trim trims = new Trim();

	public Trim getTrims() {
		return trims;
	}

	public abstract void render(Context ctxt, PrintWriter out);

	abstract void preTrim(boolean trimStart, boolean trimEnd);

	void preTrim(List<TemplateNode> children) {
		if (children.isEmpty()) {
			return;
		}
		boolean trimStartFirstChild = getTrims().isBeforeChildren();
		boolean trimEndLastChild = getTrims().isAfterChildren();

		if (children.size() == 1) {
			children.get(0).preTrim(trimStartFirstChild, trimEndLastChild);
		} else {
			int maxIdx = children.size() - 1;
			for (int i = 0; i <= maxIdx; i++) {
				boolean trimChildStart = (i == 0) ? trimStartFirstChild : children.get(i - 1).getTrims().isAfter();
				boolean trimChildEnd = (i == maxIdx) ? trimEndLastChild : children.get(i + 1).getTrims().isBefore();
				children.get(i).preTrim(trimChildStart, trimChildEnd);
			}
		}
	}
}
