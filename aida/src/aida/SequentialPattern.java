package aida;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exception.InvalidSequentialPatternException;

/**
 * This class is the object that stands for a sequential pattern. It has a list of node (element of the sequential pattern) and a
 * list of edges (the object between nodes).
 * 
 * @author Matteo 796384
 *
 */
public class SequentialPattern {

	//It contains the id of the query
	private List<Integer> node;
	//It contains the middle value of the duration of each edge
	private List<Edge> edge;
	
	public SequentialPattern() throws InvalidSequentialPatternException{
		node = new ArrayList<Integer>();
		edge = new ArrayList<Edge>();
		validateState();
	}
	
	public List<Integer> getAllNodes(){
		return node;
	}
	
	public int getNumberOfNodes(){
		return node.size();
	}
	
	public int getNumberOfEdges(){
		return edge.size();
	}
	
	public void addNode(int n) throws InvalidSequentialPatternException{
		node.add(n);
		if(node.size()>=2){
			edge.add(new Edge());
			validateState();
		}

	}
	
	public int getNode(int n){
		return node.get(n);
	}
	
	public long getDuration(int e){
		return edge.get(e).getDuration();
	}
	
	/**
	 * This function finds if the sequential is in the list of query passed as a paramenter. If it is, it compute the duration
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
					diff=diff/1000;	//Convert in seconds
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
	
	public void computeDuration(){
		for(int i=0; i<edge.size(); i++){
			edge.get(i).computeDurationAndVariance();
		}
	}
	
	private void validateState() throws InvalidSequentialPatternException {
		if(edge.size() != node.size()-1 && node.size()>=2)	throw new InvalidSequentialPatternException("Invalid edge");
		
		//TUTTE LE LISTE PER OGNI ISTANZA DEVONO ESSERE LUNGHE UGUALI
		for(int i=0; i<edge.size()-2;i++){
			if(edge.get(i).getNumberOfInstances()!=edge.get(i+1).getNumberOfInstances())	throw new InvalidSequentialPatternException("Invalid edgeInstancesDuration");
		}
	}
	
	public String toString(){
		String s = node.get(0).toString();
			if(node.size()>1){
				for(int i=1; i<node.size()-1; i++){
					s=s+" |"+edge.get(i-1).getDuration()+"| "+node.get(i);
				}
				s=s+" |"+edge.get(edge.size()-1).getDuration()+"| "+node.get(node.size()-1);
			}
		return s;
	}
}
