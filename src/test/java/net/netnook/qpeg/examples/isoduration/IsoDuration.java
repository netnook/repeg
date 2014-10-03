package net.netnook.qpeg.examples.isoduration;

import java.time.Duration;
import java.time.Period;
import java.util.Objects;

public class IsoDuration {

	public static IsoDuration of(Period period, Duration duration) {
		return new IsoDuration(period, duration);
	}

	private final Period period;
	private final Duration duration;

	public IsoDuration(Period period, Duration duration) {
		this.period = period;
		this.duration = duration;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		IsoDuration that = (IsoDuration) o;
		return Objects.equals(period, that.period) && Objects.equals(duration, that.duration);
	}

	@Override
	public int hashCode() {
		return Objects.hash(period, duration);
	}

	@Override
	public String toString() {
		return "IsoDuration{period=" + period + ", duration=" + duration + '}';
	}
}
