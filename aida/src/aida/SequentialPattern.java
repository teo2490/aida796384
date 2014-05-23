package aida;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.InvalidSequentialPatternException;

public class SequentialPattern {

	//It contains the id of the query
	private List<Integer> node;
	//It contains the middle value of the duration of each edge
	private List<Long> edgeDuration;
	//It contains the values of the duration of the edges in each istances of the sequential pattern in the input log
	//Key 1 refers to edge 1 and the value is the list of duration of each instance of edge 1 found in the input log
	private Map<Integer, ArrayList<Long>> edgeInstancesDuration;
	
	public SequentialPattern() throws InvalidSequentialPatternException{
		node = new ArrayList<Integer>();
		edgeDuration = new ArrayList<Long>();
		edgeInstancesDuration = new HashMap<Integer, ArrayList<Long>>();
		validateState();
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
			ArrayList<Long> l = new ArrayList<Long>();
			edgeInstancesDuration.put(node.size()-2, l);
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
	
	//DA CONTROLLARE!!!
	public void findSequentialPattern(List<Integer> ql, List<Date> tl) throws InvalidSequentialPatternException{
		int k=0;
		//It counts how many edge is inserted for an hypotethical instance of sp. If the sp is not complete, it is used to remove 
		//the data already inserted.
		int added=0;
		Date start=null, end=null;
		long diff;
		ArrayList<Long> la;
		
		for(int i=0; i<ql.size(); i++){
			if(ql.get(i)==node.get(k)){
				if(start==null)	start=tl.get(i);
				else end = tl.get(i);
				
				if(end!=null) {
					la = edgeInstancesDuration.get(k-1);
					diff=end.getTime()-start.getTime();
					diff=diff/1000;	//Convert in seconds
					la.add(diff);
					edgeInstancesDuration.put((k-1), la);
					System.out.println("LA: "+la.size());
					//edgeInstancesDuration.get(k-1).add(diff);
					System.out.println("k: "+(k-1));
					//else	edgeInstancesDuration.get(0).add(diff);
					added++;
					//k e i a un passo prima perchè devo riprendere la stessa query che ha chiuso un arco come inizio del prossimo
					k--;
					i--;
					start = null;
					end = null;
				}
				//PASSO INTERA SEQUENZA O SOLO CHUNCK ??
				k++;
				if(k==node.size()) return;
			}
		}
		
		/*
		//Alla fine controllo che il sequential pattern sia intero
		if(k!=node.size()-1){
			//rimuovo le durate del sequential pattern non completo
			for(int j=0; j<added; j++){
				edgeInstancesDuration.get(j).remove(edgeInstancesDuration.get(j).size()-1);
			}
		}
		*/
		System.out.println("DURATION SIZE: "+edgeInstancesDuration.get(0).size());
		validateState();
		computeDuration();
	}
	
	public void computeDuration(){
		long middle;
		if(edgeDuration.size()>0){
			for(int i=0; i<edgeInstancesDuration.size();i++){
				middle=0;
				for(int j=0; j<edgeInstancesDuration.get(i).size(); j++){
					middle=middle+edgeInstancesDuration.get(i).get(j);
				}
				//middle=middle/edgeInstancesDuration.get(i).size();
				edgeDuration.add(i, middle);
			}
		}
		System.out.println("END");
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
