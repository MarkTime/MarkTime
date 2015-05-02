package boar401s2.marktime.exceptions;

/**
 * Thrown if a squad is trying to be pushed, but hasn't been
 * pulled from either local or remote source yet.
 */
@SuppressWarnings("serial")
public class SquadNotFetchedException extends Exception {
	public SquadNotFetchedException() { super(); }
	public SquadNotFetchedException(String message) { super(message); }
	public SquadNotFetchedException(String message, Throwable cause) { super(message, cause); }
	public SquadNotFetchedException(Throwable cause) { super(cause); }
}
