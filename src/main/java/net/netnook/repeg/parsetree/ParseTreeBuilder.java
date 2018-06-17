package net.netnook.repeg.parsetree;

import java.util.ArrayList;
import java.util.List;

import net.netnook.repeg.Context;
import net.netnook.repeg.Expression;
import net.netnook.repeg.ParseListener;

public class ParseTreeBuilder implements ParseListener {

	private static class Marker {
		// empty
	}

	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);

	@Override
	public void onExpressionEnter(Expression expression, Context context) {
		push(new Marker());
	}

	@Override
	public void onExpressionExit(Expression expression, Context context, boolean success) {
		List<ParseNode> children = popToMarker();
		pop(); // pop marker

		if (success) {
			if (children.isEmpty()) {
				push(new LeafNode(context, expression));
			} else {
				push(new TreeNode(context, expression, children));
			}
		}
	}

	private void push(Object o) {
		stack.add(o);
	}

	private <T> T pop() {
		return (T) stack.remove(stack.size() - 1);
	}

	private List<ParseNode> popToMarker() {
		// TODO: consider a some stack implementation which can be reused between here and Context
		int count = sizeToMarker();

		List<ParseNode> results = new ArrayList<>(count);
		for (int i = stack.size() - count; i < stack.size(); i++) {
			results.add((ParseNode) stack.get(i));
		}

		for (int i = 0; i < count; i++) {
			stack.remove(stack.size() - 1);
		}

		return results;
	}

	private int sizeToMarker() {
		int count = 0;
		for (int i = stack.size() - 1; i >= 0; i--) {
			if (stack.get(i) instanceof Marker) {
				break;
			}
			count++;
		}
		return count;
	}

	public ParseTree getParseTree() {
		return new ParseTree(stack);
	}
}
