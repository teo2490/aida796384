package aidaController;

import aidaModel.Manager;
import aidaView.AidaView;


/*** 
 * The main class. It is the one called when the software is started.
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

public class Main {

	public static void main(String[] args) throws Exception {
		
		//float timeForCreation=80000;
		AidaView view = new AidaView();
		Manager model = new Manager();
		@SuppressWarnings("unused")
		AidaController controller = new AidaController(model, view);
		
//		
//		/* --- START of 1st part code --- */
//		System.out.println("----------------------- 1st PART output -----------------------");
//		
//		Manager m = new Manager();
//		m.parseCSVtoTXT("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\log_new.csv",
//				"C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan.txt",
//				"SELECT * FROM sensors");	 
//		
//		// Launching PrefixSpan Algorithm..
//		System.out.println("Launching PrefixSpan Algorithm..\n");
//		String[] arg = new String[2];
//		arg[0]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\inputPrefixSpan.txt";
//		arg[1]="C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt";
//		MainTestPrefixSpan_saveToFile.main(arg);
//		System.out.println("Sequential pattern search done.\n");
//		
//		// Parsing PrefixSPan Output
//		/* Now I have to parse outputPrefixSpan.txt in order to retrieve the frequent sequential patter that I need (the ones that
//		contains the time-expensive query) and use them in order to calculate the "duration" of a pattern.. */
//		List<SequentialPattern> sp = m.parseSP("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\java_prove\\csv\\outputPrefixSpan.txt");
//		
//		//Finding Sequential Pattern in the chuncks of log
//		m.findSP(sp);
//		/* --- END of 1st part code --- */
//		
//		/* --- START of 2nd part code --- */
//		System.out.println("----------------------- 2nd PART output -----------------------");
//		System.out.println("\nIndex implementation time: "+timeForCreation);
//		Simulator initiater = new Simulator(5);
//        Listener r1 = new Listener(sp, timeForCreation);
//
//        initiater.addListener(r1);
//
//        System.out.println("\nSTARTING EXECUTION!\n");
//        
//        for(int i=0; i<10; i++){
//        	//Long pausing time in order to make some partial so invalid due to time constraint
//        	if(i==6)	Thread.sleep(120000);
//        	initiater.makeQuery();
//        }
//        /* Used in place of the for-block for ad-hoc testin purposes
//        initiater.makeQuery();
//        System.out.println("\nSTOP EXECUTION!");
//		*/
//		/* --- END of 2nd part code --- */
	  }
}
