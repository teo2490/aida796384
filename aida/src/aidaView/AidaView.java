package aidaView;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

import exception.InvalidSequentialPatternException;

import aidaModel.SequentialPattern;
import aidaView.sequentialPatternView.ConnectorContainer;
import aidaView.sequentialPatternView.JConnector;
import aidaView.sequentialPatternView.ConnectLine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/*** 
 * The view of the application.
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

public class AidaView {

	private JFrame frmAidaAutomatic;
	private JTextField inputLogText;
	private JTextField inputTimeText;
	private JTextField inputSupText;
	private JFileChooser fc;
	private JTabbedPane tabbedPane;
	private JPanel outputPanel;
	private JTextArea outputTextArea;
	private JButton btnStart;
	private JLabel errorLabel;
	private JTextArea flowingQueriesArea;
	private JButton startFlowBtn;
	private JButton pauseFlowBtn;
	private JButton stopFlowBtn;
	private JTextArea outputForecast;
	
	private String inputLog;
	private String inputTime;
	private String inputSup;
	
	private JPanel currentSpPanel;	
	static ConnectorContainer cc;
	
	private List<SequentialPattern> spOnGoingView;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AidaView window = new AidaView();
					window.frmAidaAutomatic.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws InvalidSequentialPatternException 
	 */
	public AidaView() throws InvalidSequentialPatternException {
		spOnGoingView = new ArrayList<SequentialPattern>();
		initialize();
		frmAidaAutomatic.setVisible(true);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = AidaView.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
	/**
	 * Initialize the contents of the frame.
	 * @throws InvalidSequentialPatternException 
	 */
	private void initialize() throws InvalidSequentialPatternException {
		frmAidaAutomatic = new JFrame();
		frmAidaAutomatic.setIconImage(Toolkit.getDefaultToolkit().getImage(AidaView.class.getResource("aida_39.png")));
		frmAidaAutomatic.setTitle("AIDA - Automatic Index extraction with Data Mining tool");
		frmAidaAutomatic.setBounds(180, 50, 900, 650);
		frmAidaAutomatic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAidaAutomatic.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		 tabbedPane = new JTabbedPane();
		 frmAidaAutomatic.getContentPane().add(tabbedPane);
		 
		 ImageIcon icon = createImageIcon("aida_39.png");
	        
	        //Creating and adding first tab
	        JComponent panel1 = makeTrainingPanel();
	        tabbedPane.addTab("Training Phase", icon, panel1,
	                "It extracts all the enriched sequential pattern");
	        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	         
	        //Creating and adding second tab
	        JComponent panel2 = makeForecastingPanel();
	        tabbedPane.addTab("Forecasting Phase", icon, panel2,
	                "It simulates the functioning of the forecasting system");
	        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	        
	        //Set the forecasting tab disabled at launch. It will be enabled after the training phase is done
	        tabbedPane.setEnabledAt(1, false);
	        	         
	        //The following line enables to use scrolling tabs.
	        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	protected JComponent makeTrainingPanel() {
    	GridLayout gl_trainingPanel = new GridLayout(1, 2, 10, 10);
        JPanel trainingPanel = new JPanel(false);
        trainingPanel.setLayout(gl_trainingPanel);
        fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TXT & CSV files", "txt", "csv");
        fc.setFileFilter(filter);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        trainingPanel.add(inputPanel);
        inputPanel.setLayout(null);
        
        JButton btnReset = new JButton("RESET");
        btnReset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		inputLogText.setText("");
        		inputTimeText.setText("");
        		inputSupText.setText("");
        		outputTextArea.setText("");
        	}
        });
        btnReset.setBounds(54, 244, 89, 23);
        inputPanel.add(btnReset);
        
        inputLogText = new JTextField();
        inputLogText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		int returnVal = fc.showOpenDialog(frmAidaAutomatic);
       		 
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String s = file.getAbsolutePath();
                    inputLogText.setText(s);
                }
        	}
        });
        inputLogText.setBounds(10, 87, 322, 20);
        inputPanel.add(inputLogText);
        inputLogText.setColumns(10);
        
        JButton btnOpenLog = new JButton("");
        btnOpenLog.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		int returnVal = fc.showOpenDialog(frmAidaAutomatic);
        		 
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String s = file.getAbsolutePath();
                    inputLogText.setText(s);
                }
            }
        });
        btnOpenLog.setIcon(new ImageIcon(AidaView.class.getResource("open.png")));
        btnOpenLog.setBounds(336, 85, 38, 23);
        inputPanel.add(btnOpenLog);
        
        JLabel lblInputLogFile = new JLabel("Input Log File");
        lblInputLogFile.setBounds(10, 68, 81, 14);
        inputPanel.add(lblInputLogFile);
        
        inputTimeText = new JTextField();
        inputTimeText.setBounds(10, 133, 189, 20);
        inputPanel.add(inputTimeText);
        inputTimeText.setColumns(10);
        
        JLabel lblTimeForIndex = new JLabel("Time for Index implementation (in ms)");
        lblTimeForIndex.setBounds(10, 115, 222, 14);
        inputPanel.add(lblTimeForIndex);
        
        JLabel lblMinimumSupport = new JLabel("Minimum Support (between 0 and 1)");
        lblMinimumSupport.setBounds(10, 164, 210, 14);
        inputPanel.add(lblMinimumSupport);
        
        inputSupText = new JTextField();
        inputSupText.setBounds(10, 181, 189, 20);
        inputPanel.add(inputSupText);
        inputSupText.setColumns(10);
        
        JLabel lblInputParameter = new JLabel("INPUT PARAMETER");
        lblInputParameter.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblInputParameter.setBackground(Color.GRAY);
        lblInputParameter.setBounds(127, 23, 151, 25);
        inputPanel.add(lblInputParameter);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(10, 300, 364, 33);
        inputPanel.add(errorLabel);
        
        outputPanel = new JPanel();
        outputPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        outputPanel.setBackground(Color.WHITE);
        JScrollPane outputScrollPanel = new JScrollPane(outputPanel);
        
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputPanel.add(outputTextArea);
        outputScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        trainingPanel.add(outputScrollPanel);
        
        btnStart = new JButton("START");
        /*btnStart.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		inputLog = inputLogText.getText();
        		inputTime = inputTimeText.getText();
        		inputSup = inputSupText.getText();
        		float inSup;
        		int inTime;
        		
        		errorLabel.setText("");
        		if(inputLog.isEmpty() || inputTime.isEmpty() || inputSup.isEmpty()){
        			errorLabel.setText("ERROR! All fields must be filled!");
        			return;
        		} else {
        			inSup = Float.parseFloat(inputSup);
            		inTime = Integer.parseInt(inputTime);
        			if(inSup<0 || inSup>1){
        				errorLabel.setText("ERROR! Support must be a number between 0 and 1!");
        				return;
        			}
        		//QUI l'output della prima parte!
        		outputTextArea.setText(inputLog+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+inputLog);
        		tabbedPane.setEnabledAt(1, true);
        		}
        	}
        });*/
        btnStart.setBounds(248, 244, 89, 23);
        inputPanel.add(btnStart);
        
        //trainingPanel.add(panel);
        return trainingPanel;
    }
    
    protected JComponent makeForecastingPanel() throws InvalidSequentialPatternException {
    	GridLayout gl_forecastingPanel = new GridLayout(1, 2, 10, 10);
        JPanel forecastingPanel = new JPanel(false);
        forecastingPanel.setLayout(gl_forecastingPanel);
        
        JPanel flowingPanel = new JPanel();
        flowingPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        forecastingPanel.add(flowingPanel);
        flowingPanel.setLayout(new BorderLayout(5, 5));
        
        startFlowBtn = new JButton("START FLOW");
        flowingPanel.add(startFlowBtn, BorderLayout.WEST);
        
        pauseFlowBtn = new JButton("PAUSE FLOW");
        flowingPanel.add(pauseFlowBtn, BorderLayout.CENTER);
        
        stopFlowBtn = new JButton("STOP FLOW");
        flowingPanel.add(stopFlowBtn, BorderLayout.EAST);
        
        flowingQueriesArea = new JTextArea();
        flowingQueriesArea.setEditable(false);
        flowingQueriesArea.setRows(15);
        flowingQueriesArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        DefaultCaret caret = (DefaultCaret)flowingQueriesArea.getCaret();  
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane fqPanel = new JScrollPane(flowingQueriesArea);
        fqPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        flowingPanel.add(fqPanel, BorderLayout.NORTH);
        
        outputForecast = new JTextArea();
        outputForecast.setEditable(false);
        outputForecast.setRows(12);
        outputForecast.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        DefaultCaret caret1 = (DefaultCaret)outputForecast.getCaret();  
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        JScrollPane outputAreaPanel = new JScrollPane(outputForecast);
        outputAreaPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        flowingPanel.add(outputAreaPanel, BorderLayout.SOUTH);
        
        currentSpPanel = new JPanel();
        currentSpPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        currentSpPanel.setLayout(new GridLayout(16, 1));
        JScrollPane cspPanel = new JScrollPane(currentSpPanel);
        cspPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        forecastingPanel.add(cspPanel);
        
        return forecastingPanel;
    }
    
    /**
     * This method creates a visual view of a Sequential Pattern and show it
     * @param i	The Sequential Pattern that has to be visualized
     * @return
     */
    protected ConnectorContainer initSp(SequentialPattern i){
    	JConnector[] connectors = new JConnector[i.getNumberOfEdges()];
    	ConnectorContainer cc = new ConnectorContainer(connectors);
        cc.setLayout(null);
        //cc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JLabel[] b2 = new JLabel[i.getNumberOfNodes()];
        b2[0]=new JLabel("  q"+i.getNode(0));
        b2[0].setBounds(0, 10, 30, 30);
        b2[0].setBorder(new EtchedBorder());
        b2[0].setOpaque(true);
        b2[0].setBackground(Color.red);
        cc.add(b2[0]);
        for(int j=1; j<i.getNumberOfNodes(); j++){
        	b2[j]=new JLabel("  q"+i.getNode(j));
            b2[j].setBounds(50*(j), 10, 30, 30);
            b2[j].setBorder(new EtchedBorder());
            if(j<i.getNextNodeToCheck()){
            	b2[j].setOpaque(true);
                b2[j].setBackground(Color.red);
            }
        	connectors[j-1] = new JConnector(b2[j-1], b2[j], ConnectLine.LINE_ARROW_NONE, JConnector.CONNECT_LINE_TYPE_RECTANGULAR, Color.red);
        	cc.add(b2[j]);
        }
        cc.setLabels(b2);
        cc.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return cc;
    }
    
    protected ConnectorContainer addScheduled(SequentialPattern i){
    	JConnector[] connectors = new JConnector[i.getNumberOfEdges()];
    	ConnectorContainer cc = new ConnectorContainer(connectors);
        cc.setLayout(null);
        //cc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JLabel[] b2 = new JLabel[i.getNumberOfNodes()];
        b2[0]=new JLabel("  q"+i.getNode(0));
        b2[0].setBounds(0, 10, 30, 30);
        b2[0].setBorder(new EtchedBorder());
        b2[0].setOpaque(true);
        b2[0].setBackground(Color.green);
        cc.add(b2[0]);
        for(int j=1; j<i.getNumberOfNodes(); j++){
        	b2[j]=new JLabel("  q"+i.getNode(j));
            b2[j].setBounds(50*(j), 10, 30, 30);
            b2[j].setBorder(new EtchedBorder());
        	b2[j].setOpaque(true);
            b2[j].setBackground(Color.green);
        	connectors[j-1] = new JConnector(b2[j-1], b2[j], ConnectLine.LINE_ARROW_NONE, JConnector.CONNECT_LINE_TYPE_RECTANGULAR, Color.red);
        	cc.add(b2[j]);
        }
        cc.setLabels(b2);
        cc.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return cc;
    }
    
    public JButton getStartBtn(){
		return btnStart;
	}
    
    public JButton getStartFlowBtn(){
		return startFlowBtn;
	}
    
    public JButton getPauseFlowBtn(){
		return pauseFlowBtn;
	}
    
    public JButton getStopFlowBtn(){
		return stopFlowBtn;
	}
    
    public void printTrainingOutput(String s){
    	outputTextArea.append(s+"\n");
    }
    
    public void printForecastingQueries(String s){
    	flowingQueriesArea.append(s+"\n");
    }
    
    public void printForecastingResponse(String s){
    	outputForecast.append(s+"\n");
    }
    
    public void clearForecastingQueries(){
    	flowingQueriesArea.setText("");
    }
    
    public void clearForecastingOutput(){
    	outputForecast.setText("");
    }
    
    public void clearForecastingSpView(){
    	spOnGoingView.removeAll(spOnGoingView);
    	currentSpPanel.removeAll();
    	currentSpPanel.revalidate();
    	currentSpPanel.repaint();
    }
    
    public void addSpToList(SequentialPattern sp){
    	spOnGoingView.add(sp);
    	currentSpPanel.add(initSp(sp));
    	currentSpPanel.revalidate();
    	currentSpPanel.repaint();
    }
    
    public void removeSpFromList(int pos){
    	currentSpPanel.remove(pos);
    	spOnGoingView.remove(pos);
    	currentSpPanel.revalidate();
    	currentSpPanel.repaint();
    }
    
    public void validateSpInList(int pos){
    	currentSpPanel.remove(pos);
    	currentSpPanel.add(addScheduled(spOnGoingView.get(pos)));
    	spOnGoingView.remove(pos);
    	currentSpPanel.revalidate();
    	currentSpPanel.repaint();
    }
    
    public List<String> getInputParameter(){
    	
    	inputLog = inputLogText.getText();
		inputTime = inputTimeText.getText();
		inputSup = inputSupText.getText();
		double inSup;
		//int inTime; Non usato in controlli
		
		errorLabel.setText("");
		if(inputLog.isEmpty() || inputTime.isEmpty() || inputSup.isEmpty()){
			errorLabel.setText("ERROR! All fields must be filled!");
			return null;
		} else {
			inSup = Float.parseFloat(inputSup);
			if(inSup<0 || inSup>1){
				errorLabel.setText("ERROR! Support must be a number between 0 and 1!");
				return null;
			}
		//QUI l'output della prima parte!
		//outputTextArea.setText(inputLog+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+inputLog);
		tabbedPane.setEnabledAt(1, true);
		
    	List<String> in = new ArrayList<String>();
    	
    	in.add(inputLog);
    	in.add(inputTime);
    	in.add(inputSup);
    	
    	return in;
		}
    }
}

