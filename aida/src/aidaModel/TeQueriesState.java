package aidaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import aidaView.GraphDesigner;

public class TeQueriesState {

	//Keep association between a teQuery (Key) and the number of its occurences (Value)
	private Map<Integer, Integer> teqCount;
	//Keep association between a teQuery (Key) and a boolean that says of the index for that query are implemented or not (Value)
	private Map<Integer, Boolean> teqIndex;
	
	//teQuery execution forecasted and teQuery REALLY executed
	private Map<Integer, Integer> teqGoodPrevisionCounter;
	//teQuery execution forecasted and teQuery NOT executed
	private Map<Integer, Integer> teqBadPrevisionCounter;
	//teQuery execution NOT forecasted, but teQuery executed
	private Map<Integer, Integer> teqMissedPrevisionCounter;
	
	private Map<Integer, ArrayList<Float>> precisionValues;
	private Map<Integer, ArrayList<Float>> recallValues;
	private Map<Integer, Timer> timerList;
	
	public TeQueriesState() {
		super();
		teqCount = new HashMap<Integer, Integer>(); //Denominatore Recall
		teqIndex = new HashMap<Integer, Boolean>();
		teqGoodPrevisionCounter = new HashMap<Integer, Integer>(); //Numeratore Precision e Numeratore Recall. Parte di denominatore Precision.
		teqBadPrevisionCounter = new HashMap<Integer, Integer>(); 
		teqMissedPrevisionCounter = new HashMap<Integer, Integer>(); //Parte di denominatore Precision.
		
		precisionValues = new HashMap<Integer, ArrayList<Float>>();
		recallValues = new HashMap<Integer, ArrayList<Float>>();
		timerList = new HashMap<Integer, Timer>();
	}

	/**
	 * @return the teqCount
	 */
	public Map<Integer, Integer> getTeqCount() {
		return teqCount;
	}

	/**
	 * @param teqCount the teqCount to set
	 */
	public void setTeqCount(Map<Integer, Integer> teqCount) {
		this.teqCount = teqCount;
	}

	/**
	 * @return the teqGoodPrevisionCounter
	 */
	public Map<Integer, Integer> getTeqGoodPrevisionCounter() {
		return teqGoodPrevisionCounter;
	}

	/**
	 * @param teqGoodPrevisionCounter the teqGoodPrevisionCounter to set
	 */
	public void setTeqGoodPrevisionCounter(
			Map<Integer, Integer> teqGoodPrevisionCounter) {
		this.teqGoodPrevisionCounter = teqGoodPrevisionCounter;
	}

	/**
	 * @return the teqBadPrevisionCounter
	 */
	public Map<Integer, Integer> getTeqBadPrevisionCounter() {
		return teqBadPrevisionCounter;
	}

	/**
	 * @param teqBadPrevisionCounter the teqBadPrevisionCounter to set
	 */
	public void setTeqBadPrevisionCounter(
			Map<Integer, Integer> teqBadPrevisionCounter) {
		this.teqBadPrevisionCounter = teqBadPrevisionCounter;
	}

	/**
	 * @return the teqIndex
	 */
	public Map<Integer, Boolean> getTeqIndex() {
		return teqIndex;
	}

	/**
	 * @param teqIndex the teqIndex to set
	 */
	public void setTeqIndex(Map<Integer, Boolean> teqIndex) {
		this.teqIndex = teqIndex;
	}
	
	/**
	 * This method adds a new teQuery to the list.
	 * @param teQuery
	 */
	public void addTeQuery(Integer teQuery){
		teqCount.put(teQuery, 0);
		teqIndex.put(teQuery, false);
		teqGoodPrevisionCounter.put(teQuery, 0);
		teqBadPrevisionCounter.put(teQuery, 0);
		teqMissedPrevisionCounter.put(teQuery, 0);
		
		ArrayList<Float> r = new ArrayList<Float>();
		recallValues.put(teQuery, r);
		
		ArrayList<Float> p = new ArrayList<Float>();
		precisionValues.put(teQuery, p);
		
		timerList.put(teQuery, null);
	}
	
	/**
	 * This method says if index for a teQuery q is already set or not.
	 * @param q
	 * @return
	 */
	public boolean indexIsSet(int q){
		return teqIndex.get(q);
	}
	
	/**
	 * This method sets the index for the teQuery.
	 * @param teQuery
	 */
	public void createIndex(Integer teQuery, long time){
		teqIndex.put(teQuery, true);
		
		IndexTimer task = new IndexTimer(this, teQuery);
		Timer t = new Timer(true);
		t.schedule(task, time);
		
		if(timerList.get(teQuery)!=null)	timerList.get(teQuery).cancel();

		timerList.put(teQuery, t);
		
		System.out.println("INDEX CREATED IN TEQUERIESSTATE CLASS!");
	}
	
	/**
	 * This method removes the index for the teQuery.
	 * @param teQuery
	 */
	public void removeIndex(Integer teQuery){
		teqIndex.put(teQuery, false);
		
		timerList.get(teQuery).cancel();
		timerList.put(teQuery, null);
		
		System.out.println("INDEX REMOVED IN TEQUERIESSTATE CLASS!");
	}
	
	public void removeIndexTimeOut(Integer teQuery){
		teqMissedPrevisionCounter.put(teQuery, teqMissedPrevisionCounter.get(teQuery)+1);
		removeIndex(teQuery);
	}
	
	/**
	 * This method increments the count of appearence of the teQuery.
	 * @param teQuery
	 */
	public void incrementAppearanceNumber(Integer teQuery){
		teqCount.put(teQuery, teqCount.get(teQuery)+1);
		
		if(teqIndex.get(teQuery)==true)	teqGoodPrevisionCounter.put(teQuery, teqGoodPrevisionCounter.get(teQuery)+1);
	}
	
	public void computePartialPrecision(){
		Set<Integer> keySet = teqCount.keySet();
		for(Integer key:keySet){
			//Computing partial precision
			float r;
			int denominator = teqGoodPrevisionCounter.get(key)+teqMissedPrevisionCounter.get(key);
			if(denominator==0)	r=-1;	//Null value for Recall due to no occurences of the teQuery in the interval.
			else	r = (float) teqGoodPrevisionCounter.get(key) / denominator;
			precisionValues.get(key).add(r);
		}
	}
	
	public void computePartialRecall(){
		Set<Integer> keySet = teqCount.keySet();
		for(Integer key:keySet){
			//Computing partial recall
			float r;
			if(teqCount.get(key)==0)	r=-1;	//Null value for Recall due to no occurences of the teQuery in the interval.
			else	r = (float) teqGoodPrevisionCounter.get(key) / teqCount.get(key);
			recallValues.get(key).add(r);
		}
	}
	
	public void resetValuesAfterPartialComputations(){
		Set<Integer> keySet = teqCount.keySet();
		for(Integer key:keySet){
			//reset values
			teqCount.put(key, 0);
			teqIndex.put(key, false);
			teqGoodPrevisionCounter.put(key, 0);
			teqBadPrevisionCounter.put(key, 0);
			teqMissedPrevisionCounter.put(key, 0);
		}
	}
	
	public void paintPrecision(){
		@SuppressWarnings("unused")
		GraphDesigner d = new GraphDesigner(precisionValues, 0);
	}
	
	public void paintRecall(){
		@SuppressWarnings("unused")
		GraphDesigner d = new GraphDesigner(recallValues, 1);
	}
}
