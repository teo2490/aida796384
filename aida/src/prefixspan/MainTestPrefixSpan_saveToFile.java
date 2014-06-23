package prefixspan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import prefixspan.BIDE_and_prefixspan.AlgoPrefixSpan;
import tools.SequenceDatabase;


/**
 * Example of how to use the PrefixSpan algorithm in source code.
 * @author Philippe Fournier-Viger
 */
public class MainTestPrefixSpan_saveToFile {

	public static void main(String [] args) throws IOException{    
		String outputPath = args[1];
		// Load a sequence database
		SequenceDatabase sequenceDatabase = new SequenceDatabase(); 
		sequenceDatabase.loadFile(args[0]);
		// print the database to console
		sequenceDatabase.print();
		
		
		
		// Create an instance of the algorithm with minsup = 50 %
		AlgoPrefixSpan algo = new AlgoPrefixSpan(); 
		
		//int minsup = 2; // we use a minimum support of 2 sequences.
		//calculating  number of sequences in which the sp has to appear, based on the support
		double minsup = Double.parseDouble(args[2]);
		int sup = (int) Math.ceil(minsup*sequenceDatabase.size());
		
		// execute the algorithm
		//algo.runAlgorithm(sequenceDatabase, minsup, outputPath);    
		algo.runAlgorithm(sequenceDatabase, outputPath, sup);   
		algo.printStatistics(sequenceDatabase.size());
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestPrefixSpan_saveToFile.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}