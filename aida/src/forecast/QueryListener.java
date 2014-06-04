package forecast;

import exception.InvalidSequentialPatternException;

/**
 * The interface for a query listener.
 * 
 * @author Matteo Simoni 796384
 *
 */
public interface QueryListener {
	public void someoneMadeQuery(int q) throws InvalidSequentialPatternException, InterruptedException;
}
