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
	private List<Long> edgeDuration;
	//It contains the values of the duration of the edges in each istances of the sequential pattern in the input log
	//Position 1 refers to edge 1 and the values are the list of duration of each instance of edge 1 found in the input log
	private List<List<Long>> edgeInstancesDuration;
	
	public SequentialPattern() throws InvalidSequentialPatternException{
		node = new ArrayList<Integer>();
		edgeDuration = new ArrayList<Long>();
		edgeInstancesDuration = new ArrayList<List<Long>>();
		validateState();
	}
	
	public List<Integer> getAllNodes(){
		return node;
	}
	
	public int getNumberOfNodes(){
		return node.size();
	}
	
	public int getNumberOfEdges(){
		return edgeDuration.size();
	}
	
	public int getNumberofInstanceEdges(){
		return edgeInstancesDuration.size();
	}
	
	public void addNode(int n) throws InvalidSequentialPatternException{
		node.add(n);
		if(node.size()>=2){
			edgeDuration.add(null);
			edgeInstancesDuration.add(new ArrayList<Long>());
		}
		validateState();
	}
	
	public void addDuration(long duration, int pos){
		edgeDuration.set(pos, duration);
	}
	
	public int getNode(int n){
		return node.get(n);
	}
	
	public long getDuration(int e){
		return edgeDuration.get(e);
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
					edgeInstancesDuration.get(k-1).add(diff);
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
				edgeInstancesDuration.get(j).remove(edgeInstancesDuration.get(j).size()-1);
			}
		}

		validateState();
	}
	
	/**
	 *  This function computes the middle of the duration for each edge in edgeInstanceDuration and saves it in the corresponding
	 *  place in edgeDuration.
	 */
	public void computeDuration(){
		long middle;
		if(edgeDuration.size()>0 && edgeInstancesDuration.get(0).size()>0){
			for(int i=0; i<edgeInstancesDuration.size();i++){
				middle=0;
				for(int j=0; j<edgeInstancesDuration.get(i).size(); j++){
					middle=middle+edgeInstancesDuration.get(i).get(j);
				}
				middle=middle/edgeInstancesDuration.get(i).size();
				edgeDuration.set(i, middle);
			}
		}
	}
	
	private void validateState() throws InvalidSequentialPatternException {
		if(edgeDuration.size() != node.size()-1 && node.size()>=2)	throw new InvalidSequentialPatternException("Invalid edgeDuration");
		
		//TUTTE LE LISTE PER OGNI ISTANZA DEVONO ESSERE LUNGHE UGUALI
		for(int i=0; i<edgeInstancesDuration.size()-1;i++){
			if(edgeInstancesDuration.get(i).size()!=edgeInstancesDuration.get(i+1).size())	throw new InvalidSequentialPatternException("Invalid edgeInstancesDuration");
		}
	}
	
	public String toString(){
		String s = node.get(0).toString();
			if(node.size()>1){
				for(int i=1; i<node.size()-1; i++){
					s=s+" |"+edgeDuration.get(i-1)+"| "+node.get(i);
				}
				s=s+" |"+edgeDuration.get(edgeDuration.size()-1)+"| "+node.get(node.size()-1);
			}
		return s;
	}
}
