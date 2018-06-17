package net.netnook.repeg.exceptions;

/**
 * Exception thrown to indicate that parsing did not result in a match.
 */
public final class NoMatchException extends ParseException {

	public NoMatchException(String message) {
		super(message);
	}
}
