package net.netnook.repeg.examples.isoduration;

import java.time.Duration;
import java.time.Period;

import net.netnook.repeg.ExpressionBuilder;
import net.netnook.repeg.ParserFactoryBase;
import net.netnook.repeg.RuleEnum;

public class ParserFactory extends ParserFactoryBase<IsoDuration> {

	@Override
	protected RuleEnum getStartRule() {
		return Rules.START;
	}

	public enum Rules implements RuleEnum {
		START {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						one('P'), //
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

		Period_Part {
			@Override
			public ExpressionBuilder expression() {
				return sequence( //
						// TODO: not 'T'
						optional(sequence(Number, one('Y'))).onSuccess(pushIfEmpty(0)), //
						optional(sequence(Number, one('M'))).onSuccess(pushIfEmpty(0)), //
						optional(sequence(Number, one('D'))).onSuccess(pushIfEmpty(0)) //
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
			public ExpressionBuilder expression() {
				return optional( //
						sequence( //
								one('T'), //
								optional(sequence(Number, one('H'))).onSuccess(pushIfEmpty(0)), //
								optional(sequence(Number, one('M'))).onSuccess(pushIfEmpty(0)), //
								optional(sequence(Number, one('S'))).onSuccess(pushIfEmpty(0)) //
						).onSuccess(context -> {
							int hours = context.get(0);
							int minutes = context.get(1);
							int seconds = context.get(2);

							int totalSeconds = (((hours * 60) + minutes) * 60) + seconds;
							context.replaceWith(Duration.ofSeconds(totalSeconds));
						}) //
				).onSuccess(pushIfEmpty(Duration.ofSeconds(0)));
			}
		},

		Number {
			@Override
			public ExpressionBuilder expression() {
				return oneOrMore(characterInRange('0', '9')).onSuccess(pushTextAsInteger());
			}
		};
	}
}
