package edu.gmu.trajviz.view;
/**
 * View componet of TrajViz MVC GUI.
 * 
 * @author QingzheLi
 */

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;

import com.roots.map.MapPanel;

import edu.gmu.trajviz.logic.MotifChartData;
import edu.gmu.trajviz.model.SequiturMessage;
import edu.gmu.trajviz.model.SequiturModel;
import edu.gmu.trajviz.util.StackTrace;
import gmu.edu.core.gi.RuleInterval;
import edu.gmu.itr.ItrSeq;
import edu.gmu.trajviz.controller.SequiturController;
import edu.gmu.trajviz.logic.Route;

public class SequiturView implements Observer, ActionListener{
	private static final String APPLICATION_VERSION = "TrajectoryViz 0.0.1: visualizing trajectory patterns.";
	
	// static block - we instantiate the logger
	  //
	  // logging stuff
	  //
	private static Logger consoleLogger;
	private static Level LOGGING_LEVEL = Level.INFO;
	static {
		consoleLogger = (Logger) LoggerFactory.getLogger(SequiturView.class);
		consoleLogger.setLevel(LOGGING_LEVEL);
		  }

		  // relevant string constants go here
		  //
		  private static final String CR = "\n";
		  private static final String TITLE_FONT = "helvetica";

		  // String is the king - constants for actions
		  //
		  /** Select data file action key. */
		  private static final String SELECT_FILE = "select_file";
		  /** Load data action key. */
		  private static final String LOAD_DATA = "load_data";
		  /** Process data action key. */
		  private static final String PROCESS_DATA = "process_data";
		  private static final String OPTIONS_MENU_ITEM = "menu_item_options";

		  /** The action command for About dialog. */
		  private static final String ABOUT_MENU_ITEM = "menu_item_about";

		  /** Frame for the GUI. */
		  private static final JFrame frame = new JFrame(APPLICATION_VERSION);

		  /** The main menu bar. */
		  private static final JMenuBar menuBar = new JMenuBar();	  
		  
		  /** Global controller handler - controller is supplier of action handlers. */
		  private SequiturController controller;

		  // data source related variables
		  //
		  public MapPanel mapPanel,mapPanel1;
		  private JTabbedPane tabbedRulesPane;
		  private SequiturRulesPanel sequiturRulesPane;
		  private JPanel dataSourcePane;
		  private JTextField dataFilePathField;
		  private JButton selectFileButton;
		  private JTextField dataRowsLimitTextField;
		  private JPanel processPane;
		  private SimpleDateFormat logDateFormat = new SimpleDateFormat("HH:mm:ss' '");
		  private boolean isTimeSeriesLoaded = false;
		  private JButton dataLoadButton;
		  private JPanel parametersPane;
		  private JLabel minLinkLabel;
		  private JLabel minBlocksLabel;
		  private JLabel noiseThresholdLabel;
		  private JTextField minBlocksField;
		  private JTextField noiseThresholdField;
		  private JTextField minLinkField;
		  private JTextField alphabetSizeField;
		  
		  private JButton processButton;
		//  private JButton displayRuleOnMapButton;

