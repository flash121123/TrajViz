package edu.gmu.trajviz.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.swing.JFileChooser;

import edu.gmu.trajviz.logic.UserSession;
import edu.gmu.trajviz.model.SequiturMessage;
import edu.gmu.trajviz.model.SequiturModel;
/**
 * Implements the Controller component for TrajViz2 GUI MVC.
 * 
 * @author qz
 */
public class SequiturController extends Observable implements ActionListener {
  private SequiturModel model;

  private UserSession session;
  
  public SequiturController(SequiturModel model) {
	    super();
	    this.model = model;
	    this.session = new UserSession();
	  }
  /**
   * Implements a listener for the "Browse" button at GUI; opens FileChooser and so on.
   * 
   * @return the action listener.
   */
  public ActionListener getBrowseFilesListener() {

    ActionListener selectDataActionListener = new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Data File");

        String filename = model.getDataFileName();
        if (!((null == filename) || filename.isEmpty())) {
          fileChooser.setSelectedFile(new File(filename));
        }

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();

          // here it calls to model -informing about the selected file.
          //
          model.setFileNameOnly(file.getName());
          model.setDataSource(file.getAbsolutePath());
        }
      }

    };
    return selectDataActionListener;
  }
  public ActionListener getLoadFileListener() {
	    ActionListener loadDataActionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        model.loadData(e.getActionCommand());
	       
	      }
	    };
	    return loadDataActionListener;
	  }
  /**
   * This provide Process action listener. Gets all the parameters from the session component
   * 
   * @return
   */
  public ActionListener getProcessDataListener() {
	  ActionListener loadDataActionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  double minLink = session.getMinLink();
	    	  int alphabetSize = session.getAlphabet();
	    	  int minBlocks = session.getMinBlocks();
	    	  int noiseThreshold = session.getNoisePointThreshold();
	    	  
	    	  log("PAA Size: " + minLink + ", Alphabet Size: "
	    	            + alphabetSize + ", Minimal Continous Blocks: " + minBlocks);
	    	  try {
	    		  model.processData(minLink,alphabetSize,minBlocks, noiseThreshold);
	    	  }
	    	  catch (Exception e1){
	    		  e1.printStackTrace();
	    	  }
	      }
	      
	  };
	  return loadDataActionListener;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
	 
    this.setChanged();
    notifyObservers(new SequiturMessage(SequiturMessage.STATUS_MESSAGE,"controller: Unknown action performed " + e.getActionCommand()));
      
  }
  /**
   * Performs logging messages distribution.
   * 
   * @param message the message to log.
   */
  private void log(String message) {
    this.setChanged();
    notifyObservers(new SequiturMessage(SequiturMessage.STATUS_MESSAGE, "controller: " + message));
  }
public UserSession getSession() {
	return this.session;
}
}
