package aidaView;

import java.awt.EventQueue;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;


/*** 
 * The view of the application.
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

public class AidaView {

	private JFrame frmAidaAutomatic;
	private JTextField inputLogText;
	private List<JTextField> inputTimeText;
	private List<JTextField> inputSupText;
	private JFileChooser fc;
	private JTabbedPane tabbedPane;
	private JPanel outputPanel;
	private JButton btnStart;
	private JLabel errorLabel;
	private JTextArea flowingQueriesArea;
	private JButton startFlowBtn;
	private JButton pauseFlowBtn;
	private JButton stopFlowBtn;
	private JTextArea outputForecast;
	
	private String inputLog;
	private ArrayList<String> inputTime;
	private ArrayList<String> inputSup;
	private ArrayList<String> teQueries;
	
	private JPanel inputPanel;
	
	private JPanel currentSpPanel;
	private JScrollPane cspPanel;
	static ConnectorContainer cc;
	
	private List<SequentialPattern> spOnGoingView;
	private JPanel buttonPanel;
	private JPanel inputFile;
	private JPanel inputTeQuery;
	private JPanel inputButton;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton addRowButton;
	
	private DefaultTableModel dtm;
	private ArrayList<String> m;
	private JLabel lblColumnNumQuery;
	private JTextField numQueryText;
	private JLabel lblColumnNumTimestamp;
	private JTextField numTimestampText;
	private JButton btnComputeInput;
	
	private File file;
	private List<String> removed;
	private int currentNumRow = 1;
	private JTextArea outputLabel;
	private JPanel spPanel;
	private JScrollPane outputScrollPanel;
	
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
		frmAidaAutomatic.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		inputSupText = new ArrayList<JTextField>();
		inputTimeText = new ArrayList<JTextField>();
		file = null;
		
    	GridLayout gl_trainingPanel = new GridLayout(1, 2, 10, 10);
        JPanel trainingPanel = new JPanel(false);
        trainingPanel.setLayout(gl_trainingPanel);
        
        fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TXT & CSV files", "txt", "csv");
        fc.setFileFilter(filter);
        
        inputPanel = new JPanel();
        inputPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        trainingPanel.add(inputPanel);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        
        inputFile = new JPanel();
        inputPanel.add(inputFile);
        GridBagLayout gbl_inputFile = new GridBagLayout();
        gbl_inputFile.columnWidths = new int[]{106, 80, 0, 0, 0};
        gbl_inputFile.rowHeights = new int[]{33, 0, 0, 0, 0, 12, 0};
        gbl_inputFile.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_inputFile.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        inputFile.setLayout(gbl_inputFile);
        
        addRowButton = new JButton("Add Row");
        addRowButton.setEnabled(false);
        addRowButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(currentNumRow<m.size()){
	        		JTextField t = new JTextField();
	                t.setColumns(10);
	                inputTimeText.add(t);
	                JTextField sup = new JTextField();
	                sup.setColumns(10);
	                inputSupText.add(sup);
	                
	                dtm.addRow(new Object[] { "Choose the teQuery..", 
	                		"", ""});
	                
	                currentNumRow++;
	                if(currentNumRow==m.size())	addRowButton.setEnabled(false);
        		}
        	}
        });
        
        JButton btnOpenLog = new JButton("");
        GridBagConstraints gbc_btnOpenLog = new GridBagConstraints();
        gbc_btnOpenLog.anchor = GridBagConstraints.EAST;
        gbc_btnOpenLog.insets = new Insets(0, 0, 5, 5);
        gbc_btnOpenLog.gridx = 2;
        gbc_btnOpenLog.gridy = 1;
        inputFile.add(btnOpenLog, gbc_btnOpenLog);
        btnOpenLog.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		int returnVal = fc.showOpenDialog(frmAidaAutomatic);
        		 
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                    String s = file.getAbsolutePath();
                    inputLogText.setText(s);
                }
            }
        });
        btnOpenLog.setIcon(new ImageIcon(AidaView.class.getResource("open.png")));
        
        lblColumnNumQuery = new JLabel("Column Num Query");
        GridBagConstraints gbc_lblColumnNumQuery = new GridBagConstraints();
        gbc_lblColumnNumQuery.anchor = GridBagConstraints.WEST;
        gbc_lblColumnNumQuery.insets = new Insets(0, 5, 5, 5);
        gbc_lblColumnNumQuery.gridx = 0;
        gbc_lblColumnNumQuery.gridy = 2;
        inputFile.add(lblColumnNumQuery, gbc_lblColumnNumQuery);
        
        numQueryText = new JTextField();
        GridBagConstraints gbc_numQueryText = new GridBagConstraints();
        gbc_numQueryText.anchor = GridBagConstraints.WEST;
        gbc_numQueryText.insets = new Insets(0, 0, 5, 5);
        gbc_numQueryText.gridx = 1;
        gbc_numQueryText.gridy = 2;
        inputFile.add(numQueryText, gbc_numQueryText);
        numQueryText.setColumns(10);
        
        lblColumnNumTimestamp = new JLabel("Column Num Timestamp");
        GridBagConstraints gbc_lblColumnNumTimestamp = new GridBagConstraints();
        gbc_lblColumnNumTimestamp.anchor = GridBagConstraints.WEST;
        gbc_lblColumnNumTimestamp.insets = new Insets(0, 5, 5, 5);
        gbc_lblColumnNumTimestamp.gridx = 0;
        gbc_lblColumnNumTimestamp.gridy = 3;
        inputFile.add(lblColumnNumTimestamp, gbc_lblColumnNumTimestamp);
        
        numTimestampText = new JTextField();
        GridBagConstraints gbc_numTimestampText = new GridBagConstraints();
        gbc_numTimestampText.anchor = GridBagConstraints.WEST;
        gbc_numTimestampText.insets = new Insets(0, 0, 5, 5);
        gbc_numTimestampText.gridx = 1;
        gbc_numTimestampText.gridy = 3;
        inputFile.add(numTimestampText, gbc_numTimestampText);
        numTimestampText.setColumns(10);
        
        btnComputeInput = new JButton("");
        btnComputeInput.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		int numQuery = -1;
            	File file = fc.getSelectedFile();
            	
            	if(numQueryText.getText() != "" && numTimestampText.getText() != "" && file!=null){
            		numQuery = Integer.parseInt(numQueryText.getText());
                	
            		JTextField t = new JTextField();
                    t.setColumns(10);
                    inputTimeText.add(t);
                    JTextField sup = new JTextField();
                    sup.setColumns(10);
                    inputSupText.add(sup);
                    btnStart.setEnabled(true);
                    addRowButton.setEnabled(true);
                    
                    m = new ArrayList<String>();
        			BufferedReader br = null;
					try {
						br = new BufferedReader(new FileReader(file));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
        			String line = "";
        			String cvsSplitBy = ", ";
        			//int i=0;
					try {
						while ((line = br.readLine()) != null) {        				
							// use comma as separator
							String[] token = line.split(cvsSplitBy);
							System.out.println(!m.contains(token[numQuery]));
							System.out.println(token[numQuery]);
							if(!m.contains(token[numQuery]))	m.add(token[numQuery]);
							//i++;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					errorLabel.setText("");
                    @SuppressWarnings({ "rawtypes", "unchecked" })
					final JComboBox qm = new JComboBox(m.toArray());
                    removed = new ArrayList<String>();
                    qm.addActionListener(new ActionListener() {
                        @SuppressWarnings("unchecked")
						public void actionPerformed(ActionEvent event) {
                        	table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
                        	if(table.isEditing())	table.getCellEditor().stopCellEditing();
                        	removed.clear();
                        	for(int i=0; i<inputSupText.size();i++){
                        		removed.add(table.getModel().getValueAt(i, 0).toString());
                        	}

                            qm.removeAllItems();
                            for(int k=0; k<m.size(); k++)      	if(!removed.contains(m.get(k))) 	qm.addItem(m.get(k));
                        }

                    });
                    qm.setBounds(20, 183, 400, 20);
                    qm.setBounds(20, 183, 400, 20);
                    //queryMenu.add(qm);
                    table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(qm));
                    //inputPanel.add(queryMenu);
                    dtm.addRow(new Object[] { "Choose the teQuery..", 
                    		"", ""});
            	} else {
            		errorLabel.setText("All field must be correctly filled!\n");
            	}	
        	}
        });
        btnComputeInput.setIcon(new ImageIcon(AidaView.class.getResource("/aidaView/vverde.png")));
        GridBagConstraints gbc_btnComputeInput = new GridBagConstraints();
        gbc_btnComputeInput.anchor = GridBagConstraints.EAST;
        gbc_btnComputeInput.insets = new Insets(0, 0, 5, 5);
        gbc_btnComputeInput.gridx = 2;
        gbc_btnComputeInput.gridy = 3;
        inputFile.add(btnComputeInput, gbc_btnComputeInput);
        GridBagConstraints gbc_addRowButton = new GridBagConstraints();
        gbc_addRowButton.gridwidth = 3;
        gbc_addRowButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_addRowButton.insets = new Insets(5, 5, 5, 15);
        gbc_addRowButton.gridx = 0;
        gbc_addRowButton.gridy = 4;
        inputFile.add(addRowButton, gbc_addRowButton);
        
        inputLogText = new JTextField();
        GridBagConstraints gbc_inputLogText = new GridBagConstraints();
        gbc_inputLogText.gridwidth = 2;
        gbc_inputLogText.fill = GridBagConstraints.HORIZONTAL;
        gbc_inputLogText.insets = new Insets(5, 5, 5, 5);
        gbc_inputLogText.gridx = 0;
        gbc_inputLogText.gridy = 1;
        inputFile.add(inputLogText, gbc_inputLogText);
        
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
        inputLogText.setColumns(10);
        
        JLabel lblInputLogFile = new JLabel("Input Log File");
        GridBagConstraints gbc_lblInputLogFile = new GridBagConstraints();
        gbc_lblInputLogFile.insets = new Insets(0, 0, 5, 5);
        gbc_lblInputLogFile.gridx = 0;
        gbc_lblInputLogFile.gridy = 0;
        inputFile.add(lblInputLogFile, gbc_lblInputLogFile);
        
        errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        errorLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        errorLabel.setVerticalAlignment(SwingConstants.TOP);
        inputPanel.add(errorLabel);
        errorLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        errorLabel.setForeground(Color.RED);
          
        // inputTeQuery.add(errorLabel);

        inputTeQuery = new JPanel();
        inputPanel.add(inputTeQuery);
        
        table = new JTable();
        inputTeQuery.add(table);
        table.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        dtm = new DefaultTableModel(0, 0);

        //add header of the table
      	String header[] = new String[] { "teQuery", "Time (ms)", "Min Sup"};

      	//add header in table model
      	dtm.setColumnIdentifiers(header);
        //set model into the table object
        table.setModel(dtm);
        table.getColumnModel().getColumn(2).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setMaxWidth(180);
        inputTeQuery.setLayout(new BoxLayout(inputTeQuery, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(table);
        inputTeQuery.add(scrollPane);
        
        inputButton = new JPanel();
        inputPanel.add(inputButton);
        GridBagLayout gbl_inputButton = new GridBagLayout();
        gbl_inputButton.columnWidths = new int[]{0, 63, 0, 65, 0, 0};
        gbl_inputButton.rowHeights = new int[]{0, 0};
        gbl_inputButton.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_inputButton.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        inputButton.setLayout(gbl_inputButton);
        
        
        JButton btnReset = new JButton("RESET");
        GridBagConstraints gbc_btnReset = new GridBagConstraints();
        gbc_btnReset.anchor = GridBagConstraints.WEST;
        gbc_btnReset.insets = new Insets(0, 0, 0, 5);
        gbc_btnReset.gridx = 1;
        gbc_btnReset.gridy = 0;
        inputButton.add(btnReset, gbc_btnReset);
        btnReset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		inputLogText.setText("");
        		inputTimeText.clear();
        		inputSupText.clear();
        		numQueryText.setText("");
        		numTimestampText.setText("");
        		dtm.setRowCount(0);
        		outputLabel.setText("");
        		currentNumRow=1;
        		removed.clear();
        		spPanel.removeAll();
        	}
        });
        
        btnStart = new JButton("START");
        GridBagConstraints gbc_btnStart = new GridBagConstraints();
        gbc_btnStart.insets = new Insets(0, 0, 0, 5);
        gbc_btnStart.anchor = GridBagConstraints.EAST;
        gbc_btnStart.gridx = 3;
        gbc_btnStart.gridy = 0;
        inputButton.add(btnStart, gbc_btnStart);
        btnStart.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if (table.isEditing())	table.getCellEditor().stopCellEditing();
        	}
        });
        btnStart.setEnabled(false);
        
        outputPanel = new JPanel();

        outputPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        outputPanel.setBackground(UIManager.getColor("Panel.background"));
        GridBagLayout gbl_outputPanel = new GridBagLayout();
        gbl_outputPanel.columnWidths = new int[]{420, 0};
        gbl_outputPanel.rowHeights = new int[]{14, 541, 0, 0};
        gbl_outputPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_outputPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        outputPanel.setLayout(gbl_outputPanel);
        
        outputLabel = new JTextArea("");
        outputLabel.setLineWrap(true);
        outputLabel.setEditable(false);
        outputLabel.setBackground(UIManager.getColor("Panel.background"));
        GridBagConstraints gbc_outputLabel = new GridBagConstraints();
        gbc_outputLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_outputLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_outputLabel.insets = new Insets(0, 0, 5, 0);
        gbc_outputLabel.gridx = 0;
        gbc_outputLabel.gridy = 0;
        outputPanel.add(outputLabel, gbc_outputLabel);
        outputScrollPanel = new JScrollPane(outputPanel);
        
        spPanel = new JPanel();
        GridBagConstraints gbc_spPanel = new GridBagConstraints();
        gbc_spPanel.fill = GridBagConstraints.BOTH;
        gbc_spPanel.gridx = 0;
        gbc_spPanel.gridy = 1;
        outputPanel.add(spPanel, gbc_spPanel);
        spPanel.setLayout(new BoxLayout(spPanel, BoxLayout.Y_AXIS));
        trainingPanel.add(outputScrollPanel);
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
        flowingPanel.setLayout(new BoxLayout(flowingPanel, BoxLayout.Y_AXIS));
        
        flowingQueriesArea = new JTextArea();
        flowingQueriesArea.setEditable(false);
        //flowingQueriesArea.setRows(15);
        flowingQueriesArea.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        DefaultCaret caret = (DefaultCaret)flowingQueriesArea.getCaret();  
        JScrollPane fqPanel = new JScrollPane(flowingQueriesArea);
        fqPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        flowingPanel.add(fqPanel);
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        outputForecast = new JTextArea();
        outputForecast.setEditable(false);
        //outputForecast.setRows(12);
        outputForecast.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        DefaultCaret caret1 = (DefaultCaret)outputForecast.getCaret();  
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        buttonPanel = new JPanel();
        flowingPanel.add(buttonPanel);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        startFlowBtn = new JButton("START FLOW");
        buttonPanel.add(startFlowBtn);
        
        pauseFlowBtn = new JButton("PAUSE FLOW");
        buttonPanel.add(pauseFlowBtn);
        
        stopFlowBtn = new JButton("STOP FLOW");
        buttonPanel.add(stopFlowBtn);
        
        JScrollPane outputAreaPanel = new JScrollPane(outputForecast);
        outputAreaPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        flowingPanel.add(outputAreaPanel);
        
        currentSpPanel = new JPanel();
        currentSpPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
        cspPanel = new JScrollPane(currentSpPanel);
        currentSpPanel.setLayout(new BoxLayout(currentSpPanel, BoxLayout.Y_AXIS));
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
    	cc.setLayout(new FlowLayout(FlowLayout.LEADING));
        //cc.setLayout(null);
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
        //cc.setMinimumSize(new Dimension(80, 50));
        currentSpPanel.add(cc);
        currentSpPanel.revalidate();
        currentSpPanel.repaint();
        cspPanel.revalidate();
        cspPanel.repaint();
        return cc;
    }
    
    protected ConnectorContainer addScheduled(SequentialPattern i){
    	JConnector[] connectors = new JConnector[i.getNumberOfEdges()];
    	ConnectorContainer cc = new ConnectorContainer(connectors);
    	cc.setLayout(new FlowLayout(FlowLayout.LEFT));
        //cc.setLayout(null);
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
        //cc.setMinimumSize(new Dimension(20, 30));
        currentSpPanel.add(cc);
        currentSpPanel.revalidate();
        currentSpPanel.repaint();
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
    
//    public void printTrainingOutput(String s){
//    	outputTextArea.append(s+"\n");
//    }
    
    public void printOutputLabel(String s){
    	outputLabel.setText(s);
    }
    
	public ConnectorContainer printSequentialPattern(final SequentialPattern sp){
    	JConnector[] connectors = new JConnector[sp.getNumberOfEdges()];
    	JPanel pp = new JPanel();
    	pp.setLayout(new BoxLayout(pp, BoxLayout.X_AXIS));
    	JButton openS = new JButton();
    	openS.setText("See complete SP");
    	openS.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		JOptionPane.showMessageDialog(frmAidaAutomatic, "node1 |mean # tolerance| node2\n\n"+sp.toString());
            }
        });
    	ConnectorContainer cc = new ConnectorContainer(connectors);
    	cc.setLayout(new FlowLayout(FlowLayout.LEADING));
        //cc.setLayout(null);
        JLabel[] b2 = new JLabel[sp.getNumberOfNodes()];
        b2[0]=new JLabel("  q"+sp.getNode(0));
        b2[0].setBounds(0, 10, 30, 30);
        b2[0].setBorder(new EtchedBorder());
        b2[0].setOpaque(true);
        cc.add(b2[0]);
        for(int j=1; j<sp.getNumberOfNodes(); j++){
        	b2[j]=new JLabel("  q"+sp.getNode(j));
            b2[j].setBounds(50*(j), 10, 30, 30);
            b2[j].setBorder(new EtchedBorder());
        	connectors[j-1] = new JConnector(b2[j-1], b2[j], ConnectLine.LINE_ARROW_NONE, JConnector.CONNECT_LINE_TYPE_RECTANGULAR, Color.red);
        	cc.add(b2[j]);
        }
        cc.setLabels(b2);
        cc.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        //cc.setMinimumSize(new Dimension(80, 50));
        pp.add(cc);
        pp.add(openS);
        spPanel.add(pp);
        spPanel.revalidate();
        spPanel.repaint();
        outputScrollPanel.revalidate();
        outputScrollPanel.repaint();
        return cc;
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
    
    public void validateSpInList(SequentialPattern sp){
    	spOnGoingView.add(sp);
    	//currentSpPanel.remove(pos);
    	currentSpPanel.add(addScheduled(sp));
    	//spOnGoingView.remove(pos);
    	currentSpPanel.revalidate();
    	currentSpPanel.repaint();
    }
    
    public List<String> getInputTime(){
    	inputTime = new ArrayList<String>();
    	table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    	if(table.isEditing())	table.getCellEditor().stopCellEditing();
    	
    	for(int i=0; i<inputTimeText.size();i++){
    		inputTime.add(table.getValueAt(i, 1).toString());
    		System.out.println(table.getValueAt(i, 1).toString());
    	}
    	
    	return inputTime;
    }
    
    public List<String> getInputSup(){
    	inputSup = new ArrayList<String>();
    	table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    	if(table.isEditing())	table.getCellEditor().stopCellEditing();
    	
    	for(int i=0; i<inputSupText.size();i++){
    		inputSup.add(table.getValueAt(i, 2).toString());
    		System.out.println(table.getValueAt(i, 2).toString());
    	}
    	
    	return inputSup;
    }
    
    public List<String> getInputTeQuery(){
    	teQueries = new ArrayList<String>();
    	table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    	if(table.isEditing())	table.getCellEditor().stopCellEditing();
    	
    	for(int i=0; i<inputSupText.size();i++){
    		teQueries.add(table.getModel().getValueAt(i, 0).toString());
    	}
    	
    	return teQueries;
    }
    
    public String getInputLog(){

    	inputLog = inputLogText.getText();
		
		errorLabel.setText("");
//		if(inputLog.isEmpty() || inputTime.isEmpty() || inputSup.isEmpty()){
//			errorLabel.setText("ERROR! All fields must be filled!");
//			return null;
//		} else {
//			inSup = Float.parseFloat(inputSup);
//			if(inSup<0 || inSup>1){
//				errorLabel.setText("ERROR! Support must be a number between 0 and 1!");
//				return null;
//			}
		//QUI l'output della prima parte!
		//outputTextArea.setText(inputLog+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+inputLog);
		tabbedPane.setEnabledAt(1, true);
    	
    	return inputLog;
	}
    
}

