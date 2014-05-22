package aida;
import prefixspan.MainTestPrefixSpan_saveToFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class read a CSV file and create a TXT file with a predefined structure.
 * In the TXT file there is not reported the text query, but at each type of query is associated an ID used in the TXT file
 * and there is a Map that keeps the association between query and ID.
 * 
 * The value "-2" indicates the end of a sequence (it appears at the end of each line) and "-1" indicates the end of an itemset.
 *  
 * Moreover this class keeps an Arraylist of the query and an ArrayList of the timestamp. (At position 32 of timestampList there is
 * the timestamp of the query at the position 32 in the queryList.)
 * 
 * When the propedeutical phase is done, this class call the algorithm for finding sequential pattern (it writes the pattern in
 * a file) and then call the ReadSP class that parse the TXT output file written by the algorithm.
 * 
 * @author Matteo Simoni 796384
 *
 */
 
public class ReadCSVtoTXT {
	
	//Keeps the association between the text of the query and its ID
	Map<String, Integer> association;
	//Keeps the entire list (with duplicate) of identifiers of queries in the log
	ArrayList<Integer> queryList;
	Map<Integer, List<Integer>> queryMap;
	//Keeps the timestamp corresponding to the query in queryList
	ArrayList<Date> timestampList;
	Map<Integer, List<Date>> timestampMap;
	//Constant that indicates the position of the query in a row of the log
	static final int QUERYPOS = 1;
	//Constant that indicates the position of the timestamp of the current query in a row of the log
	static final int TSPOS = 2;
	 
	  public static void main(String[] args) throws Exception {
	 
		ReadCSVtoTXT obj = new ReadCSVtoTXT();
		obj.parseCSVtoTXT("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\log_new.csv",
				"C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan.txt",
				"SELECT * FROM sensors");	 
	  }
	  
	 
	  public void parseCSVtoTXT(String csvPath, String outputPath, String teQuery) throws Exception {
	 
		//Three parameter of the tool (input file, output file, query time-expensive
		String csvFile = csvPath;
		FileOutputStream output = new FileOutputStream(outputPath);
		String TEquery = teQuery;
		int idTE = -1; //Initialized to an invalid value
		//----
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ", ";
		//The incremental identifier
		int key = 0;
        PrintStream outWriter = new PrintStream(output);
        int chunck = 0;
        
        association = new HashMap<String, Integer>();
        queryList = new ArrayList<Integer>();
        timestampList = new ArrayList<Date>();
        queryMap = new HashMap<Integer, List<Integer>>();
        timestampMap = new HashMap<Integer, List<Date>>();
        
        System.out.println("Starting TXT file preparation..");
        
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				
				key = association.size();
				
				// use comma as separator
				String[] token = line.split(cvsSplitBy);
				
				//Check if an ID is already associated to the current query, if not it associates a new one
				if(association.get(token[QUERYPOS]) == null){
					key++;
					association.put(token[QUERYPOS], key);
				}else{
					key = association.get(token[QUERYPOS]);
				}
				
				//If the query analyzed in this moment is the time-expensive query and it's the first time that it is found, its
				//associated ID is stored in idTE
				if(TEquery.equals(token[QUERYPOS]) && idTE==-1){
					idTE = key;
				}

				//Timestamp ts = Timestamp.valueOf(token[TSPOS]);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date ts = dateFormat.parse(token[TSPOS]);
				//Add the query to the list
				queryList.add(key);
				
				//Add the timestamp to the list
				timestampList.add(ts);
				
			}
			
			for (int i=0;i<queryList.size();i++){
				outWriter.print(queryList.get(i)+" -1 ");
				
				if(queryList.get(i)==idTE){
					queryMap.put(chunck, queryList.subList(0, i));
					timestampMap.put(chunck, timestampList.subList(0, i));
					outWriter.println("-2");
					chunck++;
				}
			}
			outWriter.println("-2");
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("TEXT file created");
		System.out.println("Parsing input done.");
		
		/* DEBUG for testing purposes */
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
		for(Date t: timestampList)	System.out.println(t.toString());
		System.out.println("------------");
		for(int q: queryList)	System.out.println(q);
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
		
		
		// Launching PrefixSpan Algorithm..
		System.out.println("Launching PrefixSpan Algorithm..\n");
		String[] args = new String[2];
		args[0]=outputPath;
		args[1]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt";
		MainTestPrefixSpan_saveToFile.main(args);
		System.out.println("Sequential pattern search done.\n");
		
		/* Now I have to parse outputPrefixSpan.txt in order to retrieve the frequent sequential patter that I need (the ones that
		contains the time-expensive query) and use them in order to calculate the "duration" of a pattern.. */
		ReadSP spReader = new ReadSP();
		//Map<Integer, List<Integer>> sp = spReader.parseSP("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt",idTE);
		List<SequentialPattern> sp = spReader.parseSP("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt",idTE);
		
		for(int i=0; i<sp.size(); i++){
			for(int j=0; j<queryMap.size();j++){
				sp.get(i).findSequentialPattern(queryMap.get(j), timestampMap.get(j));
			}
		}
		
		/* DEBUG for testing purposes */
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
		for(int j=0;j<sp.size();j++){
			SequentialPattern dbg = new SequentialPattern();
//			dbg = sp.get(j);
//			for(int k: dbg){
//				System.out.print(k+" ");
//			}
//			System.out.print("\n");
			dbg=sp.get(j);
			System.out.println(dbg.toString());
		}
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
		
		// "Duration" computation

	  }
	  
}