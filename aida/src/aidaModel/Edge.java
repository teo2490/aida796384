package aidaModel;

import java.util.ArrayList;
import java.util.List;


/*** 
 * This class represent the object that is between two nodes in a sequential pattern.
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

public class Edge {

	//It is the mean value of the instances of the edge
	private float duration;
	//It is the variance value of the instances of the edge
	private float variance;
	//It contains the values of the duration of the edges in each istances of the sequential pattern in the input log
	private List<Double> instancesDuration;

	/**
	 * Default constructor. It sets the attributes to an invalid value.
	 */
	public Edge(){
		duration=-1;
		variance=-1;
		instancesDuration = new ArrayList<Double>();
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
	public void addInstance(double i){
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
		double middle = 0;
//		float sumsq = 0;
//		float v;
		if(instancesDuration.size()>0){
			//Computing duration
			for(int i=0; i<instancesDuration.size();i++){
					System.out.println("D: "+instancesDuration.get(i));
					middle=middle+instancesDuration.get(i);
				}
			duration= (float) middle/instancesDuration.size();
			//duration = middle;
			
			//Computing Variance
//			for(int j=0; j<instancesDuration.size() ; j++){
//	            //sumsq = sumsq + ((instancesDuration.get(j)-middle) * ((instancesDuration.get(j)-middle)));
//	            sumsq = sumsq + ((instancesDuration.get(j)) * ((instancesDuration.get(j))));
//	        }
//	        v = (float) (sumsq / instancesDuration.size()) - (middle / instancesDuration.size());
//	        variance=v;
	        
	        double min=(double)duration;
	        double max=0;
	        for(int j=0; j<instancesDuration.size() ; j++){
	        	if(instancesDuration.get(j)<min)	min=instancesDuration.get(j);
	        	//< ??
	        	else if(instancesDuration.get(j)>max)	max=instancesDuration.get(j);
	        }
	        
//	        float diff1=duration- (float) min;
//	        float diff2=(float)max-duration;
//	        
//	        if(diff1>diff2)	variance=diff1;
//	        else	variance=diff2;
	        
	        variance = (float) (max-min)/2 + 1;
		}
	}
	
	/**
	 * AUTO-GENERATED hashCode method
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(duration);
		result = prime * result + Float.floatToIntBits(variance);
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
		Edge other = (Edge) obj;
		if (Float.floatToIntBits(duration) != Float
				.floatToIntBits(other.duration))
			return false;
		if (Float.floatToIntBits(variance) != Float
				.floatToIntBits(other.variance))
			return false;
		return true;
	}
	
}
