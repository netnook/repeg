package net.netnook.repeg.expressions;

interface Visitable {

	void accept(Visitor visitor);
}
