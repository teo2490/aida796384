package aidaController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aidaView.AidaView;

import exception.InvalidSequentialPatternException;


/*** 
 * This class is the simulator component. It is used as a substitute of the DMBS in order to make our tool run.
 * It simply keeps track of all the registered listeners and generates random queries with random intervals. When a query is
 * generated, it notifies all the registered listeners.
 *
 * Copyright (c) 2014 Matteo Simoni 796384
 * 
 * This file is part of the AIDA SOFTWARE
 * (https://code.google.com/p/aida796384/).
 *
 * AIDA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AIDA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AIDA.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Simulator {
	List<QueryListener> listeners = new ArrayList<QueryListener>();
	Random randomGenerator = new Random();
	int seed=0;
	AidaView view;
	
	/**
	 * Constructor with parameter.
	 * 
	 * @param r	The seed for the random generator.
	 */
	public Simulator(int r, AidaView v){
		seed=r;
		view=v;
	}

	/**
	 * This method add a listener to the registered ones list.
	 * 
	 * @param toAdd	The listener that has to be added
	 */
    public void addListener(QueryListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * This method generates random queries with random interval between two of them.
     * 
     * @throws InterruptedException
     * @throws InvalidSequentialPatternException
     */
    public int makeQuery() throws InterruptedException, InvalidSequentialPatternException {
    	
    	
    	int randomInt = randomGenerator.nextInt(seed);
    	
    	Thread.sleep(randomInt*1000*2);
    	
    	if(randomInt==0)	randomInt=randomInt+1;
    	
        //System.out.println("Query "+randomInt+" executed! @ "+System.nanoTime());
    	System.out.println(view);
        // Notify everybody that may be interested.
        for (QueryListener hl : listeners){
        	hl.someoneMadeQuery(randomInt);
        }
        
        return randomInt;
        /* Another body for makeQuery() function for ad-hoc testing purposes
    	for (QueryListener hl : listeners){
    		System.out.println("Query 3 executed! @ "+System.nanoTime());
    		hl.someoneMadeQuery(3);
    	}
    	Thread.sleep(50000);
    	for (QueryListener hl : listeners){
    		System.out.println("Query 3 executed! @ "+System.nanoTime());
    		hl.someoneMadeQuery(3);
    	}
    	Thread.sleep(50000);
    	for (QueryListener hl : listeners){
    		System.out.println("Query 1 executed! @ "+System.nanoTime());
    		hl.someoneMadeQuery(1);
    	}
    	Thread.sleep(50000);
    	for (QueryListener hl : listeners){
    		System.out.println("Query 2 executed! @ "+System.nanoTime());
    		hl.someoneMadeQuery(2);
    	}
    	*/    	
    }
}

