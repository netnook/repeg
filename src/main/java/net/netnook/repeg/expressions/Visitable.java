package net.netnook.repeg.expressions;

public interface Visitable {

	void accept(Visitor visitor);
}
