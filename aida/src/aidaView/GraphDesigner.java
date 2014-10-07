package aidaView;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphDesigner {
	
	public GraphDesigner(int p[][], int r[][]){
		
	}
	/*
	public static void main(String[] args) {
		// Create a simple XY chart
		XYSeries series = new XYSeries("Precision");
		series.add(1, 1);
		series.add(1, 2);
		series.add(2, 1);
		series.add(3, 9);
		series.add(4, 10);
		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		
		paintPrecisionGrahp(dataset);
		paintRecallGrahp(dataset);
	}
	*/

	public static void paintPrecisionGrahp(XYSeriesCollection dataset){
		// Generate the graph
			final JFreeChart chart = ChartFactory.createScatterPlot(
			//JFreeChart chart = ChartFactory.createXYLineChart(
			"Precision Chart", // Title
			"queries", // x-axis Label
			"precision", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			false, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
			);
				 
			//JPanel
			JPanel pnlButton = new JPanel();
			//Buttons
			JButton btnAddFlight = new JButton("Export JPG");
			btnAddFlight.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent arg0) {
		    		try {
		    			File f = new File("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\sperimentazione\\precision.jpg");
		    			ChartUtilities.saveChartAsJPEG(f, chart, 500, 300);
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    			System.err.println("Problem occurred creating chart.");
		    		}
		    	}
			});
			pnlButton.add(btnAddFlight);
			Frame frame = new Frame();
			frame.addWindowListener(new WindowAdapter(){
				  public void windowClosing(WindowEvent we){
				  System.exit(0);
				  }
			});
			ChartPanel panel = new ChartPanel(chart);
			frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
			frame.add(panel);
			btnAddFlight.setAlignmentX(Component.RIGHT_ALIGNMENT);
			frame.add(btnAddFlight);
			frame.pack();
			frame.setVisible(true);
	}

	public static void paintRecallGrahp(XYSeriesCollection dataset){
		// Generate the graph
			final JFreeChart chart = ChartFactory.createScatterPlot(
			//JFreeChart chart = ChartFactory.createXYLineChart(
			"Recall Chart", // Title
			"queries", // x-axis Label
			"recall", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			false, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
			);
				 
			//JPanel
			JPanel pnlButton = new JPanel();
			//Buttons
			JButton btnAddFlight = new JButton("Export JPG");
			btnAddFlight.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent arg0) {
		    		try {
		    			File f = new File("C:\\Users\\Matteo\\Dropbox\\UNI\\TESI RELACS\\MatteoSimoni\\sperimentazione\\recall.jpg");
		    			ChartUtilities.saveChartAsJPEG(f, chart, 500, 300);
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    			System.err.println("Problem occurred creating chart.");
		    		}
		    	}
			});
			pnlButton.add(btnAddFlight);
			Frame frame = new Frame();
			frame.addWindowListener(new WindowAdapter(){
				  public void windowClosing(WindowEvent we){
				  System.exit(0);
				  }
			});
			ChartPanel panel = new ChartPanel(chart);
			frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
			frame.add(panel);
			btnAddFlight.setAlignmentX(Component.RIGHT_ALIGNMENT);
			frame.add(btnAddFlight);
			frame.pack();
			frame.setVisible(true);
	}
}