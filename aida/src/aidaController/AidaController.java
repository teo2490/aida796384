package aidaController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exception.InvalidSequentialPatternException;

import prefixspan.MainTestPrefixSpan_saveToFile;

import aidaModel.Manager;
import aidaModel.SequentialPattern;
import aidaView.AidaView;


/*** 
 * This class is the controller of the application. It make it works.
 *
 * Copyright (c) 2014 Matteo Simoni 796384
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

public class AidaController {
	AidaView view;
	private List<Manager> model;
	
	private List<Double> inSup;
	private List<Integer> inTime;
	
	private List<List<SequentialPattern>> sp;
	
	private boolean running;
	private Thread t;
	private String out="";
	private boolean once = false;
	
	private int qpos;
	private int tpos;
	
	public AidaController(AidaView v){
		this.view = v;
		this.running=false;
		model = new ArrayList<Manager>();
		
		inSup = new ArrayList<Double>();
		inTime = new ArrayList<Integer>();
		sp = new ArrayList<List<SequentialPattern>>();

		//This listener starts the TRAINING phase when the START button is clicked
		view.getStartBtn().addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//Getting back parameter
        		String inLog = view.getInputLog();
        		List<String> teQueryList = view.getInputTeQuery();
        		List<String> times = view.getInputTime();
        		List<String> sups = view.getInputSup();
        		if(inLog!= null && times!= null && sups!= null){
	        		for(int i=0; i<times.size(); i++){
	        			inSup.add(Double.parseDouble(sups.get(i)));
	        			inTime.add(Integer.parseInt(times.get(i)));
	        		}
	        		try {
	        			//Start the training ////////devo recuperare la teQuery
	        			for(int i=0; i<teQueryList.size(); i++){
	        				training(inLog, teQueryList.get(i), inSup.get(i), i);
	        			}
	        			out=out+"\n----- Enriched Sequential Pattern Found -----\n";
	        			view.printOutputLabel(out);
					} catch (Exception e) {
						e.printStackTrace();
					}
        		}
        		//view.printTrainingOutput("------------------------------------------------");
        		//view.printTrainingOutput("READY FOR FORECASTING PHASE! Change Tab.");
        	}
        });
		
		view.getStartFlowBtn().addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		running=true;
        		
        		try {
					forecasting();
				} catch (InterruptedException
						| InvalidSequentialPatternException e) {
					e.printStackTrace();
				}
        	}
        });
		
		view.getPauseFlowBtn().addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		running=false;
        	}
        });
		
		view.getStopFlowBtn().addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		running=false;
        		view.clearForecastingQueries();
        		view.clearForecastingOutput();
        		view.clearForecastingSpView();
        	}
        });
	}
	
	/**
	 * This method manages the training phase
	 * 
	 * @param inLog	The path of the input log
	 * @throws Exception
	 */
	public void training(String inLog, String teQuery, Double inSup, int count) throws Exception{
		qpos = view.getInputQueryPos();
		tpos = view.getInputTimestampPos();
		Manager md = new Manager(qpos, tpos);
		//Parsing the CSV input 
		md.parseCSVtoTXT(inLog,
				"C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan_"+count+".txt",
				teQuery);	 
		
		Map<String, Integer> association = md.getAssociationMap();
		//Printing association between string queries and their symbols
		if(once==false){
			out=out+"\n----- Query Symbol Association -----\n\n";
			//out=out+association.toString()+"\n";
			Set<String> set = association.keySet();
			Iterator<String> next = set.iterator();
			String p = next.next();
			out=out+p+" = "+association.get(p)+"\n";
			for(int i=0; i<=set.size(); i++){				
				if(next.hasNext()){
					p=next.next();
					out=out+p+" = "+association.get(p)+"\n";
				}
			}
			once=true;
		}
		
		//Printing the input log splitted in chunck
		out=out+"\n----- Chunck of input Log of query "+association.get(teQuery)+" -----\n\n";
		 Map<Integer, List<Integer>> chunck = md.getChunckMap();
		 for(int j=0; j<chunck.size(); j++){
			 out=out+chunck.get(j).toString()+"\n";
		 }
		
		// Launching PrefixSpan Algorithm..
		String[] arg = new String[3];
		arg[0]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan_"+count+".txt";
		arg[1]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan_"+count+".txt";
		arg[2]=Double.toString(inSup);
		MainTestPrefixSpan_saveToFile.main(arg);
		
		// Parsing PrefixSPan Output
		/* Now I have to parse outputPrefixSpan.txt in order to retrieve the frequent sequential patter that I need (the ones that
		contains the time-expensive query) and use them in order to calculate the "duration" of a pattern.. */
		sp.add(md.parseSP("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan_"+count+".txt"));
		
		//Finding Sequential Pattern in the chuncks of log
		md.findSP(sp.get(sp.size()-1));
		model.add(md);
		
		//out=out+"\n----- Enriched Sequential Pattern Found -----\n\n";
		view.printOutputLabel(out);
		System.out.println(out);
		for(int i=0; i<sp.get(count).size(); i++){
			view.printSequentialPattern(sp.get(sp.size()-1).get(i));
		}
	}
	
	public void forecasting() throws InterruptedException, InvalidSequentialPatternException{
//		Simulator initiater = new Simulator(5);
//		Listener r1 = new Listener(sp, inTime);
//
//		initiater.addListener(r1);
//
//		System.out.println("\nSTARTING EXECUTION!\n");
//  
//		for(int i=0; i<10; i++){
//			//Long pausing time in order to make some partial so invalid due to time constraint
//			if(i==6)	Thread.sleep(120000);
//			initiater.makeQuery();
//		}
//		//Used in place of the for-block for ad-hoc testin purposes
//		initiater.makeQuery();
		final Simulator initiater = new Simulator(5, view);
		
		for(int i=0; i<sp.size(); i++){
			Listener r1 = new Listener(sp.get(i), inTime.get(i), view);
			initiater.addListener(r1);
		}

		view.printForecastingQueries("\nSTARTING EXECUTION!\n");
		
		t = new Thread(){
			public void run(){
				int q = -1;
				
				initiater.getSet();

				while(running){
					try {
						Thread.sleep(1000);
						if(running){
							q = initiater.makeQuery();
							view.printForecastingQueries("Query "+q+" executed! @ "+System.nanoTime());
						}
					} catch (InterruptedException | InvalidSequentialPatternException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

}
