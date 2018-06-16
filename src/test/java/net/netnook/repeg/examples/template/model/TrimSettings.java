package net.netnook.repeg.examples.template.model;

public class TrimSettings {
	private boolean before;
	private boolean after;
	private boolean beforeChildren;
	private boolean afterChildren;

	public boolean isBefore() {
		return before;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}

	public boolean isAfter() {
		return after;
	}

	public void setAfter(boolean after) {
		this.after = after;
	}

	public boolean isBeforeChildren() {
		return beforeChildren;
	}

	public void setBeforeChildren(boolean beforeChildren) {
		this.beforeChildren = beforeChildren;
	}

	public boolean isAfterChildren() {
		return afterChildren;
	}

	public void setAfterChildren(boolean afterChildren) {
		this.afterChildren = afterChildren;
	}
}
