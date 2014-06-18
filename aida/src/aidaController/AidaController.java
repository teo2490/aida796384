package aidaController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import prefixspan.MainTestPrefixSpan_saveToFile;

import aida.Manager;
import aida.SequentialPattern;
import aidaView.AidaView;

public class AidaController {
	private AidaView view;
	private Manager model;
	
	private	float inSup;
	private int inTime;
	
	private List<SequentialPattern> sp;
	
	public AidaController(Manager m, AidaView v){
		this.view = v;
		this.model = m;

		//This listener starts the TRAINING phase when the START button is clicked
		view.getStartBtn().addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//Getting back parameter
        		List<String> s = view.getInputParameter();
        		if(s.get(0)!= null && s.get(1)!= null && s.get(2)!= null){
	        		String inLog = s.get(0);
	        		String inTimeSting = s.get(1);
	        		String inSupString = s.get(2);
	        		inSup = Float.parseFloat(inSupString);
	        		inTime = Integer.parseInt(inTimeSting);
	        		try {
	        			//Start the training
	        			training(inLog);
					} catch (Exception e) {
						e.printStackTrace();
					}
        		}
        		view.printTrainingOutput("------------------------------------------------");
        		view.printTrainingOutput("READY FOR FORECASTING PHASE! Change Tab.");
        	}
        });
	}
	
	public void training(String inLog) throws Exception{
		view.printTrainingOutput("----------------------- Training Part output -----------------------");
		
		//Parsing the CSV input 
		model.parseCSVtoTXT(inLog,
				"C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan.txt",
				"SELECT * FROM sensors");	 
		
		//Printing association between string queries and their symbols
		Map<String, Integer> association = model.getAssociationMap();
		view.printTrainingOutput("\n----- Query Symbol Association -----\n");
		view.printTrainingOutput(association.toString());
		view.printTrainingOutput("\n------------------------------------------------");
		
		//Printing the input log splitted in chunck
		view.printTrainingOutput("\n----- Chunck of input Log -----\n");
		 Map<Integer, List<Integer>> chunck = model.getChunckMap();
		 for(int j=0; j<chunck.size(); j++){
			 view.printTrainingOutput(chunck.get(j).toString());
		 }
		 view.printTrainingOutput("\n------------------------------------------------");
		
		// Launching PrefixSpan Algorithm..
		view.printTrainingOutput("\nLaunching PrefixSpan Algorithm..\n");
		String[] arg = new String[2];
		arg[0]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan.txt";
		arg[1]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt";
		MainTestPrefixSpan_saveToFile.main(arg);
		view.printTrainingOutput("Sequential pattern search done.\n");
		
		// Parsing PrefixSPan Output
		/* Now I have to parse outputPrefixSpan.txt in order to retrieve the frequent sequential patter that I need (the ones that
		contains the time-expensive query) and use them in order to calculate the "duration" of a pattern.. */
		sp = model.parseSP("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt");
		
		//Finding Sequential Pattern in the chuncks of log
		model.findSP(sp);
		
		view.printTrainingOutput("----- Enriched Sequential Pattern Found -----\n");
		for(int i=0; i<sp.size(); i++){
			view.printTrainingOutput(sp.get(i).toString()+"\n");
		}
	}
	
	public void forecasting(){
		
	}

}
