package edu.gmu.trajviz;

import edu.gmu.trajviz.controller.SequiturController;

import edu.gmu.trajviz.model.SequiturModel;
import edu.gmu.trajviz.view.SequiturView;


/*
 * Main runnable of trajViz GUI
 * 
 * @author Qingzhe Li
 * 
 */
public class TrajVizGUI{
	
	/** The model instance. */
	private static SequiturModel model;

	/** The controller instance. */
	private static SequiturController controller;

	/** The view instance. */
	private static SequiturView view;

	public static void main(String[] args){
	  
		System.out.println("Starting TrajViz 1.0...");
		// this is the Apple fix
	    System.setProperty("apple.laf.useScreenMenuBar", "true");
	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SAXSequitur");

	    // model...
	    model = new SequiturModel();

	    // controller...
	    controller = new SequiturController(model);

	    // view...
	    view = new SequiturView(controller);

	    // make sure these two met...
	    model.addObserver(view);
	    controller.addObserver(view);
	    // live!!!
	    /*
	     * test area
	     */
	    view.showGUI();
	    
	}
	
}
