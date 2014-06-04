package aida;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exception.InvalidSequentialPatternException;

/**
 * This class is the object that stands for a sequential pattern. It has a list of node (element of the sequential pattern) and a
 * list of edges (the object between nodes).
 * 
 * @author Matteo Simoni 796384
 *
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
		validateState();
	}
	
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
	 * This function finds if the sequential is in the list of query passed as a parameter. If it is, it compute the duration
	 * of each edge from the corresponding list timestamp.
	 * 
	 * @param ql The list of query
	 * @param tl The list of timestamp (at position 0 there is the timestamp of the query at position 0 in ql)
	 * @throws InvalidSequentialPatternException
	 */
	public void findSequentialPattern(List<Integer> ql, List<Date> tl) throws InvalidSequentialPatternException{
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
					//diff=diff/1000;	//Convert in seconds
					edge.get(k-1).addInstance(diff);
					added++;
					//k and i are decremented because the query that has closed an edge will be the same that will open the next one
					k--;
					i--;
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

		validateState();
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
	 * This method puts all the information about the sequential pattern in a well formatted string
	 */
	public String toString(){
		String s = node.get(0).toString();
			if(node.size()>1){
				for(int i=1; i<node.size()-1; i++){
					s=s+" |"+edge.get(i-1).getDuration()+"#"+edge.get(i-1).getVariance()+"| "+node.get(i);
				}
				s=s+" |"+edge.get(edge.size()-1).getDuration()+"#"+edge.get(edge.size()-1).getVariance()+"| "+node.get(node.size()-1);
			}
			s=s+" - @SUP: "+sup;
		return s;
	}
}
