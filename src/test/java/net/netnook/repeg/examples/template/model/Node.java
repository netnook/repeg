package net.netnook.repeg.examples.template.model;

import java.io.PrintWriter;
import java.util.List;

public abstract class Node {

	private final TrimSettings trim = new TrimSettings();

	public TrimSettings getTrimSettings() {
		return trim;
	}

	public abstract void render(Context ctxt, PrintWriter out);

	abstract void preTrim(boolean trimStart, boolean trimEnd);

	void preTrim(List<Node> children) {
		if (children.isEmpty()) {
			return;
		}
		boolean trimStartFirstChild = getTrimSettings().isBeforeChildren();
		boolean trimEndLastChild = getTrimSettings().isAfterChildren();

		if (children.size() == 1) {
			children.get(0).preTrim(trimStartFirstChild, trimEndLastChild);
		} else {
			int maxIdx = children.size() - 1;
			for (int i = 0; i <= maxIdx; i++) {
				boolean trimChildStart = (i == 0) ? trimStartFirstChild : children.get(i - 1).getTrimSettings().isAfter();
				boolean trimChildEnd = (i == maxIdx) ? trimEndLastChild : children.get(i + 1).getTrimSettings().isBefore();
				children.get(i).preTrim(trimChildStart, trimChildEnd);
			}
		}
	}
}
