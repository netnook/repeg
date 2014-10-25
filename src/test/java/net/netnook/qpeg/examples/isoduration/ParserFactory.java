package net.netnook.qpeg.examples.isoduration;

import java.time.Duration;
import java.time.Period;

import net.netnook.qpeg.ParserFactoryBase;
import net.netnook.qpeg.expressions.ParsingExpressionBuilder;
import net.netnook.qpeg.expressions.ParsingRuleBuilder;

public class ParserFactory extends ParserFactoryBase {

	@Override
	protected ParsingRuleBuilder getStartRule() {
		return Rules.START;
	}

	public enum Rules implements ParsingRuleBuilder {
		START {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						character('P').ignore(), //
						Period_Part, //
						Time_Part, //
						endOfInput() //
				).onSuccess(context -> {
					Period period = context.get(0);
					Duration duration = context.get(1);
					context.replaceWith(IsoDuration.of(period, duration));
				});
			}
		},

		Period_Part() {
			@Override
			public ParsingExpressionBuilder expression() {
				return sequence( //
						// FIXME: not 'T'
						optional(sequence(Number, character('Y').ignore())).onSuccess(orElse(0)), //
						optional(sequence(Number, character('M').ignore())).onSuccess(orElse(0)), //
						optional(sequence(Number, character('D').ignore())).onSuccess(orElse(0)) //
				).onSuccess((context) -> {
					int years = context.get(0);
					int months = context.get(1);
					int days = context.get(2);
					context.replaceWith(Period.of(years, months, days));
				});
			}
		},

		Time_Part {
			@Override
			public ParsingExpressionBuilder expression() {
				return optional(sequence( //
						character('T').ignore(), //
						optional(sequence(Number, character('H').ignore())).onSuccess(orElse(0)), //
						optional(sequence(Number, character('M').ignore())).onSuccess(orElse(0)), //
						optional(sequence(Number, character('S').ignore())).onSuccess(orElse(0)) //
				).onSuccess(context -> {
					int hours = context.get(0);
					int minutes = context.get(1);
					int seconds = context.get(2);

					int totalSeconds = (((hours * 60) + minutes) * 60) + seconds;
					context.replaceWith(Duration.ofSeconds(totalSeconds));
				})).onSuccess(orElse(Duration.ofSeconds(0)));
			}
		}, //
		Number {
			@Override
			public ParsingExpressionBuilder expression() {
				return oneOrMore(characterInRange('0', '9').ignore()).onSuccess(textToInteger());
			}
		};
	}
}
