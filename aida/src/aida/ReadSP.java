package aida;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses the TXT output file written by the algorithm used in order to find sequential pattern.
 * 
 * It filters found patterns and chooses only the one that contains the time-expensive query by saving it in a Map.
 * 
 * @author Matteo Simoni 796384
 *
 */
public class ReadSP {

	/*
	public static void main(String[] args) throws Exception {	 
		ReadSP obj = new ReadSP();
		//obj.parseSP("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputGSP.txt",
		//			1);	 
	  }
	  */
	  
	 
	  public List<SequentialPattern> parseSP(String spPath, int teQuery) throws Exception {
		
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
				
				//NB. CON token.lenght-2 PERDIAMO IL DATO DEL SUPPORTO !!!
				//Save the pattern iff the last query in it is the time-expensive query
				if(Integer.parseInt(token[token.length-2]) == teQuery){
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
		
		/* DEBUG for testing purposes 
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
		for(int j=0;j<sp.size();j++){
			List<Integer> dbg = new ArrayList<Integer>();
			dbg = sp.get(j);
			for(int k: dbg){
				System.out.print(k+" ");
			}
			System.out.print("\n");
		}
		System.out.println("\n\n----- FOR TESTING PURPOSES -----\n\n");
		*/
		return spList;
	  }
}
