package aidaController;

import java.util.ArrayList;
import java.util.List;

import exception.InvalidSequentialPatternException;

import aidaModel.SequentialPattern;
import aidaView.AidaView;


/*** 
 * This class is the implementation of the QueryListener interface. It represents the component of our system that listen to
 * the query flowing and search for some valid sequential pattern in it.
 * If a sequential pattern is found, this component is responsible to create the more suitable indexes.
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

public class Listener implements QueryListener  {
	
	//The list of the known sequential pattern
	private List<SequentialPattern> sp;
	//The list of the sequential pattern partially found in the flow of query
	private List<SequentialPattern> spOnGoing = new ArrayList<SequentialPattern>();
	//The time required for index creation
	private float timeForCreation;
	
	AidaView view;
	
	public Listener(List<SequentialPattern> sp, float t, AidaView v){
		this.sp=sp;
		timeForCreation=t;
		view=v;
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
		
		long time = System.currentTimeMillis();
		int pos=-1;
		
		//It checks the partially recognized sequential pattern list.
        if(spOnGoing.size()>0){
	        for(int j=0; j<spOnGoing.size();j++){
	        	SequentialPattern currentSp = spOnGoing.get(j);
	        	
	        	if(currentSp.getNextNodeToCheck() < currentSp.getNumberOfNodes() && currentSp.getNode(currentSp.getNextNodeToCheck()) == q){
	        		//If the queries is the next of a partial sp (that is yet valid due to time constraint), it is copied, 
	        		//updated and added to the list of partial sp
	        		if(checkValidity(currentSp, time)==true){
		        		SequentialPattern newSP = currentSp.cloneSP();
		        		newSP.incrementNextNodeToCheck();
		        		newSP.setLastCheck(time);
		        		spOnGoing.add(newSP);
		        		//If the remaining time of a sp (the sum of its remaining edges duration without considering the
		        		//next edge to check) is lower than the time needed for index creation, it is removed from the list
		        		//of partial sp and the index creation is scheduled
		        		if(newSP.getRemainingTime()<=timeForCreation){
		        			
		        			//calculating the time to wait before scheduling index creation
		        			//long waitTime=timeForIndexCreation(newSP); //??
		        			
		        			//finding the position of the current sp in the original sp list
		        			pos = findPositionInSpList(newSP);
		        			//Thread.sleep(waitTime*1000); //??
		        			//if the indexes for the current sp are not implemented, it schedule its implementation
		        			if(sp.get(pos).isScheduled()==false){
		        				//System.out.println("INDEX SCHEDULING FOR: "+spOnGoing.get(spOnGoing.size()-1).toString()+" @ "+time);
		        				//ret.add("INDEX SCHEDULING FOR: "+spOnGoing.get(spOnGoing.size()-1).toString()+" @ "+time);
		        				view.printForecastingResponse("INDEX SCHEDULING FOR: "+spOnGoing.get(spOnGoing.size()-1).toString()+" @ "+time);
		        				sp.get(pos).schedule();
		        			}
			        		spOnGoing.remove(spOnGoing.size()-1);
		        		}
		        	} else {
		        		//If the partial sequential pattern is not valid anymore due to time constraint
		        		//System.out.println("Removed old sp for time constraint: "+currentSp.toString());
		        		view.printForecastingResponse("Removed old sp for time constraint: "+currentSp.toString());
		        		spOnGoing.remove(j);
		        	}
	        	} 
	        }
        }
        
        //It checks the original list.
        for(int i=0;i<sp.size();i++){
        	if(sp.get(i).getNode(0) == q){
        		spOnGoing.add(sp.get(i));
        		
        		//Increment and decrement (before the end of the if-block the next node to check in order to make the
        		//getRemainingTime() function work.
        		spOnGoing.get(spOnGoing.size()-1).incrementNextNodeToCheck();
        		spOnGoing.get(spOnGoing.size()-1).setLastCheck(time);
        		//If a sequential pattern is complete (except for the last node that is the teQuery), the index creation is scheduled
        		if(spOnGoing.get(spOnGoing.size()-1).getRemainingTime()<=timeForCreation){
        			//long waitTime=timeForIndexCreation(spOnGoing.get(spOnGoing.size()-1)); //?
        			//finding the position of the current sp in the original sp list
        			pos = findPositionInSpList(spOnGoing.get(spOnGoing.size()-1));
        			//Thread.sleep(waitTime*1000); //?
        			//if the indexes for the current sp are not implemented, it schedule its implementation
        			if(sp.get(pos).isScheduled()==false){
        				view.printForecastingResponse("INDEX SCHEDULING FOR: "+spOnGoing.get(spOnGoing.size()-1).toString()+" @ "+time);
        				sp.get(pos).schedule();
        			}
        		}
        		spOnGoing.get(spOnGoing.size()-1).decrementNextNodeToCheck();
        	}
        }
        view.printForecastingResponse("-------------------------");
    }
	
	/**
	 * This method scans the original list of sequential pattern in order to find if there is a sequential pattern equal to
	 * the one passed as paramenter.
	 * 
	 * @param s The sequential pattern to search in the original list
	 * @return The position of the sp passed as paramenter in the original list
	 */
	public int findPositionInSpList(SequentialPattern s){
		for(int i=0; i<sp.size(); i++){
			if(sp.get(i).equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This method checks the validity of a partial sequential pattern. 
	 * If the elapsed time from the check of the last query and the check of a new one is greater than the duration of the edge 
	 * plus the variance, then the sp is not valid anymore.
	 *  
	 * @param s The partial sp that has to be checked
	 * @param t The time at which the new query has been checked
	 * @return true if the sp is yet valid, false otherwise
	 */
	public boolean checkValidity(SequentialPattern s, long t){
		if(s.getLastCheck()==0)	return true;	//If it is the first node of a sp, return true
		//else if(t<s.getLastCheck()+s.getDuration(s.getNextNodeToCheck()-1)/*+s.getVariance(s.getNextNodeToCheck()-1)*/)	return true;
		else if(t<s.getLastCheck()+s.getDuration(s.getNextNodeToCheck()-1)+s.getVariance(s.getNextNodeToCheck()-1))	return true;
		return false;
		//AT THE STATE OF ART, WE ARE NOT CONSIDERING VARIANCE !!
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
