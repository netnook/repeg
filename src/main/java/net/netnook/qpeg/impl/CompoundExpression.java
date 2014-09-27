package net.netnook.qpeg.impl;

import java.util.List;

import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;
import net.netnook.qpeg.impl.Context.Marker;

public abstract class CompoundExpression extends ParsingExpressionBase {

	protected CompoundExpression(ParsingExpressionBuilderBase builder) {
		super(builder);
	}

	public abstract List<ParsingExpression> parts();

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	protected void onSuccess(Context context, Marker startMarker) {
		context.mark(startMarker);
		if (ignore) {
			context.clear();
		} else {
			onSuccess.accept(context);
		}
	}
}
