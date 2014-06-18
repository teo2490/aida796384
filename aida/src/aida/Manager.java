package aida;

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

import exception.InvalidSequentialPatternException;

/**
 * This class is responsible of managing files, conversion beetween queries and their ID, managing Sequential Pattern and so on..
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
 
public class Manager {
	
	//Keeps the association between the text of the query and its ID
	static Map<String, Integer> association;
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
	int idTE = -1; //Initialized to an invalid value
	 
	/**
	 * This method reads a CSV file and create a TXT file with a predefined structure.
	 * In the TXT file there is not reported the text query, but at each type of query is associated an ID used in the TXT file
	 * and there is a Map that keeps the association between query and ID.
	 * 
	 * The value "-2" indicates the end of a sequence (it appears at the end of each line) and "-1" indicates the end of an itemset.
	 * 
	 * @param csvPath
	 * @param outputPath
	 * @param teQuery
	 * @throws Exception
	 */
	  public void parseCSVtoTXT(String csvPath, String outputPath, String teQuery) throws Exception {
	 
		//Three parameter of the tool (input file, output file, query time-expensive
		String csvFile = csvPath;
		FileOutputStream output = new FileOutputStream(outputPath);
		String TEquery = teQuery;
		//----
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ", ";
		//The incremental identifier
		int key = 0;
        PrintStream outWriter = new PrintStream(output);
        int chunck = 0;
        int st=0;
        
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
					//It creates the maps with chuncks of log
					queryMap.put(chunck, queryList.subList(st, i+1));
					timestampMap.put(chunck, timestampList.subList(st, i+1));
					outWriter.println("-2");
					//It update the start point of the new chunck
					st=i+1;
					chunck++;
				}
			}
			if(queryList.get(queryList.size()-1)!=idTE)	outWriter.println("-2");
	 
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
		System.out.println(association.toString());
		System.out.println("------------");
		for(int qt=0; qt<queryList.size();qt++){	//queryList and timestampList have the same lenght by construction
			System.out.println(queryList.get(qt)+" <-> "+timestampList.get(qt));
		}
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
	  }
	  
	  /**
	   * This method considers one-at-time each sequential pattern and gives him all the chunck of the log in order to search
	   * if the sp is in the log and sets the correct values of its edges.
	   * 
	   * @param sp The list of sp
	   * @throws InvalidSequentialPatternException
	   */
	  public void findSP(List<SequentialPattern> sp) throws InvalidSequentialPatternException{
		  for(int i=0; i<sp.size(); i++){
				for(int j=0; j<queryMap.size();j++){
					sp.get(i).findSequentialPattern(queryMap.get(j), timestampMap.get(j));
				}
				sp.get(i).computeDuration();
			}
			
			/* DEBUG for testing purposes */
			System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
			for(int j=0;j<sp.size();j++){
				SequentialPattern dbg = new SequentialPattern();
				dbg=sp.get(j);
				System.out.println(dbg.toString());
			}
			System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
			
	  }
	  
	  /**
	   * This method parses the txt output file of the sequential pattern mining algorithm and creates an instance of a 
	   * sequential pattern for each sp found by the algorithm itself
	   *  
	   * @param spPath The output file of the algorithm
	   * @return The list of sequential pattern objects
	   * @throws Exception
	   */
	  public List<SequentialPattern> parseSP(String spPath) throws Exception {
			
		//Map with incremental integer as a key and sequential pattern that contains the time-expensive query as value
		//Map<Integer, List<Integer>> sp = new HashMap<Integer, List<Integer>>();
		List<SequentialPattern> spList = new ArrayList<SequentialPattern>();
	 
		int i=0;
		BufferedReader br = null;
		String line = "";
		String splitBy = " -1 ";

        System.out.println("Starting Sequential Pattern parsing..");
        
		try {
			br = new BufferedReader(new FileReader(spPath));
			while ((line = br.readLine()) != null) {
				//List<Integer> sequence = new ArrayList<Integer>();
				SequentialPattern sp = new SequentialPattern();
				
				// use " -1 " as separator in order to split
				String[] token = line.split(splitBy);
				
				//Save the pattern iff the last query in it is the time-expensive query //TOLTI 1-lenght SP CON SECONDA CONDIZIONE
				if(Integer.parseInt(token[token.length-2]) == idTE && token.length-2 != 0){
					while(i <= token.length-2){
						//sequence.add(Integer.parseInt(token[i]));
						sp.addNode(Integer.parseInt(token[i]));
						i++;
					}
					//put the sequential pattern in the Map
					//sp.put(sp.size(), sequence);
					spList.add(sp);
					i=0;
				}
				//Managing support data
				String sup=token[token.length-1].replace("#SUP: ", "");
				sp.setSupport(Integer.parseInt(sup));
			}
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
		System.out.println("Sequential Pattern parsing done\n");
		return spList;
	  }
	  
	  /**
	   * Getter method for Association map between string query and integer key associated
	   * @return the association map
	   */
	  public Map<String, Integer> getAssociationMap(){
		  return association;
	  }
	  
	  /**
	   * Getter method for chunck map of the corresponding input log
	   * @return the chunck map
	   */
	  public Map<Integer, List<Integer>> getChunckMap(){
		  return queryMap;
	  }
}