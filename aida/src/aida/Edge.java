package aida;

import java.util.ArrayList;
import java.util.List;

public class Edge {

	private Long duration;
	private Long variance;
	//It contains the values of the duration of the edges in each istances of the sequential pattern in the input log
	private List<Long> instancesDuration;
	
	public Edge(){
		duration=null;
		variance=null;
		instancesDuration = new ArrayList<Long>();
	}
	
	public Long getDuration(){
		return duration;
	}
	
	public Long getVariance(){
		return variance;
	}
	
	public int getNumberOfInstances(){
		return instancesDuration.size();
	}
	
	public void addInstance(long i){
		instancesDuration.add(i);
	}
	
	public void removeLastInstance(){
		instancesDuration.remove(instancesDuration.size()-1);
	}
	
	public void computeDurationAndVariance(){
		long middle = 0;
		if(instancesDuration.size()>0){
			//Computing duration
			for(int i=0; i<instancesDuration.size();i++){
					middle=middle+instancesDuration.get(i);
				}
			middle=middle/instancesDuration.size();
			duration = middle;
		
			//Computing Variance
			//..
		}
	}
	
	
}
