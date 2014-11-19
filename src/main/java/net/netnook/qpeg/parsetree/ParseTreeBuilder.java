package net.netnook.qpeg.parsetree;

import java.util.ArrayList;
import java.util.List;

import net.netnook.qpeg.expressions.Context;
import net.netnook.qpeg.expressions.ParsingExpression;
import net.netnook.qpeg.util.ParseListener;

public class ParseTreeBuilder implements ParseListener {

	private static final int DEFAULT_INITIAL_STACK_CAPACITY = 64;
	private final ArrayList<Object> stack = new ArrayList<>(DEFAULT_INITIAL_STACK_CAPACITY);

	@Override
	public void onExpressionEnter(ParsingExpression expression, Context context) {
		push(context.buildMarker());
	}

	@Override
	public void onExpressionExit(ParsingExpression expression, Context context, boolean success) {
		List<ParseNode> children = popToMarker();
		Context.Marker marker = pop();

		if (success) {
			if (children.isEmpty()) {
				push(new LeafNode(context, expression, marker.position(), context.position()));
			} else {
				push(new TreeNode(context, expression, marker.position(), context.position(), children));
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
		// FIXME: consider a some stack implementation which can be reused between here and Context
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
			if (stack.get(i) instanceof Context.Marker) {
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
