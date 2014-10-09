package aidaModel;

import java.util.TimerTask;

public class IndexTimer extends TimerTask{
	
	public TeQueriesState teqState;
	public int teQuery;
	
	public IndexTimer(TeQueriesState t, int q){
		teqState = t;
		teQuery = q;
	}
	
	@Override
	public void run() {
		teqState.removeIndex(teQuery);
		System.out.println("TimerTask Exectuted! Removed index for "+teQuery);
	}

}
