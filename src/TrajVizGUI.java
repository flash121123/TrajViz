

import edu.gmu.timeseries.TSUtils;

import edu.gmu.trajviz.controller.Controller;

import edu.gmu.trajviz.model.ItrSequiturModel;
import edu.gmu.trajviz.view.ItrSequiturView;


/**
 * Main runnable of trajViz GUI
 * 
 * @author Qingzhe Li, Yifeng Gao, Xiaosheng Li
 * 
 */
public class TrajVizGUI{
	
	/** The model instance. */
	private static ItrSequiturModel model;

	/** The controller instance. */
	private static Controller controller;

	/** The view instance. */
	private static ItrSequiturView view;

	public static void main(String[] args){
	  
		System.out.println("Starting TrajViz 1.0...");
		// this is the Apple fix
	    System.setProperty("apple.laf.useScreenMenuBar", "true");
	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SAXSequitur");

	    // model...
	    model = new ItrSequiturModel();
	    
	    // controller...
	    controller = new Controller(model);

	    // view...
	    view = new ItrSequiturView(controller);

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
