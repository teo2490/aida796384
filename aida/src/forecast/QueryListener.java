package forecast;

import exception.InvalidSequentialPatternException;

public interface QueryListener {
	public void someoneMadeQuery(int q) throws InvalidSequentialPatternException, InterruptedException;
}
