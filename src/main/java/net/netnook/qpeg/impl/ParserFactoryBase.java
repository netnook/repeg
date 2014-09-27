package net.netnook.qpeg.impl;

import net.netnook.qpeg.builder.BuildContext;
import net.netnook.qpeg.builder.ParsingExpressionBuilderBase;

public abstract class ParserFactoryBase {

	public ParsingRule build() {
		return getStartRule().build(new BuildContext());
	}

	protected abstract ParsingRuleBuilder getStartRule();

	public static final ParsingExpressionBuilderBase IGNORED_WS = Optional.of(CharSequenceMatcher.whitespace().ignore()).ignore();

	public static final OnSuccessHandler TEXT_TO_INTEGER = (context) -> {
		context.resetStackToMark();
		String text = context.getInputFromMark().toString();
		int value = Integer.parseInt(text);
		context.push(value);
	};

	public static OnSuccessHandler orElse(Object defaultValue) {
		return (Context context) -> {
			int len = context.stackSizeFromMark();
			Object value;
			if (len == 0) {
				value = defaultValue;
			} else if (len == 1) {
				value = context.pop();
			} else {
				throw new IllegalStateException("Expected 0 or 1 elements on stack.  Found " + len);
			}
			context.push(value);
		};
	}

	public static OnSuccessHandler replaceWithValueFrom(int childIdx) {
		return (context) -> {
			Object o = context.getFromMark(childIdx);
			context.resetStackToMark();
			context.push(o);
		};
	}

}
