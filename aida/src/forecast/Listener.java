package forecast;

import java.util.ArrayList;
import java.util.List;

import exception.InvalidSequentialPatternException;

import aida.SequentialPattern;

public class Listener implements QueryListener  {
	
	//The list of the known sequential pattern
	private List<SequentialPattern> sp;
	//The list of the sequential pattern partially found in the flow of query
	private List<SequentialPattern> spOnGoing = new ArrayList<SequentialPattern>();
	
	public Listener(List<SequentialPattern> sp){
		this.sp=sp;
	}
	
	/**
	 * Every time a query flows, this method is called back. It checks if the current query is the next one in one of the
	 * partially recognized sequential pattern: if it is, it copies the sequential pattern and update the list of the partially
	 * recognized ones. It copies because if we have a partial sp like "3 3" and we see a "2" it can be a "3 3 2" pattern, but
	 * if after the "2" we had a "1", it could also be a "3 3 1" pattern.
	 * 
	 * After it has checked the partially recognized sp list, it checks the original list of the sequential pattern in order to
	 * found if the query just executed could be the starting point for a new sp. In this case it update the partially recognized
	 * sequential pattern list with the new partial sp. 
	 * @throws InterruptedException 
	 */
	@Override
    public void someoneMadeQuery(int q) throws InvalidSequentialPatternException, InterruptedException {
        
		//It checks the partially recognized sequential pattern list.
        if(spOnGoing.size()>0){
	        for(int j=0; j<spOnGoing.size();j++){
	        	SequentialPattern currentSp = spOnGoing.get(j);
	        	if(currentSp.getNextNodeToCheck() < currentSp.getNumberOfNodes() && currentSp.getNode(currentSp.getNextNodeToCheck()) == q){
	        		//If the queries is the next of a partial sp, it is copied, updated and added to the list of partial sp
	        		SequentialPattern newSP = currentSp.cloneSP();
	        		newSP.incrementNextNodeToCheck();
	        		spOnGoing.add(newSP);
	        		//If a sequential pattern is complete (except for the last node that is the teQuery), it is removed 
	        		//from the list of partial sp and the index creation is scheduled
	        		if(newSP.getNextNodeToCheck() == newSP.getNumberOfNodes()-1){
	        			long waitTime=timeForIndexCreation(newSP);
	        			Thread.sleep(waitTime*1000);
	        			System.out.println("INDEX SCHEDULING FOR: "+spOnGoing.get(spOnGoing.size()-1).toString()+" @ "+System.nanoTime());
		        		spOnGoing.remove(spOnGoing.size()-1);
		        	}
	        	}
	        }
        }
        
        //It checks the original list
        for(int i=0;i<sp.size();i++){
        	if(sp.get(i).getNode(0) == q)	spOnGoing.add(sp.get(i));
        }
    }
	
	/**
	 * This method is called when the last query before the time-expensive one is exectuted and computes the time 
	 * in which the indexes for that query has to be created.
	 * 
	 * @param s	The completed sequential pattern (except for the time-expensive query)
	 * @return	The time to wait before indexes creation
	 */
	private long timeForIndexCreation(SequentialPattern s){
		float floatWaitTime = s.getDuration(s.getNumberOfEdges()-1) - s.getVariance(s.getNumberOfEdges()-1);
		long waitTime = (long) floatWaitTime;
		if(waitTime<0)	waitTime=0;
		
		return waitTime;
	}
}
