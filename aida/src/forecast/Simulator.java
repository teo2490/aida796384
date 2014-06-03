package forecast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exception.InvalidSequentialPatternException;

public class Simulator {
	List<QueryListener> listeners = new ArrayList<QueryListener>();
	Random randomGenerator = new Random();
	int seed=0;
	
	public Simulator(int r){
		seed=r;
	}

    public void addListener(QueryListener toAdd) {
        listeners.add(toAdd);
    }

    public void makeQuery() throws InterruptedException, InvalidSequentialPatternException {
    	
    	int randomInt = randomGenerator.nextInt(seed);
    	
    	Thread.sleep(randomInt*1000);
    	
        System.out.println("Query "+randomInt+" executed!");

        // Notify everybody that may be interested.
        for (QueryListener hl : listeners)
            hl.someoneMadeQuery(randomInt);
    }
}