		  // logging area
		  //
		  private static final JTextArea logTextArea = new JTextArea();
		  private static final JScrollPane logTextPane = new JScrollPane(logTextArea,
		      ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
		      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		  /**
		   * Constructor.
		   * 
		   * @param controller The controller used for the application flow control.
		   */
	public SequiturView(SequiturController controller) {
		this.controller = controller;
	}
	
	public void showGUI(){
		// Schedule a job for the event-dispatching thread:
	    // creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (ClassNotFoundException e){
			          System.err.println("ClassNotFoundException: " + e.getMessage());
				}
				catch (InstantiationException e) {
			          System.err.println("InstantiationException: " + e.getMessage());
			    }
			    catch (IllegalAccessException e) {
			          System.err.println("IllegalAccessException: " + e.getMessage());
			    }
			    catch (UnsupportedLookAndFeelException e) {
			          System.err.println("UnsupportedLookAndFeelException: " + e.getMessage());
			    }
			    catch (Exception e) {
			          System.err.print(StackTrace.toString(e));
			    }
			    configureGUI();
			}
		});
	}
	/**
	   * Initialize the dialog
	*/
	private void configureGUI() {
	    // set look and fill
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    // build main UI components
	    //
	    buildMenuBar();
	      
	    buildDataSourcePane();	
	    buildParameterPane();
	    buildMapPane();
	     buildMapPane1();
	     buildRulesPane();
	   
	
	    buildLogPane();
	    
	    // put listeners in place for the Sequitur rule panel
	    sequiturRulesPane.addPropertyChangeListener(mapPanel1);
	    
	 // set the main panel layout
	    MigLayout mainFrameLayout = new MigLayout("", "[fill,grow,center]",
	        "[][][fill,grow 500][][][][]");
	    frame.getContentPane().setLayout(mainFrameLayout);

	    // set the menu bar
	    frame.setJMenuBar(menuBar);

	    // place panels
	    frame.getContentPane().add(dataSourcePane, "wrap");
	    frame.getContentPane().add(parametersPane, "grow,split");
	    frame.getContentPane().add(processPane, "wrap");
	    
	    frame.getContentPane().add(mapPanel,"w 44%,split");
	    frame.getContentPane().add(tabbedRulesPane, "w 12%, split");
	    frame.getContentPane().add(mapPanel1, "w 44%,wrap");
	 //   frame.getContentPane().add(tabbedRulesPane, "wrap");
	  //  frame.getContentPane().add(logTextPane, "h 80:100:100,wrap");
	    frame.getContentPane().add(logTextPane, "h 80:100:100,wrap");

	    // Show frame
	    frame.pack();
	    frame.setSize(new Dimension(1920, 1000));
	    frame.setVisible(true);
	    
	}

	

	

	private void buildRulesPane() {
		tabbedRulesPane = new JTabbedPane();
		sequiturRulesPane = new SequiturRulesPanel();
		MigLayout sequiturPaneLayout = new MigLayout(",insets 0 0 0 0", "[fill,grow]", "[fill,grow]");
		sequiturRulesPane.setLayout(sequiturPaneLayout);

	    tabbedRulesPane.addTab("Motif/Anomalies Table", null, sequiturRulesPane,"Shows Motif/Anomalies");
	    
	    // now format the tabbed pane
	    //
	    tabbedRulesPane.setBorder(BorderFactory.createTitledBorder(
	        BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
	        "Motif/Anomalies Detail",
	        TitledBorder.LEFT, TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));
	}

	/**
	   * Build the application menu bar.
	   */
	  private void buildMenuBar() {

	    // Build the File menu.
	    //
	    //
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    fileMenu.getAccessibleContext().setAccessibleDescription("The file menu");
	    // Open file item
	    JMenuItem openFileItem = new JMenuItem("Select", KeyEvent.VK_O);
	    openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	    openFileItem.getAccessibleContext().setAccessibleDescription("Open a data file");
	    openFileItem.setActionCommand(SELECT_FILE);
	    openFileItem.addActionListener(this);
	    fileMenu.add(openFileItem);
	    // add a separator
	    fileMenu.addSeparator();
	    // an exit item
	    JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
	    exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
	    exitItem.getAccessibleContext().setAccessibleDescription("Exit from here");
	    exitItem.addActionListener(this);
	    fileMenu.add(exitItem);

	    // Build the Options menu.
	    //
	    //
	    JMenu settingsMenu = new JMenu("Settings");
	    settingsMenu.setMnemonic(KeyEvent.VK_S);
	    settingsMenu.getAccessibleContext().setAccessibleDescription("Settings menu");
	    // an exit item
	    JMenuItem optionsItem = new JMenuItem("GrammarViz options", KeyEvent.VK_P);
	    optionsItem.setActionCommand(OPTIONS_MENU_ITEM);
	    optionsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
	    optionsItem.getAccessibleContext().setAccessibleDescription("Options");
	    optionsItem.addActionListener(this);
	    settingsMenu.add(optionsItem);

	    // Build the About menu.
	    JMenu helpMenu = new JMenu("Help");
	    helpMenu.setMnemonic(KeyEvent.VK_F1);
	    helpMenu.getAccessibleContext().setAccessibleDescription("Help & About");

	    // a help item
	    JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
	    helpItem.getAccessibleContext().setAccessibleDescription("Get some help here.");
	    exitItem.addActionListener(controller);
	    helpMenu.add(helpItem);

	    // an about item
	    JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
	    aboutItem.getAccessibleContext().setAccessibleDescription("About the app.");
	    aboutItem.setActionCommand(ABOUT_MENU_ITEM);
	    aboutItem.addActionListener(this);
	    helpMenu.add(aboutItem);

	    // make sure that controller is connected with Exit item
	    //
	    exitItem.addActionListener(controller);

	    menuBar.add(fileMenu);
	    menuBar.add(settingsMenu);
	    menuBar.add(helpMenu);
	  }
	  private void buildParameterPane() {

		    parametersPane = new JPanel();
		    parametersPane.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "Parameteres & Motif Filters",
		        TitledBorder.LEFT, TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));

		    // insets: T, L, B, R.
		    MigLayout saxPaneLayout = new MigLayout("insets 3 2 2 2",
		        "[][fill,grow]10[][fill,grow]10[][fill,grow]10[][fill,grow]10", "[]");
		    parametersPane.setLayout(saxPaneLayout);

		    // the sliding window parameter
		   
		    minLinkLabel = new JLabel("Minimum Frequency:");
		    minLinkField = new JTextField(String.valueOf(this.controller.getSession().getMinLink()));
		    
		    JLabel alphabetSizeLabel = new JLabel("Grid Cell Size:");
		    alphabetSizeField = new JTextField(String.valueOf(this.controller.getSession()
		        .getAlphabet()));
		    
		    minBlocksLabel = new JLabel("Minumum Motif Length:");
		    minBlocksField = new JTextField(String.valueOf(this.controller.getSession().getMinBlocks()));
		    
		    noiseThresholdLabel = new JLabel("Abnormal Frequency:");
		    noiseThresholdField = new JTextField(String.valueOf(this.controller.getSession().getNoisePointThreshold()));


		    

		    parametersPane.add(alphabetSizeLabel);
		    parametersPane.add(alphabetSizeField);
		    parametersPane.add(minLinkLabel);
		    parametersPane.add(minLinkField);
		    parametersPane.add(minBlocksLabel);
		    parametersPane.add(minBlocksField);
		    parametersPane.add(noiseThresholdLabel);
		    parametersPane.add(noiseThresholdField);
		    // PROCESS button
		    //
		    processButton = new JButton("Process data");
		    processButton.setMnemonic('P');
		    processButton.setActionCommand(PROCESS_DATA);
		    processButton.addActionListener(this);		    
		    processPane = new JPanel();
		    processPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
		    		"Hit to run GI", TitledBorder.LEFT, TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));
		    MigLayout processPaneLayout = new MigLayout("insets 0 2 2 2", "[]","[]");
		    processPane.setLayout(processPaneLayout);
		    processPane.add(processButton,"");
	  }
		    
		    
		    
	  private void buildDataSourcePane() {

		    dataSourcePane = new JPanel();

		    // Layout, insets: T, L, B, R.
		    dataSourcePane.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "Data source", TitledBorder.LEFT,
		        TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));
		    MigLayout dataSourcePaneLayout = new MigLayout("insets 0 2 2 2",
		        "[][fill,grow 80][]10[][fill, grow 20][]", "[]");
		    
		    dataSourcePane.setLayout(dataSourcePaneLayout);
		    // file label
		    //
		    JLabel fileNameLabel = new JLabel("Data file: ");

		    // field
		    dataFilePathField = new JTextField("");
		    fileNameLabel.setLabelFor(dataFilePathField);

		    // the Browse button
		    selectFileButton = new JButton("Browse...");
		    selectFileButton.setMnemonic('B');

		    dataSourcePane.add(fileNameLabel, "");
		    //dataSourcePane.add(dataFilePathField, "");
		    dataSourcePane.add(dataFilePathField,"");
		    dataSourcePane.add(selectFileButton, "");

		    // add the action listener
		    //
		    selectFileButton.addActionListener(controller.getBrowseFilesListener());
		    // dataFilePathField.getDocument().addDocumentListener(controller.getDataFileNameListener());

		    // data rows interval section
		    //
		    JLabel lblCountRows = new JLabel("Row limit (0=all):");
		    dataRowsLimitTextField = new JTextField("0");
		    dataSourcePane.add(lblCountRows, "");
		    dataSourcePane.add(dataRowsLimitTextField, "");

		    // the load button
		    //
		    dataLoadButton = new JButton("Load data");
		    dataLoadButton.setMnemonic('L');
		    // add the action listener
		    dataLoadButton.setActionCommand(LOAD_DATA);
		    dataLoadButton.addActionListener(this);
		    dataSourcePane.add(dataLoadButton, "");

		    // PROCESS button
		    //
		    processButton = new JButton("Process data");
		    processButton.setMnemonic('P');
		    processButton.setActionCommand(PROCESS_DATA);
		    processButton.addActionListener(this);		    
		    processPane = new JPanel();
		    processPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
		    		"Hit to run GI", TitledBorder.LEFT, TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));
		    MigLayout processPaneLayout = new MigLayout("insets 0 2 2 2", "[]","[]");
		    processPane.setLayout(processPaneLayout);
		    processPane.add(processButton,"");
	  }	  
	  /**
	   * Build the map panel.
	   */
	  private void buildMapPane() {
		  mapPanel = new MapPanel();
		  mapPanel.setBorder(BorderFactory.createTitledBorder(
			        BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "All Motifs", TitledBorder.LEFT,
			        TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));
		 // MigLayout mapPaneLayout = new MigLayout("insets 0 0 0 0", "[fill,grow]", "[fill,grow]");
		 // mapPanel.setLayout(mapPaneLayout);
		  mapPanel.setZoom(3);  // set some zoom level (1-18 are valid)
		 
		  Point position = mapPanel.computePosition(new Point2D.Double(-150, 70));
		  mapPanel.setCenterPosition(position);
		  mapPanel.repaint();
		  
		  
	  }
	  private void buildMapPane1() {
		  mapPanel1 = new MapPanel();
		  mapPanel1.setBorder(BorderFactory.createTitledBorder(
			        BorderFactory.createEtchedBorder(BevelBorder.LOWERED), "Motif Details", TitledBorder.LEFT,
			        TitledBorder.CENTER, new Font(TITLE_FONT, Font.PLAIN, 10)));
			
		}
	  /**
	   * Build the logging panel.
	   */
	  private void buildLogPane() {
		  // logging panel
		    logTextArea.setFont(new Font("MonoSpaced", Font.PLAIN, 10));
		    logTextArea.setEditable(false);
		    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
		    logTextPane.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		    logTextPane.setAutoscrolls(true);
		    log(Level.INFO, "running TrajViz 1.0 demo");
		    log(Level.INFO, "require data file format: latitude, longitude, status, time in UNIX epoch format");
	}
	  /**
	   * Logs message.
	   * 
	   * @param level The logging level to use.
	   * @param message The log message.
	   */
	  protected void log(Level level, String message) {
	    message = message.replaceAll("\n", "");
	    String dateStr = logDateFormat.format(System.currentTimeMillis());
	    if (message.startsWith("model") || message.startsWith("controller")) {
	      logTextArea.append(dateStr + message + CR);
	    }
	    else {
	      logTextArea.append(dateStr + "view: " + message + CR);
	    }
	    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
	    consoleLogger.info(dateStr + message);
	  }
	  
	  @Override
	  public void actionPerformed(ActionEvent arg) {

	    // get the action command code
	    //
	    String command = arg.getActionCommand();

	    // treating options
	    //
	    
	    /*
	     * don't use this session yet. 
	     *
	     //
	    if (OPTIONS_MENU_ITEM.equalsIgnoreCase(command)) {
	      log(Level.INFO, "options menu action performed");
	      ParametersPane parametersPanel = new ParametersPane(this.controller.getSession());
	      ParametersDialog parametersDialog = new ParametersDialog(frame, parametersPanel,
	          this.controller.getSession());
	      parametersDialog.setVisible(true);
	    }
*/
	    // showing up the about dialog
	    //
	    if (ABOUT_MENU_ITEM.equalsIgnoreCase(command)) {
	      log(Level.INFO, "about menu action performed");
	      AboutGrammarVizDialog dlg = new AboutGrammarVizDialog(frame);
	      dlg.clearAndHide();
	    }

	    if (SELECT_FILE.equalsIgnoreCase(command)) {
	      log(Level.INFO, "select file action performed");
	      controller.getBrowseFilesListener().actionPerformed(null);
	    }

	    if (LOAD_DATA.equalsIgnoreCase(command)) {
	      log(Level.INFO, "load data action performed");
	      
	      
	      this.isTimeSeriesLoaded = false;
	      if (this.dataFilePathField.getText().isEmpty()) {
	        raiseValidationError("The file is not yet selected.");
	      }
	      else {
	        String loadLimit = this.dataRowsLimitTextField.getText();
	       
	        this.controller.getLoadFileListener().actionPerformed(new ActionEvent(this, 1, loadLimit));
	        
	        ArrayList<ArrayList<Route>> initialMotifs = new ArrayList<ArrayList<Route>>();
	        ArrayList<Route> initialTrajjectories = new ArrayList<Route>();
	        
	        mapPanel.setMotifs(initialMotifs);
	        
	    	mapPanel.setAllTrajectories(initialTrajjectories);
	    	mapPanel.setAllAnomalies(initialTrajjectories);
	    	mapPanel1.setMotifs(initialMotifs);
	        
	        mapPanel.setZoom(12);  // set some zoom level (1-18 are valid)
	        mapPanel1.setZoom(12);
	     //   mapPanel1.setRuleDetails(-1);
			  double lat = SequiturModel.getLatitudeCenter();
			  
			  double lon = SequiturModel.getLongitudeCenter();
			  System.out.println("View:  lat:  "+lat+"       lon: "+lon);
			  Point position = mapPanel.computePosition(new Point2D.Double(lon, lat));
			  mapPanel.setCenterPosition(position);
			  mapPanel.repaint();
			  mapPanel1.setCenterPosition(position);
			 
			  mapPanel1.repaint();
			  this.isTimeSeriesLoaded = true;
	      }
	    }

	    else if (PROCESS_DATA.equalsIgnoreCase(command)) {
	      log(Level.INFO, "process data action performed");
	      if (this.isTimeSeriesLoaded) {
	    	//  mapPanel1.setRuleDetails(-1);  //reset the to -1 to avoid IndexOutOfBoundException
	        // check the values for window/minimum link threshold/alphabet, etc.
	    	  this.controller.getSession().setMinLink(Double.valueOf(this.minLinkField.getText()));
	    	  this.controller.getSession().setAlphabet(Integer.valueOf(this.alphabetSizeField.getText()));
	    	  this.controller.getSession().setMinBlocks(Integer.valueOf(this.minBlocksField.getText()));
	    	  this.controller.getSession().setNoisePointThreshold(Integer.valueOf(this.noiseThresholdField.getText()));
	    	  this.controller.getProcessDataListener().actionPerformed(new ActionEvent(this,2,null));
	    	  
	    	  sequiturRulesPane.resetSelection();
//	    	  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@--------------------------------");
	    	  sequiturRulesPane.resetPanel();
	    	  mapPanel1.setRuleDetails2(-1);
//	    	  System.out.println("fffffffffffffffff"+  mapPanel1.getRuleDetails());
	    	  //mapPanel.setBlocks(SequiturModel.getLatOri(), SequiturModel.getLonOri());
	    	  mapPanel.setMotifs(SequiturModel.getMotifs());
	    	  mapPanel.setAllTrajectories(SequiturModel.getRawTrajectory());
	    	 
	    	  mapPanel.setAllAnomalies(SequiturModel.getAnomaly());
	    	  mapPanel1.setMotifs(SequiturModel.getMotifs());
	    	 
//	    	  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@"+  mapPanel1.getRuleDetails());
	    	  
	       }
	      else {
	        raiseValidationError("The timeseries is not loaded yet.");
	      }
	    }
	  }
	  private void raiseValidationError(String message) {
		    JOptionPane.showMessageDialog(frame, message, "Validation error", JOptionPane.ERROR_MESSAGE);
		  }
	  @Override
	  public void update(Observable o, Object arg) {
	    if (arg instanceof SequiturMessage) {
	      final SequiturMessage message = (SequiturMessage) arg;
	    
	 // new FileName
	      //
	      if (SequiturMessage.DATA_FNAME.equalsIgnoreCase(message.getType())) {
	        Runnable doHighlight = new Runnable() {
	          @Override
	          public void run() {
	            dataFilePathField.setText((String) message.getPayload());
	            dataFilePathField.repaint();
	           
	          }
	        };
	        SwingUtilities.invokeLater(doHighlight);
	      }

	      // new log message
	      //
	      else if (SequiturMessage.STATUS_MESSAGE.equalsIgnoreCase(message.getType())) {
	        log(Level.ALL, (String) message.getPayload());
	      //  log(Level.ALL, (String) message.getPayload1());
	      }
	      else if (SequiturMessage.CHART_MESSAGE.equalsIgnoreCase(message.getType())) {
	    	  MotifChartData chartData = (MotifChartData) message.getPayload();
	    	  @SuppressWarnings("unchecked")
			ArrayList<ArrayList<RuleInterval>> ruleIntervals = (ArrayList<ArrayList<RuleInterval>>) message.getPayload1();
	    	@SuppressWarnings("unchecked")
			//ArrayList<HashSet<Integer>> map = (ArrayList<HashSet<Integer>>) message.getPayload2();
	    	  
	    	ArrayList<Integer> ruleMapLength = (ArrayList<Integer>) message.getPayload2();

	    //	  System.out.println("Check Payload:::::::::::::::::::"+filteredRulesMap );
	    	  sequiturRulesPane.resetSelection();
//	    	  System.out.println("---------------------------------------------");
	    	  mapPanel1.setRuleDetails2(-1);
	    	  //sequiturRulesPane.setRulesData(chartData,ruleIntervals,map,ItrSeq.rn);//,frequency);
	    	  sequiturRulesPane.setRulesData(chartData,ruleIntervals,null, ItrSeq.rn,ruleMapLength);
	    	  frame.validate();
	    	  frame.repaint();
	    	//  sequiturRulesPane.clear();
	    	 // frame.repaint();
	      }
	      else if (SequiturMessage.TIME_SERIES_MESSAGE.equalsIgnoreCase(message.getType())) {
	    	 // sequiturRulesPane.clear();
	    	  
	    	  frame.repaint();
	    	  this.isTimeSeriesLoaded = true;
	    	  
	      }
	    }
	  }
}	