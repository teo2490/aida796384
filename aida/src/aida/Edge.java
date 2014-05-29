package aida;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the object that is between two nodes in a sequential pattern.
 * 
 * @author Matteo Simoni 796384
 *
 */
public class Edge {

	//It is the mean value of the instances of the edge
	private float duration;
	//It is the variance value of the instances of the edge
	private float variance;
	//It contains the values of the duration of the edges in each istances of the sequential pattern in the input log
	private List<Long> instancesDuration;
	
	/**
	 * Default constructor. It sets the attributes to an invalid value.
	 */
	public Edge(){
		duration=-1;
		variance=-1;
		instancesDuration = new ArrayList<Long>();
	}
	
	/* Start of the getter and setter methods */
	public float getDuration(){
		return duration;
	}
	
	public float getVariance(){
		return variance;
	}
	
	public int getNumberOfInstances(){
		return instancesDuration.size();
	}
	/* End of the getter and setter methods */
	
	/**
	 * This method adds the measure of an instance of the edge to the edge itself.
	 * 
	 * @param i	The measure of the instance
	 */
	public void addInstance(long i){
		instancesDuration.add(i);
	}
	
	/**
	 * This method removes the measure of the last instance of the edge.
	 */
	public void removeLastInstance(){
		instancesDuration.remove(instancesDuration.size()-1);
	}
	
	/**
	 * This methods compute the mean and the variance of the set of measurements of the instances.
	 */
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
