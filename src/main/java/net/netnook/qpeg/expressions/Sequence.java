package net.netnook.qpeg.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sequence extends CompoundExpression {

	public static SequenceBuilder of(ParsingExpressionBuilder... expressions) {
		return new SequenceBuilder().expressions(expressions);
	}

	public static class SequenceBuilder extends ParsingExpressionBuilderBase {
		private ParsingExpressionBuilder[] expressions;

		public SequenceBuilder expressions(ParsingExpressionBuilder[] expressions) {
			this.expressions = expressions;
			return this;
		}

		@Override
		public SequenceBuilder onSuccess(OnSuccessHandler onSuccess) {
			super.onSuccess(onSuccess);
			return this;
		}

		@Override
		public SequenceBuilder ignore() {
			super.ignore();
			return this;
		}

		@Override
		public Sequence build(BuildContext ctxt) {
			return new Sequence(this, build(ctxt, expressions));
		}
	}

	private final ParsingExpression[] expressions;

	private Sequence(SequenceBuilder builder, ParsingExpression[] expressions) {
		super(builder);
		this.expressions = expressions;
	}

	@Override
	public List<ParsingExpression> parts() {
		return Arrays.asList(expressions);
	}

	@Override
	public String buildGrammar() {
		return Stream.of(expressions) //
				.map(ParsingExpression::buildGrammar) //
				.collect(Collectors.joining(" ", "(", ")"));
	}

	@Override
	public boolean parse(Context context) {
		Context.Marker startMarker = context.mark();

		boolean success = true;
		for (ParsingExpression expression : expressions) {
			success = expression.parse(context);
			if (!success) {
				break;
			}
		}

		if (!success) {
			return false;
		}

		onSuccess(context, startMarker);

		return true;
	}
}
