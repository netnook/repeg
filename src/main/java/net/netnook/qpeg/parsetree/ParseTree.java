package net.netnook.qpeg.parsetree;

import java.util.List;

public class ParseTree {

	private final List<Object> children;

	public ParseTree(List<Object> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "ParseTree{" + "children=" + children + '}';
	}
}
