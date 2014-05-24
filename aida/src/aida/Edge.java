package aida;

import java.util.ArrayList;
import java.util.List;

public class Edge {

	private float duration;
	private float variance;
	//It contains the values of the duration of the edges in each istances of the sequential pattern in the input log
	private List<Long> instancesDuration;
	
	public Edge(){
		duration=-1;
		variance=-1;
		instancesDuration = new ArrayList<Long>();
	}
	
	public float getDuration(){
		return duration;
	}
	
	public float getVariance(){
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
		float middle = 0;
		float sumsq = 0;
		float v;
		if(instancesDuration.size()>0){
			//Computing duration
			for(int i=0; i<instancesDuration.size();i++){
					middle=middle+instancesDuration.get(i);
				}
			middle=middle/instancesDuration.size();
			duration = middle;
			
			//Computing Variance
			for (int j=0; j<instancesDuration.size() ; j++) 
	        {
	            sumsq = sumsq + ((instancesDuration.get(j)-middle) * ((instancesDuration.get(j)-middle)));
	        }
	        v = (float) sumsq / instancesDuration.size();
	        variance=v;
		}
	}
	
	
}
