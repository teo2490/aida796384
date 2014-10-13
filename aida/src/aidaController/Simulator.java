package aidaController;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import aidaModel.Manager;
import aidaView.AidaView;

import exception.InvalidSequentialPatternException;


/*** 
 * This class is the simulator component. It is used as a substitute of the DMBS in order to make our tool run.
 * It simply keeps track of all the registered listeners and generates random queries with random intervals. When a query is
 * generated, it notifies all the registered listeners.
 *
 * Copyright (c) 2014 Matteo Simoni 796384
 * Mail to matteo.simoni@mail.polimi.it
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

public class Simulator {
	List<QueryListener> listeners = new ArrayList<QueryListener>();
	Random randomGenerator = new Random();
	int seed=0;
	AidaView view;
	ResultSet rs;
	Manager m;
	
	/**
	 * Constructor with parameter.
	 * 
	 * @param r	The seed for the random generator.
	 */
	public Simulator(int r, AidaView v, Manager mp){
		seed=r;
		view=v;
		m=mp;
		System.out.println("ASSO MAP: "+m.getAssociationMap().toString());
	}

	/**
	 * This method add a listener to the registered ones list.
	 * 
	 * @param toAdd	The listener that has to be added
	 */
    public void addListener(QueryListener toAdd) {
        listeners.add(toAdd);
    }
    
    public void getSet(){
    	String dbURL = "jdbc:mysql://localhost:3306/auditel";
        String username ="root";
        String password = "pizzamysql";
       
        Connection dbCon = null;
        Statement stmt = null;
        rs = null;

        String query ="SELECT emittente, startTime, endTime FROM auditel.individuals_syntonizations_live WHERE user=5 ORDER BY startTime;";
        
        try {
            //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbURL, username, password);
           
            //getting PreparedStatment to execute query
            stmt = dbCon.prepareStatement(query);
           
            //Resultset returned by query
            rs = stmt.executeQuery(query);
            rs.beforeFirst();
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("SQL Exception!");
        } finally{
           //close connection ,stmt and resultset here
        }
    }
    
	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

    /**
     * This method generates random queries with random interval between two of them.
     * 
     * @throws InterruptedException
     * @throws InvalidSequentialPatternException
     */
    public int makeQuery() throws InterruptedException, InvalidSequentialPatternException {
    	
        //int randomInt = -1;
        
        int emittente = -1;
        int em = -1;
        
        try {
	        if(rs.next()){
		        em = rs.getInt(1);
		        System.out.println("EM: "+em);
		        try{
		        	emittente = m.getAssociationMap().get(Integer.toString(em));
		        } catch(NullPointerException e){
		        	emittente = -1;
		        }
		         
		        //randomInt = randomGenerator.nextInt(seed);
		        long time = getDateDiff(rs.getDate(2), rs.getDate(3), TimeUnit.MILLISECONDS);
		        time = time / 1000;
		        Thread.sleep(time);
		         
		        // Notify everybody that may be interested.
		        for (QueryListener hl : listeners){
		        	System.out.println("Emittente "+emittente+" eseguita!");
		        	hl.someoneMadeQuery(emittente);
		        } 
	        }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
//    	randomInt = randomGenerator.nextInt(seed);
//        Thread.sleep(randomInt*1000*2);
//    	if(randomInt==0)	randomInt=randomInt+1;
//   	
//        //System.out.println("Query "+randomInt+" executed! @ "+System.nanoTime());
//        // Notify everybody that may be interested.
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(randomInt);
//        }
        
// ------------------        
     
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(2);
//        }
//        System.out.println("QUERY 5");
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(5);
//        }
//        System.out.println("QUERY 5");
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(1);
//        }
//        System.out.println("QUERY 1");
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(5);
//        }
//        System.out.println("QUERY 5");
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(5);
//        }
//        System.out.println("QUERY 5");
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(5);
//        }
//        System.out.println("QUERY 5");
//        Thread.sleep(1000*2);
//       
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(5);
//        }
//        System.out.println("QUERY 5");
//        Thread.sleep(1000*2);
//        for (QueryListener hl : listeners){
//        	hl.someoneMadeQuery(1);
//        }
//        System.out.println("QUERY 1");
//        Thread.sleep(1000*2);
//        return randomInt;
        
        return emittente;
    }
}

