package aidaModel;

import java.util.HashMap;
import java.util.Map;

public class TeQueriesState {

	//Keep association between a teQuery (Key) and the number of its occurences (Value)
	private Map<Integer, Integer> teqCount;
	//Keep association between a teQuery (Key) and a boolean that says of the index for that query are implemented or not (Value)
	private Map<Integer, Boolean> teqIndex;
	
	//teQuery execution forecasted and teQuery REALLY executed
	private Map<Integer, Integer> teqGoodPrevisionCounter;
	//teQuery execution forecasted and teQuery NOT executed
	private Map<Integer, Integer> teqBadPrevisionCounter;
	
	public TeQueriesState() {
		super();
		teqCount = new HashMap<Integer, Integer>();
		teqIndex = new HashMap<Integer, Boolean>();
		teqGoodPrevisionCounter = new HashMap<Integer, Integer>();
		teqBadPrevisionCounter = new HashMap<Integer, Integer>();
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
	}
	
	/**
	 * This method sets the index for the teQuery.
	 * @param teQuery
	 */
	public void createIndex(Integer teQuery){
		teqIndex.put(teQuery, true);
	}
	
	/**
	 * This method removes the index for the teQuery.
	 * @param teQuery
	 */
	public void removeIndex(Integer teQuery){
		teqIndex.put(teQuery, false);
	}
	
	/**
	 * This method increments the count of appearence of the teQuery.
	 * @param teQuery
	 */
	public void incrementAppearanceNumber(Integer teQuery){
		teqCount.put(teQuery, teqCount.get(teQuery)+1);
		
		if(teqIndex.get(teQuery)==true)	teqGoodPrevisionCounter.put(teQuery, teqGoodPrevisionCounter.get(teQuery)+1);
	}
	
}
