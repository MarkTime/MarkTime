package boar401s2.marktime.exceptions;

/**
 * Thrown if a squad doesn't exist.
 */
@SuppressWarnings("serial")
public class NonexistantSquadException extends Exception {
	  public NonexistantSquadException() { super(); }
	  public NonexistantSquadException(String message) { super(message); }
	  public NonexistantSquadException(String message, Throwable cause) { super(message, cause); }
	  public NonexistantSquadException(Throwable cause) { super(cause); }
}
