package aidaModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import exception.InvalidSequentialPatternException;


/*** 
 * This class is the object that stands for a sequential pattern. 
 * It has a list of node (element of the sequential pattern) and a
 * list of edges (the object between nodes).
 *
 * Copyright (c) 2014 Matteo Simoni 796384
 * Mail to matteo.simoni@mail.polimi.it
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

public class SequentialPattern {

	//It contains the id of the query
	private List<Integer> node;
	//It contains the objects that connect nodes
	private List<Edge> edge;
	//The support of the sequential pattern
	private int sup;
	
	//This is a pointer to the next node that has to be checked in this sequential pattern in the forcasting phase
	private int nextNodeToCheck;
	//This is a reminder for the last matching of a query from the flow with a query in the sequential pattern.
	private long lastCheck;
	//This field knows if the indexes for this sp are already scheduled or not
	private boolean indexScheduled;
	
	/**
	 * Default Constructor
	 * 
	 * @throws InvalidSequentialPatternException
	 */
	public SequentialPattern() throws InvalidSequentialPatternException{
		node = new ArrayList<Integer>();
		edge = new ArrayList<Edge>();
		sup=0;
		nextNodeToCheck=1;
		lastCheck=0;
		indexScheduled=false;
		validateState();
	}
	
	/**
	 * This method perform a deep cloning of a sequential pattern.
	 * 
	 * @return The cloned sequential pattern
	 * @throws InvalidSequentialPatternException
	 */
	public SequentialPattern cloneSP() throws InvalidSequentialPatternException{
		SequentialPattern cloned = new SequentialPattern();
		cloned.setSupport(this.getSupport());
		cloned.setNextNodeToCheck(this.getNextNodeToCheck());
		cloned.setLastCheck(this.getLastCheck());
		for(int i=0; i<node.size();i++){
			cloned.addNode(this.getNode(i));
		}
		for(int j=0; j<edge.size(); j++){
			cloned.setEdge(edge.get(j), j);
		}
		return cloned;
	}
	
	/* Start of the getter and setter methods */
	public int getSupport(){
		return sup;
	}
	
	public void setSupport(int s){
		sup=s;
	}
	
	public void setNextNodeToCheck(int n){
		nextNodeToCheck=n;
	}
	
	public List<Integer> getAllNodes(){
		return node;
	}
	
	public void setEdge(Edge e, int pos){
		edge.set(pos, e);
	}
	
	public int getNumberOfNodes(){
		return node.size();
	}
	
	public int getNumberOfEdges(){
		return edge.size();
	}
	
	public int getNode(int n){
		return node.get(n);
	}
	
	public float getDuration(int e){
		return edge.get(e).getDuration();
	}
	
	public float getVariance(int e){
		return edge.get(e).getVariance();
	}
	
	public long getLastCheck(){
		return lastCheck;
	}
	
	public void setLastCheck(long i){
		lastCheck=i;
	}
	/* End of the getter and setter methods */
	
	/**
	 * This method creates add a new node to the sequetial pattern. If the number of nodes is >1, when a node is added
	 * a new edge is also added
	 * 
	 * @param n	The value of the node
	 * @throws InvalidSequentialPatternException
	 */
	public void addNode(int n) throws InvalidSequentialPatternException{
		node.add(n);
		if(node.size()>=2){
			edge.add(new Edge());
			validateState();
		}

	}
	
	/**
	 * This method checks if a particular edge (that starts at position ps in the sequential pattern) is part of the sp or not.
	 * In particular it checks if the forward part of the sp is present.
	 * 
	 * @param ql The list of query
	 * @param ps The position of the ending query of the edge in the sp
	 * @param pc The current position in the list of query
	 * @return true if the edge is part of a sp, false otherwise
	 */
	public boolean partialSpIsPresentForward(List<Integer> ql, int ps, int pc){
		if(ps >= node.size())	return true;
		for(int i=pc; i<ql.size(); i++){
			if(ql.get(i)==node.get(ps))	ps++;
		}
		if(ps==node.size())	return true;
		else return false;
	}
	
	/**
	 * This method checks if a particular edge (that starts at position ps in the sequential pattern) is part of the sp or not.
	 * In particular it checks if the backward part of the sp is present.
	 * 
	 * @param ql The list of query
	 * @param ps The position of the starting query of the edge in the sp
	 * @param pc The current position in the list of query
	 * @return true if the edge is part of a sp, false otherwise
	 */
	public boolean partialSpIsPresentBackward(List<Integer> ql, int ps, int pc){
		if(ps <= 0)	return true;
		for(int i=pc; i>=0; i--){
			if(ql.get(i)==node.get(ps))	ps--;
			if(ps == -1)	return true;
		}
		if(ps == -1)	return true;
		else return false;
	}
	
	/**
	 * This method finds all the occurences of an edge (two consecutives queries in a sp) in a list of query. It calculates
	 * the duration of the instance of the edge and adds it to the instances duration list only if that edge is part of a
	 * sequential pattern.
	 *  
	 * @param ql The list of query
	 * @param tl The list of timestamp (at position 0 there is the timestamp of the query at position 0 in ql)
	 * @param e The edge number
	 */
	public void findEdgeInstance(List<Integer> ql, List<Date> tl, int e){
		int k=e;
		int i=0;
		int t=0;
		Date start=null;
		Date end=null;
		double diff;
		
		while(t<ql.size()-1){
			if(ql.get(t)==node.get(k)){
				start = tl.get(t);
				for(i=t+1;i<ql.size();i++){
					if(ql.get(i)==node.get(k+1) && partialSpIsPresentBackward(ql, k, t) && partialSpIsPresentForward(ql, k+1, i)){
						end = tl.get(i);
						diff=getDateDiff(start, end, TimeUnit.MILLISECONDS);
						diff=diff/1000;	//Convert in seconds
						edge.get(k).addInstance(diff);
					}
				}
				start = null;
				end = null;
			}
			t++;
		}
	}
	
	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	/**
	 * This function finds if the sequential is in the list of query passed as a parameter. If it is, it compute the duration
	 * of each edge from the corresponding list timestamp.
	 * 
	 * @param ql The list of query
	 * @param tl The list of timestamp (at position 0 there is the timestamp of the query at position 0 in ql)
	 * @throws InvalidSequentialPatternException
	 */
	public void findSequentialPattern(List<Integer> ql, List<Date> tl) throws InvalidSequentialPatternException{
		for(int i=0; i<edge.size(); i++){
			findEdgeInstance(ql, tl, i);
		}
		/*------------------------------------
		while(countContinue < ql.size()){
			for(i = countContinue; i<ql.size(); i++){
				System.out.println("I: "+i);
				if(ql.get(i)==node.get(k)){
					if(start==null)	{
						start=tl.get(i);
						k++;
					}
					else {
						end = tl.get(i);
						k--;
					}
					
					if(end!=null) {
						System.out.println(partialSpIsPresent(ql, k+2, i));
						if(partialSpIsPresent(ql, k+2, i) == true){
							diff=end.getTime()-start.getTime();
							System.out.println("S: "+start);
							System.out.println("E: "+end);
							System.out.println(diff);
							System.out.println("k: "+k);
							//diff=diff/1000;	//Convert in seconds
							edge.get(k).addInstance(diff);
							start = null;
							end = null;
							i = ql.size();
							countContinue++;
						} else {
							i = ql.size();
							System.out.println("i=ql.size()");
							k++;
						}
					}		
				}
			}
		}
		/* ------------------------
		int k=0;
		//It counts how many edge is inserted for an hypotethical instance of sp. If the sp is not complete, it is used to remove 
		//the data already inserted.
		int added=0;
		Date start=null, end=null;
		long diff;
		for(int i=0; i<ql.size(); i++){
			if(ql.get(i)==node.get(k)){
				if(start==null)	start=tl.get(i);
				else end = tl.get(i);
				
				if(end!=null) {
					diff=end.getTime()-start.getTime();
					System.out.println("S: "+start);
					System.out.println("E: "+end);
					System.out.println(diff);
					//diff=diff/1000;	//Convert in seconds
					edge.get(k-1).addInstance(diff);
					added++;
					//k and i are decremented because the query that has closed an edge will be the same that will open the next one
					k--;
					
;
					start = null;
					end = null;
				}
				k++;
			}
		}
		
		//It checks the the sp is complete, otherwise it clean the edgeInstancesDuration list from the partial values
		if(k<node.size()-1){
			//It removes the duration of the incomplete sp
			for(int j=0; j<added; j++){
				edge.get(j).removeLastInstance();
			}
		}
		----------------- */
		//validateState();
	}
	
	/**
	 * This method computes the duration and the variance for each edge in the sequential pattern
	 */
	public void computeDuration(){
		for(int i=0; i<edge.size(); i++){
			edge.get(i).computeDurationAndVariance();
		}
	}
	
	/**
	 * This method returns the next node that has to be checked in the forecasting phase
	 * 
	 * @return
	 */
	public int getNextNodeToCheck(){
		return nextNodeToCheck;
	}
	
	/**
	 * This method increases the next node that has to be checked in the forecasting phase
	 */
	public void incrementNextNodeToCheck(){
		nextNodeToCheck++;
	}
	
	/**
	 * This method decreases the next node that has to be checked in the forecasting phase
	 */
	public void decrementNextNodeToCheck(){
		nextNodeToCheck--;
	}
	
	/**
	 * This method checks if the indexes for this sp are already implemented or not.
	 * 
	 * @return true if the indexes are implemented, false otherwise
	 */
	public boolean isScheduled(){
		return indexScheduled;
	}
