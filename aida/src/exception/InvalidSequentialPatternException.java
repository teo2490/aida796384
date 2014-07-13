package exception;

/**
 * Exception thrown in case of malformed sequential pattern.
 * 
 * @author Matteo Simoni 796384
 * Mail to matteo.simoni@mail.polimi.it
 *
 */
public class InvalidSequentialPatternException extends Exception {

	private static final long serialVersionUID = 9106483735947414827L;
	
	public InvalidSequentialPatternException(String s) {
		super(s);
	}
}
