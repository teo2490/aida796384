package forecast;

import java.util.ArrayList;
import java.util.List;

import aida.SequentialPattern;

public class Listener implements QueryListener  {
	
	//The list of the known sequential pattern
	private List<SequentialPattern> sp;
	//The list of the sequential pattern partially found in the flow of query
	private List<SequentialPattern> spOnGoing = new ArrayList<SequentialPattern>();
	
	public Listener(List<SequentialPattern> sp){
		this.sp=sp;
	}
	
	@Override
    public void someoneMadeQuery(int q) {
        System.out.println("Query "+q+" catched!");
        
        if(spOnGoing.size()>0){
	        for(int j=0; j<spOnGoing.size();j++){
	        	if(spOnGoing.get(j).getNextNodeToCheck() == spOnGoing.get(j).getNumberOfNodes()){
	        		System.out.println("Sequential Pattern Found! "+spOnGoing.get(j).toString());
	        		spOnGoing.remove(j);
	        	}
	        	else if(spOnGoing.get(j).getNode(spOnGoing.get(j).getNextNodeToCheck()) == q){
	        		spOnGoing.get(j).incrementNextNodeToCheck();
	        	}
	        }
        }
        
        for(int i=0;i<sp.size();i++){
        	if(sp.get(i).getNode(0) == q)	spOnGoing.add(sp.get(i));
        }
    }
	
	public int spOnGoingSize(){
		return spOnGoing.size();
	}
}
