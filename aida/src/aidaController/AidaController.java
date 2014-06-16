package aidaController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import aida.Manager;
import aidaView.AidaView;

public class AidaController {
	private AidaView view;
	private Manager model;
	
	public AidaController(Manager m, AidaView v){
		this.view = v;
		this.model = m;

		view.getStartBtn().addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		List<String> s = view.getInputParameter();
        		//Chiamo il metodo per fare tutta la parte di training con i parametri e la stampo
        		view.printTrainingOutput(s.get(0));
        	}
        });
	}
	
	public void parsing(){
		List<String> s = view.getInputParameter();
		view.printTrainingOutput(s.get(0));
	}

}
