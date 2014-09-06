package net.netnook.qpeg.parsetree;

public class ParseTree {

	private final ParseNode root;

	public ParseTree(ParseNode root) {
		this.root = root;
	}

	public ParseNode getRoot() {
		return root;
	}

	public void dump() {
		int depth = 0;
		root.dump(depth);
	}

	public Object getOutput() {
		return root.getOutput();
	}
}