//	
//	/**
//	 * This method schedule the creation of indexes for this sp
//	 */
//	public void schedule(){
//		indexScheduled=true;
//	}
//	
//	/**
//	 * This method removes indexes already created for this sp
//	 */
//	public void unschedule(){
//		indexScheduled=false;
//		System.out.println("UNSCHEDULED");
//	}
	
	public float getRemainingTime(){
		float last=0;
		if(nextNodeToCheck==node.size()-1)	return last;
		for(int i=nextNodeToCheck-1; i<getNumberOfEdges(); i++){
			last=last+edge.get(i).getDuration();
		}
		return last;
	}
	
	public long getIndexActiveTime(){
		float last=0;
		
		for(int i=nextNodeToCheck-1; i<getNumberOfEdges(); i++){
			last=last+edge.get(i).getDuration();
			last=last+edge.get(i).getVariance();
		}
		return (long) last;
	}
	
	/**
	 * This method is used to validate the state of a sequential pattern. 
	 * A valid state has two conditions:
	 * - #edges=#nodes-1 (if there is almost a node)
	 * - the number of instances of each edge must be equal to the one of the other edeges
	 * 
	 * @throws InvalidSequentialPatternException
	 */
	private void validateState() throws InvalidSequentialPatternException {
		if(edge.size() != node.size()-1 && node.size()>=2)	throw new InvalidSequentialPatternException("Invalid edge");
		
		//TUTTE LE LISTE PER OGNI ISTANZA DEVONO ESSERE LUNGHE UGUALI
		for(int i=0; i<edge.size()-2;i++){
			if(edge.get(i).getNumberOfInstances()!=edge.get(i+1).getNumberOfInstances())	throw new InvalidSequentialPatternException("Invalid edgeInstancesDuration");
		}
	}
	
	/**
	 * This method returns the last query of the sp (in fact, the teQuery)
	 * @return
	 */
	public int getTeQuery(){
		return node.get(node.size()-1);
	}
	
	/**
	 * AUTO-GENERATED hashCode method
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edge == null) ? 0 : edge.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	/**
	 * AUTO-GENERATED equals method
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequentialPattern other = (SequentialPattern) obj;
		if (edge == null) {
			if (other.edge != null)
				return false;
		} else if (!edge.equals(other.edge))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	/**
	 * This method puts all the information about the sequential pattern in a well formatted string
	 */
	public String toString(){
		String s = node.get(0).toString();
			if(node.size()>1){
				for(int i=1; i<node.size()-1; i++){
					s=s+" | "+edge.get(i-1).getDuration()/1000+" # "+edge.get(i-1).getVariance()/1000+" | "+node.get(i);
				}
				s=s+" | "+edge.get(edge.size()-1).getDuration()/1000+" # "+edge.get(edge.size()-1).getVariance()/1000+" | "+node.get(node.size()-1);
			}
			s=s+" - @SUP: "+sup;
		return s;
	}
}
