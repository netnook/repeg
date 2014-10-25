package net.netnook.qpeg.expressions;

import java.util.Comparator;

import net.netnook.qpeg.expressions.Context.Marker;
import net.netnook.qpeg.parsetree.ParseTree;
import net.netnook.qpeg.util.ParseListener;
import net.netnook.qpeg.util.ParseTreeBuilder;

public class ParsingRule extends ParsingExpressionBase {

	public static final Comparator<? super ParsingRule> SORT_BY_NAME_WITH_START_FIRST = (Comparator<ParsingRule>) (r1, r2) -> {
		String name1 = r1.getName();
		String name2 = r2.getName();

		if ("START".equals(name1)) {
			return -1;
		} else if ("START".equals(name2)) {
			return 1;
		} else {
			return name1.compareTo(name2);
		}
	};

	private final String name;
	// FIXME: is there a way to make this final too ?
	private ParsingExpression expression;

	public ParsingRule(ParsingRuleBuilder builder) {
		super(builder);
		this.name = builder.name();
	}

	public String getName() {
		return name;
	}

	public ParsingExpression getExpression() {
		return expression;
	}

	public void setExpression(ParsingExpression expression) {
		this.expression = expression;
	}

	@Override
	public String buildGrammar() {
		return name;
	}

	@Override
	public boolean parse(Context context) {
		Marker startMarker = context.updateMark();
		onExpressionEnter(context);
		boolean success = expression.parse(context);
		onExpressionExit(context, startMarker, success);
		return success;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public <T> T parse(CharSequence input) throws NoMatchException {
		return parse(input, ParseListener.NO_OP);
	}

	public <T> T parse(CharSequence input, ParseListener listener) throws NoMatchException {
		Context context = new Context(input);
		context.setListener(listener);

		Marker start = context.updateMark();

		boolean success = parse(context);

		// FIXME: check that end of input was reached ?
		if (!success) {
			throw new NoMatchException();
		}

		context.mark(start);

		int count = context.size();

		if (count == 0) {
			return null;
		} else if (count == 1) {
			return context.get(0);
		} else {
			return (T) context.getAll();
		}
	}

	public ParseTree parseTree(CharSequence input) throws NoMatchException {
		Context context = new Context(input);

		ParseTreeBuilder parseTreeBuilder = new ParseTreeBuilder();

		context.setListener(parseTreeBuilder);

		boolean success = parse(context);

		// FIXME: check that end of input was reached ?
		if (!success) {
			throw new NoMatchException();
		}

		return parseTreeBuilder.getParseTree();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
