package edu.gmu.trajviz.model;
/*
 * Author: Qingzhe Li
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.roots.map.MapPanel;

import edu.gmu.itr.ItrSeq;
import edu.gmu.itr.RuleDensityEstimator;
import edu.gmu.trajviz.gi.GrammarRuleRecord;
import edu.gmu.trajviz.gi.GrammarRules;
import edu.gmu.trajviz.gi.sequitur.SAXMotif;
import edu.gmu.trajviz.gi.sequitur.SAXRule;
import edu.gmu.trajviz.gi.sequitur.SequiturFactory;
import edu.gmu.trajviz.logic.*;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.gmu.trajviz.sax.datastructures.SAXRecords;
import edu.gmu.trajviz.timeseries.TSException;
import edu.gmu.trajviz.util.StackTrace;
public class SequiturModel extends Observable {
//	public static double MINLINK = 0.0;
//	public final static double (minLink*2) = 0.0;
	public final static int EVAL_RESOLUTION = 100;
	private ArrayList<Integer> ruleMapLength;
	
	//new added global variable for new rules
	private ArrayList<ArrayList<RuleInterval>> newRules;
	
	final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	public final static String EVALUATION_HEAD = "DataName,MinLink,AlphabetSize,MinBlocks,NCThreshold,RunningTime,AvgDistance,AvgeStdDev,MinInterDistance,SilhouetteCoefficient,TotalRules,TotalDataPoints, TotalSubTrajectories,CoveredPoints, ImmergableRuleCount\n";
//	public static final int ALPHABETSIZE = 50;
//	public static final int CONTINUALBLOCKTHRESHOLD = 10;
	//public static final int paaSize = 10;
	private ArrayList<Integer> status;
	private static final String SPACE = " ";
	private static final String CR = "\n";
	private static final int STEP = 2;
	

	private void runSequitur(int iteration) throws Exception {
			chartData = new MotifChartData(this.dataFileName, lat, lon, 1, alphabetSize); //PAA is always 1.
			  clusters = new ArrayList<HashSet<Integer>>();
			  filter = new ArrayList<Integer>();
			  clusterMap = new HashMap<Integer,Integer>();
			  mapToPreviousR0 = new ArrayList<Integer>();
			  
			  
			  rules = new GrammarRules();
				try{
				  SAXRecords saxFrequencyData = null;
				  saxFrequencyData = SequiturFactory.entries2SAXRecords(trimedTrack);
				  System.out.println("Input String Length: " + countSpaces(saxFrequencyData.getSAXString(SPACE)));
				  consoleLogger.trace("String: " + saxFrequencyData.getSAXString(SPACE));
				  //System.out.println("String: "+ saxFrequencyData.getSAXString(SPACE));
				  consoleLogger.debug("running sequitur...");
				  ItrSeq ss=new ItrSeq(saxFrequencyData.getSAXString(" "));
				  ss.setAlt(lat);
				  /*if(flag)
				  {	  */
				  tmprule = ss.run(saxFrequencyData);
				  flag=false;
				  //}
				  
				  //SAXRule sequiturGrammar = SequiturFactory.runSequitur(saxFrequencyData.getSAXString(SPACE));
				  //System.out.println("sequiturGrammar: "+sequiturGrammar.toGrammarRulesData().getRuleRecord(1));
				  consoleLogger.debug("collecting grammar rules data ...");
				 // GrammarRules rules1 = sequiturGrammar.toGRD();
				 // System.out.println("rules size: "+ rules1.size());			 
		          
				  //rules = sequiturGrammar.toGrammarRulesData();
		          
		          //new added part for testing yifeng's input
//		          HashMap<String, ArrayList<RuleInterval>> newRule = new HashMap<String, ArrayList<RuleInterval>> ();
//		          ArrayList<RuleInterval> arrRule = new ArrayList<RuleInterval>();
//		          arrRule.add(new RuleInterval(12241, 12574));
//		          arrRule.add(new RuleInterval(12576, 12909));
//		          arrRule.add(new RuleInterval(13399, 13731));
//		          newRule.put("R0",arrRule);		     
//		          rules = convert(newRule);
		          
		          //rules.setParsedString();          
		          
		          //allRules.add(rules);
		         // System.out.println("rules size: "+ rules.size());
		          //debug
		          
		          
		          consoleLogger.debug("mapping rule intervals on timeseries ...");
		          HashMap<String, Integer> hm = new HashMap<String, Integer>();
		          //GrammarRuleRecord rule0 = rules.get(0);
		          
		          //String rule0 = rules.get(0).getRuleString();
		          
		          //int length3 = countSpaces(rule0.getRuleString());
		          
		          //r0 = rule0.getRuleString().split(" ");
		          //System.out.println("R0 = "+r0);
		          //int length4 = r0.length;
		          //if (length3!=length4)
	        	  //	  throw new IndexOutOfBoundsException(length3+":"+length4);;
		          //allR0.add(r0);
		          /*
		          for(int i = 0; i<rules.size();i++){
		        	  String key = rules.get(i).getRuleName();
		        	 // String expandedString = rules.get(i).getExpandedRuleString();
		        	//  System.out.println(rules.get(i));
		        	  hm.put(key, 0);
		          }
		          */
		          
		          //System.out.println("R0: "+rule0.getRuleString());
		          //System.out.print("r0: ");
		          
		          /*
		          for(int i = 0; i<r0.length;i++){
		        	          
		          System.out.print(r0[i]+" ");
		          }
		          System.out.println();
		          */
		      //    System.out.println(r0);
		          /*
		          int currentIdx = 0;
		       //   int[] indexes = new int[r0.length];
		         
		          for(int i=0;i<r0.length;i++){
		        	  if(r0[i]==" ")
		        		  throw new IndexOutOfBoundsException(i+" : |"+r0[i]+"|");
  
		        	  if(r0[i].charAt(0)=='R')
		        		  {
		        		  	Integer currentRule = Integer.valueOf(r0[i].substring(1));
		        		  	hm.put(r0[i], hm.get(r0[i])+1);
		        		  	rules.get(currentRule).addR0Occurrence(currentIdx); // setOccurenceInR0
		        		  	mapToPreviousR0.add(currentIdx);
		        		  //	System.out.println(i+" : "+r0[i]+":"+currentIdx+" ");
		        		  	int length1 = rules.get(currentRule).getRuleYield();
		        		  	int length2 = countSpaces(rules.get(currentRule).getExpandedRuleString());
		        		    currentIdx = currentIdx + rules.get(currentRule).getRuleYield();
		        		    if(currentIdx>mapToOriginalTS.size())
		        		    	
		        		    {
				        		  throw new IndexOutOfBoundsException(i+" : "+r0[i]+":"+currentIdx+" expandRule:  "+rules.get(currentRule).getExpandedRuleString()+" length1:length2 = "+length1+":"+length2);

		        		    }
		        		  }
		        	  else
		        		  {
		        			  
		        		  mapToPreviousR0.add(currentIdx);
	
		        //		  System.out.println(i+" : "+r0[i]+":"+currentIdx+" ");
		        		  
		        		  	currentIdx++;
		        		  	if(currentIdx>mapToOriginalTS.size())
		        		    	
		        		    {
				        		  System.out.println(i+" : "+r0[i]+":"+currentIdx);

		        		    }
		        		  }
		        		  
		          }
		          System.out.println();
		          System.out.print("mapToPreviousR0: ");
		  //        printArrayList(mapToPreviousR0);
		          
		          for(int i = 1; i<rules.size();i++){
		        	  
		        	  String key = rules.get(i).getRuleName();
		        	  rules.get(i).setFrequencyInR0((hm.get(key)).intValue());
		          }
		          
		          
		          SequiturFactory.updateRuleIntervals(rules, saxFrequencyData, lat.size());   //Both update intervals and intervals in R0
		          
		          */
		        /* print all rule details
		         */
		        /* 
		          for(int i=0;i<rules.size();i++){
		        	  System.out.println("Rule number: "+rules.getRuleRecord(i).getRuleNumber()+" Fre in R0: "+rules.get(i).frequencyInR0()+" LEVEL: "+rules.get(i).getRuleLevel()+" "+rules.get(i)+" StringOccurence: "+rules.getRuleRecord(i).occurrencesToString()+"OccurenceInR0: "+rules.get(i).r0OccurrencesToString()+" Rule String: "+rules.getRuleRecord(i).getExpandedRuleString()+" Rule Positions: "+rules.getRuleRecord(i).getRuleIntervals());
		          }
		         */
		       /*  */
		         
	
		          
		       //  if(this.alphabetSize<=100)
		      //   clusterRules();
		       
		          
		        	  //System.out.print("mapToOriginalTS: ");
		        	  //ArrayList<Integer> previousMapToOriginalTS = mapToOriginalTS;
		        	//  printArrayList(previousMapToOriginalTS);
		        	  /*new ArrayList<Integer>();
		        	  
		        	  for (int i = 0; i<mapToOriginalTS.size();i++)
		        	  	{
		        		  previousMapToOriginalTS.add(mapToOriginalTS.get(i));
		        	  	  System.out.print( mapToOriginalTS.get(i) + " ");
		        	  	}
		             System.out.println();
		             */
		          
		          //    mapToOriginalTS = new ArrayList<Integer>();
		          /*
		              for(int i = 0; i<r0.length;i++){
		            	          
		              System.out.print(r0[i]+" ");
		              }
		              System.out.println();
					  trimedTrack = new ArrayList<NumerosityReductionMapEntry>();
					  */
				  /*
				   * Replace Rules' Ids with Clusters' Ids
				   */
		          /*
					  System.out.println("r0.length: "+r0.length);
		          for (int i = 0; i<r0.length;i++){
		        	  
		        	  NumerosityReductionMapEntry<Integer, String> entry;
		        	//  System.out.println("r0_"+i+"="+r0[i] );
		        	  if (r0[i].charAt(0)=='R')
		        		  {
		        		  //	if(i==0)
		        		  	//	System.out.println("r0[i] = "+r0[i]);
		        		  	Integer ruleNumber = Integer.parseInt(r0[i].substring(1));
		        		  	String currentRule = "I"+iteration+"r"+ruleNumber;
		        		  	sortedRuleMap.put(currentRule, rules.get(ruleNumber));
		        		  //	System.out.println("sortedRuleMap.size() = " + sortedRuleMap.size()+" "+currentRule+" : "+rules.get(ruleNumber)+" "+sortedRuleMap);
		        		  	sortedCounter++;
		        		//  	int cursor = rules.get(ruleNumber).getCursor(); 
	
		        		  	if (clusterMap.containsKey(filterMap.get(ruleNumber))){
		        		  			hasNewCluster = true;
		        		  			String s = "I" + (iteration) + "C" + clusterMap.get(filterMap.get(ruleNumber));
		        		  			r0[i] = s;
		        		  			Integer pos = getPositionsInTS(mapToPreviousR0,previousMapToOriginalTS,i);
		        		  			mapToOriginalTS.add(pos);
		        //	  				System.out.println("BlockID: " +r0[i]+" : "+pos);//mapTrimed2Original.get(mapToPreviousR0.get(i)));
	
		        		  			entry = new NumerosityReductionMapEntry<Integer, String>(pos, s);
		        		  			trimedTrack.add(entry);
		        		  		
		        		  	}
		        		  	else{
		        		  			String s = "I" + (iteration) + "r"+ruleNumber;
		        		  			r0[i] = s;
		        		  			Integer pos = getPositionsInTS(mapToPreviousR0,previousMapToOriginalTS,i);
		        		  			mapToOriginalTS.add(pos);
	
	//	        		  			System.out.println("RuleID: " +r0[i]+" : "+pos);
	
		        		  			entry = new NumerosityReductionMapEntry<Integer, String>(pos, s);
		        	  				trimedTrack.add(entry);
			
		        		  	
		        		  	}
		        		  		
		        			
		        		  	
		        		  }
		        	  else
		        		  
		        	  {
				  		
		        		   
		        		  	Integer pos = getPositionsInTS(mapToPreviousR0,previousMapToOriginalTS,i);
				  			mapToOriginalTS.add(pos);
	
			  				entry = new NumerosityReductionMapEntry<Integer, String>(pos, r0[i]);
			  	//			System.out.println("BlockID: " +r0[i]+" : "+pos);//mapTrimed2Original.get(mapToPreviousR0.get(i)));
			  				trimedTrack.add(entry);
	
		        	  }
		          }
		          */
		          /*
		          System.out.println("after:");
		          for (int d = 0; d<words.size(); d++)
		        	  System.out.print(words.get(d)+ " ");
		          System.out.println();
		          */
		          
		          consoleLogger.debug("done ...");
		          
		          
		 
		          //this.setNewRules(tmprule);
		          chartData.setGrammarRules(ItrSeq.arules);
		          
		          //new added part starts ----------------------------------------------------------
//		          System.out.println(rules.size());
//		          Iterator<GrammarRuleRecord> iter = rules.iterator();
//		          
//		          while(iter.hasNext()) {
//		        	  System.out.println("---------"+ iter.next().getRuleIntervals());
//		          }
		         
		          
		          
		          
		        //new added part ends ---------------------------------------------------------------
		          
		          System.out.println("chartData size: "+ chartData.getRulesNumber());
				
		
			  }
			  catch (TSException e){
				  this.log("error while processing data "+StackTrace.toString(e));
				  e.printStackTrace();
			  }
			//  allMapToOriginalTS.add(mapToOriginalTS);
			  		
		}
	
	//private static final int DEFAULT_TIME_GAP = 6;//180;
	private static final int DEFAULT_TIME_GAP = 180;
    private boolean[] isCovered;
    private boolean[] groundTruth;
    private int breakPoint; // the positions<breakPoint are normal, otherwise are abnormal.
    private int trueAnomalyCount;
    private int falsePositiveCount;
    private int trueNegativeCount;
    private int falseNegativeCount;
    private boolean[] ruleCovered;

//	private static final int NOISYELIMINATIONTHRESHOLD = 5;
	public static int alphabetSize;
	private double minLink;
	private int noiseThreshold;
	private GrammarRules rules;
	public static HashMap<String, ArrayList<String>> allPostions;
	public static ArrayList<GrammarRules> allRules;
	public static TreeMap<String, GrammarRuleRecord> sortedRuleMap;
	public static ArrayList< ArrayList<HashSet<Integer>>> allClusters;
	private ArrayList<HashSet<Integer>> clusters;
	private ArrayList<Integer> filter;
	public static ArrayList<ArrayList<Integer>> allFilters;
	private HashMap<Integer,Integer> filterMap;
	HashMap<Integer, Integer> clusterMap;
	private ArrayList<Integer> mapTrimed2Original; 
	private ArrayList<Integer> mapToPreviousR0;
	private ArrayList<Integer> mapToOriginalTS;
	public static ArrayList<ArrayList<Integer>> allMapToOriginalTS;
	public static ArrayList<ArrayList<Integer>> allMapToPreviousR0;
	public static HashMap<String, ArrayList<RuleInterval>> finalIntervals;
	private boolean hasNewCluster = true;
	private int sortedCounter;
	private String dataFileName, fileNameOnly;
	private static double lat_center;
	public static double latMax;
	public static double latMin;
	public static double lonMin;
	public static double lonMax;
	public int trajCounter;
	private int coverCount;
	private int immergableRuleCount;
	private int totalSubTrajectory;
	private String[] r0;
	public static ArrayList<String[]> allR0;
	private static double lon_center;
	//The outer arrayList includes all rules, the inner arrayList includes all route under the same rule
	private static ArrayList<ArrayList<Route>> routes;  
	
	
	private static ArrayList<Route> rawRoutes;  
	
	private static ArrayList<Route> anomalyRoutes;
	public static ArrayList<Double> lat;
	private ArrayList<Double> ncLat = new ArrayList<Double>();
	private ArrayList<Double> ncLon = new ArrayList<Double>();
	//public ArrayList<Double> paaLat;
	//public ArrayList<Double> paaLon;
	public static ArrayList<Double> lon;
	public static ArrayList<Double> latOri;
	public static ArrayList<Double> lonOri;
	private MotifChartData chartData;
	private ArrayList<ArrayList<RuleInterval>> ruleIntervals;
	private ArrayList<RuleInterval> rawAllIntervals;
	private ArrayList<RuleInterval> anomalyIntervals;
	private ArrayList<RuleInterval> anomalRuleIntervals;
//	private ArrayList<HashSet<Integer>> mapToOriginRules;
	private double runTime = -1;
	//private static GrammarRules filteredRules;
	@SuppressWarnings("rawtypes")
	public ArrayList<NumerosityReductionMapEntry> trimedTrack;
	// index is the rule# after filtering, Integer value is the actual rule number
//	private ArrayList<Integer> filteredRuleMap = new ArrayList<Integer>(); 
	private ArrayList<String> words;
	//public Blocks blocks, eBlocks;
	public Blocks eBlocks;
	//public Blocks tblocks;
	public AdaptiveBlocks blocks;
	private int minBlocks;


	private ArrayList<ArrayList<RuleInterval>> tmprule;


	private boolean flag=true;
	public static ArrayList<ArrayList<Route>> motifs; 
	private static Logger consoleLogger;
	  private static Level LOGGING_LEVEL = Level.DEBUG;
	
	  static {
	    consoleLogger = (Logger) LoggerFactory.getLogger(SequiturModel.class);
	    consoleLogger.setLevel(LOGGING_LEVEL);
	}
	 /**
	   * The file name setter.
	   * 
	   * @param filename The file name to set.
	   */
	  /*
	  public static GrammarRules getFilteredGrammarRules(){
		  return filteredRules;
	  }
	  */
	  private synchronized void setDataFileName(String filename) {
	    this.dataFileName = filename;
	  }

	  /**
	   * The file name getter.
	   * 
	   * @return current filename.
	   */
	  public synchronized String getDataFileName() {
		return this.dataFileName;
	}
	  public synchronized void setFileNameOnly(String filename) {
		  fileNameOnly = filename;
		   
		  }
	  public synchronized void setDataSource(String filename) {

		    consoleLogger.info("setting the file " + filename + " as current data source");

		    // action
		    this.setDataFileName(filename);

		    // notify the View
		    this.setChanged();
		    notifyObservers(new SequiturMessage(SequiturMessage.DATA_FNAME, this.getDataFileName()));

		    // this notification tells GUI which file was selected as the data source
		    this.log("set file " + filename + " as current data source");

		  }
	  /**
	   * Load the data which is supposedly in the file which is selected as the data source.
	   * 
	   * @param limitStr the limit of lines to read.
	   */
	  public synchronized void loadData(String limitStr) {
		  if((null == this.dataFileName)	|| this.dataFileName.isEmpty()){
			  this.log("unable to load data - no data source select yet");
			  return;
		  }
		  Path path = Paths.get(this.dataFileName);
		  if (!Files.exists(path)){
			  this.log("file"+ this.dataFileName + "doesn't exist.");
			  return;
		  }
		  // read the input
		  // init the data array
		  ArrayList<Double> data = new ArrayList<Double>();
		  ArrayList<Double> data1 = new ArrayList<Double>();
		  status = new ArrayList<Integer>();    // taxi loading status
		  ArrayList<Long> timeAsUnixEpoc = new ArrayList<Long>();   //number of seconds since Jan. 1 1970 midnight GMT, if the time is in milliseconds, it will need to be converted to seconds,otherwise it may over Integer's limite. 
		  ArrayList<Double> as=new ArrayList<Double>();
		  try{
			  long loadLimit = 0l;
			  if(!(null == limitStr)&&!(limitStr.isEmpty())){

				  loadLimit = Long.parseLong(limitStr);
			  }
				
			  BufferedReader reader = Files.newBufferedReader(path, DEFAULT_CHARSET);
				  String line = null;
				  long lineCounter = 0;
				  int trajectoryCounter = -1001;
				  
				  while ((line = reader.readLine()) !=null){
					  String[] lineSplit = line.trim().split("\\s+|,");
					  double value = new BigDecimal(lineSplit[0]).doubleValue();
					  
					  double value1 = new BigDecimal(lineSplit[1]).doubleValue();
					  int value2 = Integer.parseInt(lineSplit[2]);
					  long value3 = Long.parseLong(lineSplit[3]);
					  as.add((double)value3);
					  
					 /*
					  if(value2==1000)
						  breakPoint = status.size();
					  */
					//  if (value>=37.7254&&value<=37.8212&&value1>=-122.5432&&value1<=-122.3561)
					  {
					  if((lineCounter<=1)||(Math.abs(value3-timeAsUnixEpoc.get(timeAsUnixEpoc.size()-1))<=DEFAULT_TIME_GAP &&(value3-timeAsUnixEpoc.get(timeAsUnixEpoc.size()-1))!=0))
					  {
						  
					  
						  if((value<=90)&&(value>=-90))
						  {
							  data.add(value);
					  
							  data1.add(value1);
							  status.add(value2);
							 
							  timeAsUnixEpoc.add(value3);
						  }
						  else
						  {
							  data.add(value1);
							  data1.add(value);
							  status.add(value2);
							  timeAsUnixEpoc.add(value3);
						  }
					  }
					  else{
						  data.add((double)trajectoryCounter);  //adding dummy point to split two trajectories
						  data1.add((double)trajectoryCounter);
						  trajectoryCounter--;
						  status.add(-1);
						  timeAsUnixEpoc.add(value3);
						  //following is adding the first point of a new trajectories
						  if((value<=90)&&(value>=-90))
						  {
							  data.add(value);
					  
							  data1.add(value1);
							  status.add(value2);
							  timeAsUnixEpoc.add(value3);
						  }
						  else
						  {
							  data.add(value1);
							  data1.add(value);
							  status.add(value2);
							  timeAsUnixEpoc.add(value3);
						  }
					  }
				lineCounter++;
				  }
				if((loadLimit>0&&(lineCounter>=loadLimit))){
					break;
				}
			  }
				  data.add((double)trajectoryCounter);
				  data1.add((double)trajectoryCounter);
				  trajCounter = 0 - (trajectoryCounter+1000);
				  status.add(-1);
				  timeAsUnixEpoc.add(timeAsUnixEpoc.get(timeAsUnixEpoc.size()-1));
			  reader.close();
		  }
		  catch (Exception e){
			  String stackTrace = StackTrace.toString(e);
			  System.err.println(StackTrace.toString(e));
			  this.log("error while trying to read data from " + this.dataFileName + ":\n" + stackTrace);
		  }
		  
		
			  this.latOri = new ArrayList<Double>();
			  this.lonOri = new ArrayList<Double>();
		double a1=Collections.max(as);
		double a2=Collections.min(as);
		
		latMax = Double.valueOf(data.get(0));
		lonMax = Double.valueOf(data1.get(0));
		latMin = Double.valueOf(data.get(0));
		lonMin = Double.valueOf(data1.get(0));
		  for(int i = 0; i<data.size(); i++){
			  double temp_latitude = Double.valueOf(data.get(i));
			  double temp_longitude = Double.valueOf(data1.get(i));
			//  System.out.println("i = "+i+": "+temp_latitude+","+temp_longitude);
			  this.latOri.add(temp_latitude);
			  this.lonOri.add(temp_longitude);
			  if((temp_latitude>=-90)&&temp_latitude>latMax)
				  
				  {
				   		
				  latMax = temp_latitude;
				  }
			  if((temp_latitude>=-90)&&temp_latitude<latMin)
				  {
				  
				  latMin = temp_latitude;
				  }
			  if(temp_longitude>=-180&&temp_longitude>lonMax)
				  lonMax = temp_longitude;
			  if(temp_longitude>=-180&&temp_longitude<lonMin)
				  lonMin = temp_longitude;
			//test loaded points
		//	  System.out.println(this.lat.get(i)+", "+this.lon.get(i)+","+data.get(i)+", "+data1.get(i));
		  }
		  data = new ArrayList<>();
		  data1 = new ArrayList<>();
		  lat_center = (latMax+latMin)/2;

		  lon_center = (lonMax+lonMin)/2;
		  System.out.println("lonMax:  "+lonMax+"       lonMin: "+lonMin);

		  System.out.println("latMax:  "+latMax+"       latMin: "+latMin);
		  System.out.println("Number of trajectories: "+trajCounter);
		  consoleLogger.debug("loaded " + this.latOri.size() + " points and "+trajCounter+" Trajecoties... ");
		 
		  this.log("loaded " + this.latOri.size() + " points from " + this.dataFileName);
		  
		  
		  
		  setChanged();
		  notifyObservers(new SequiturMessage(SequiturMessage.TIME_SERIES_MESSAGE, this.latOri,this.lonOri));
		  
	}
	  public static double getLatitudeCenter(){
		  return lat_center;
	  }
	  public static double getLongitudeCenter(){
		  return lon_center;
	  }
	  @SuppressWarnings("rawtypes")
	
	  //This is the method to transform yifeng's rule output to the type of GrammarRules
//	  private GrammarRules convert(HashMap<String, ArrayList<RuleInterval>> rules) {
//		  GrammarRules newRules = new GrammarRules();
//		  Iterator it = rules.keySet().iterator();  
//	      while(it.hasNext()) {  
//	    	  GrammarRuleRecord rule = new GrammarRuleRecord();
//	    	  
//	    	  String key = (String)it.next();  
//	    	  rule.setRuleNumber(Integer.parseInt(key.replaceAll("R|r", "")));
//	    	  rule.setRuleIntervals(rules.get(key));  
//	    	  newRules.addRule(rule);
//	      }  
//		  return newRules;
//	  }
	  
	  private void convert(ArrayList<ArrayList<RuleInterval>> rules) {
		  ruleIntervals = rules;
		  ArrayList<ArrayList<Route>> r=new ArrayList<ArrayList<Route>>(rules.size());
		  for(ArrayList<RuleInterval> x : rules)
		  {
			  ArrayList<Route> tmp=new ArrayList<Route>();
			  for(RuleInterval y : x)
			  {
				  ArrayList<Double> m1=new ArrayList<Double>(y.getLength());
				  ArrayList<Double> m2=new ArrayList<Double>(y.getLength());
				  
				  for(int i=y.startPos;i<=y.endPos-1;i=i+2)
				  {
					  m1.add(lat.get(i));
					  m2.add(lon.get(i));
				  }
				  m1.add(lat.get(y.endPos));
				  m2.add(lon.get(y.endPos));
				  Route ro=new Route(m1,m2);
				  tmp.add(ro);
			  }
			  r.add(tmp);
		  }
		  motifs=r;
		  MapPanel.createDenseMap(motifs);
		  
	  }
	  
	  
	  public synchronized void processData(double minLink, int alphabetSize, int minBlocks, int noiseThreshold)throws IOException{
		  sortedCounter = 0;
		  this.minLink = minLink;
		  this.minBlocks = minBlocks;
		  this.noiseThreshold = noiseThreshold;
		  this.alphabetSize = alphabetSize;
		  this.allRules = new ArrayList<GrammarRules>();
		  this.allFilters = new ArrayList<ArrayList<Integer>>();
		  this.allClusters = new ArrayList<ArrayList<HashSet<Integer>>>();
		  this.allR0 = new ArrayList<String[]>();
		  this.allMapToPreviousR0 = new ArrayList<ArrayList<Integer>>();
		  this.allMapToOriginalTS = new ArrayList<ArrayList<Integer>>();
		  this.rawRoutes = new ArrayList<Route>();
		  this.anomalyRoutes = new ArrayList<Route>();
		  this.lat = new ArrayList<Double>();
		  motifs=new ArrayList<ArrayList<Route>>();
		  this.lon = new ArrayList<Double>();
		  Comparator<String> expandedRuleComparator = new Comparator<String>(){
			  @Override public int compare(String r1, String r2)
			  {
				  Integer iteration1 = 0;
				  Integer iteration2 = 0;
				  Integer rule1 = 0;
				  Integer rule2 = 0;
				  if (r1.charAt(0)=='I' )
					{
						if(r1.contains("r")){
							int rIndex = r1.indexOf("r");
							iteration1 = Integer.valueOf(r1.substring(1, rIndex));
							rule1 = Integer.valueOf(r1.substring(rIndex+1));
						//	System.out.println("r1: "+r1+" iteration: "+iteration1+" rule1: "+rule1);
							
					//		System.out.println(s+" = "+subRule );
						}
						else 
							throw new IllegalArgumentException(r1+" is not comparable with "+ r2);
					}
				  else
					 throw new IllegalArgumentException(r1+" is not comparable with "+ r2);
				  if (r2.charAt(0)=='I' )
					{
						if(r2.contains("r")){
							int rIndex = r2.indexOf("r");
							iteration2 = Integer.valueOf(r2.substring(1, rIndex));
							rule2 = Integer.valueOf(r2.substring(rIndex+1));
						//	System.out.println("r2: "+r2+" iteration2: "+iteration2+" rule2: "+rule2);
							
					//		System.out.println(s+" = "+subRule );
							
						}
						else 
							throw new IllegalArgumentException(r1+" is not comparable with "+ r2);
					}
				  else
					  new IllegalArgumentException(r1+" is not comparable with "+ r2);
			
				  
			
				  if(allRules.get(iteration2).get(rule2).getActualRuleYield() > allRules.get(iteration1).get(rule1).getActualRuleYield())
						  return 1;
				 
				  else 
					  return -1;
				   
			  }
		  };

		  SequiturModel.sortedRuleMap = new TreeMap<String, GrammarRuleRecord>(expandedRuleComparator);
		  
		  hasNewCluster = true;
		  StringBuffer sb = new StringBuffer();
		  if (null == this.latOri ||null == this.lonOri|| this.latOri.size()==0 || this.lonOri.size()==0 ){
			  this.log("unable to \"Process data\" - no data were loaded...");
		  }
		  else{
			  
			  consoleLogger.info("setting up GI with params: ");
			  sb.append(" algorithm: Sequitur");
			  sb.append(" MinLink: ").append(minLink);
			  sb.append(" Alphabet size: ").append(alphabetSize);
			  sb.append(" Minimal Continuous Blocks: ").append(minBlocks);
			  sb.append(" Noise Cancellation Threshold: ").append(noiseThreshold);
			  consoleLogger.info(sb.toString());
			 
			 
			  this.log(sb.toString());
		  }
		  rawAllIntervals = new ArrayList<RuleInterval>();
		  long beginTime = System.currentTimeMillis();
			 // beginTime  =  System.nanoTime();
			  System.out.println("begin time: "+beginTime);
		  buildModel();
		 
		  /*
		  for (int i = 0; i<rawAllIntervals.size();i++)
			  System.out.println("Trajectory "+i+":" + rawAllIntervals.get(i));
			  */
		  drawRawTrajectories();

		  
		  
          allMapToPreviousR0.add(mapToPreviousR0);


		
		  
          /*
           * 
           *    replace rules with rules' ids and clusters' ids
           * 
           */
		  
		  
          Integer iteration = 0;
 		 try {
			runSequitur(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        /*  System.out.println("before:");
          for (int d = 0; d<words.size(); d++)
        	  System.out.print(words.get(d)+ " ");
          System.out.println();
          */
          
          /*
           * run the algorithm
           */
         
/*          while(hasNewCluster){
	  		  int lastIteration = iteration;
	  		  hasNewCluster = false;
        	  
        	  System.out.println("Iteration: "+iteration);*/
        	  
        //  if(hasNewCluster)
        	  /*
        	  try {
				runSequitur(iteration);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
        	  iteration = iteration + 1;
        	 // this.minLink = this.minLink*2;
        	  //this.minLink = minLink*(iteration+1);
        	  
	          //new added part for testing yifeng's input
//	          HashMap<String, ArrayList<RuleInterval>> newRule = new HashMap<String, ArrayList<RuleInterval>> ();
//	          ArrayList<RuleInterval> arrRule = new ArrayList<RuleInterval>();
//	          arrRule.add(new RuleInterval(12241, 12574));
//	          arrRule.add(new RuleInterval(12576, 12909));
//	          arrRule.add(new RuleInterval(13399, 13731));
//	          newRule.put("R0",arrRule);		     
//	          rules = convert(newRule);
        	  
        	   drawOnMap();
           	System.out.println("total anomalies: "+anomalyRoutes.size());

      //}
        //  drawOnMap();
         //	System.out.println("total anomalies: "+anomalyRoutes.size());
	  
	  //end while
		  
         
		  System.out.println("Sorted Map.size = "+ sortedRuleMap.size()+ "sortedCounter = "+sortedCounter);
		  /*
		  for (int i = 0 ; i<r0.length;i++)
			  System.out.println(i+ " : "+r0[i]);
		  while(sortedRuleMap.size()>0)
		  {
			 
			  Entry<String, GrammarRuleRecord> entry = sortedRuleMap.pollFirstEntry();
		//	  System.out.println(entry.getKey()+" : "+entry.getValue());
		  }
		  */
	//	  AnomalyDetection();
  
		  System.out.println(Collections.max(lat));
		  System.out.println(Collections.min(lat)); 
		  
		  

		  System.out.println(Collections.max(lon));
		  System.out.println(Collections.min(lon)); 
		 
		  this.log("processed data, painting on map");
		  consoleLogger.info("process finished");
		  setChanged();

		
		 


		  //test
		/*  blocks.printBlockMap();
		  for (int i = 0; i<words.size(); i++)
		  {
			  System.out.print("  "+words.get(i));
		  }
		  */
		  /*r
		  System.out.println("trackMap:");
		  System.out.println(trackMap.toString());
		 // System.out.println(map2String(trackMap));
		  System.out.println("Postions:\t"+getTrimedPositions(trimedTrack).toString()+"\t");
		  System.out.println("TrimedStrs:\t"+getTrimedIds(trimedTrack));
		  */
		

		  System.out.println("running time: "+runTime);
		  ArrayList<Integer> frequency = new ArrayList<Integer>();
		  	

		  /*
		  for (int i=0;i<ruleIntervals.size();i++)
			  frequency.add(ruleIntervals.get(i).size());
			  */
		  notifyObservers(new SequiturMessage(SequiturMessage.CHART_MESSAGE, this.chartData, ruleIntervals, ruleMapLength));///, mapToOriginRules));//, frequency ));
  		  
	  //evaluateResult();
	  }
	  
	  private void buildModel() {
		 routes = new ArrayList< ArrayList<Route>>();
		//  paaLat = new ArrayList<Double>();
		//  paaLon = new ArrayList<Double>();
		//  ArrayList<Double> latBuffer=new ArrayList<Double>();
		//  ArrayList<Double> lonBuffer=new ArrayList<Double>();
		  double avgLat;
		  double avgLon;
		  /*
		   * use the centroid(paaLat,paaLon) to represent the data
		   */
		//  System.out.println("paaSize: "+paaSize);
		 /*
		  if(paaSize==1)
		  {
			  paaLat = lat;
			  paaLon = lon;
			  
		  }
		  else
		  */
		   // System.out.println("Should not see this msg.");
		  
		  
		  
		  
		  

	//	  System.out.println("oriLon" + lon);
		 
		  //blocks = new Blocks(alphabetSize,latMin,latMax,lonMin,lonMax);
		  blocks = new AdaptiveBlocks(alphabetSize, latOri, lonOri);
		  //double latCut = blocks.latCut;
		  //double lonCut = blocks.lonCut;
		  ncLat = new ArrayList<Double>();
		  ncLon = new ArrayList<Double>();
		  //resample(latCut,lonCut);
		  adaptiveResample(blocks);
		//  lat = latOri;
		 // lon = lonOri;
		  /*System.out.println("--------");
		  System.out.println(blocks.findBlockIdForPoint(new Location(blocks.latMin,blocks.lonMax)));
		  System.out.println(blocks.lonSpan(-122.3862, -122.37538));
		  System.out.println(blocks.latMin);
		  double[] latCut = blocks.latCutPoints;
		  for(int i=0; i<latCut.length; i++) {
			  System.out.println(latCut[i]);
		  }
		  System.out.println(blocks.latMax);*/
		  
		  isCovered= new boolean[lat.size()];
		 
		  ruleCovered = new boolean[lat.size()];
		  for(int i=0;i<lat.size();i++){
			  isCovered[i] = true;
			 // System.out.println(lat.get(i)+" , "+lon.get(i));
			
			  
		  }
		  words = new ArrayList<String>();
		  // add all points into blocks.
		  Integer previousId=(Integer)(-1);
		
		//  HashMap<Integer,Integer> trackMap = new HashMap<Integer, Integer>();
		  trimedTrack = new ArrayList<NumerosityReductionMapEntry>();
		  mapTrimed2Original = new ArrayList<Integer>();  // The index is the position in trimmed array, and the content is position in original time series.
		  mapToOriginalTS = new ArrayList<Integer>();
		  int startPoint = 0;
		  int endPoint = 0;
		  for (int i = 0; i<ncLat.size();i++){
			  
			  	Location loc= new Location(ncLat.get(i),ncLon.get(i));
			  	/*
			if(ncLat.get(i)>AdaptiveBlocks.clatu || ncLat.get(i)<AdaptiveBlocks.clatl || ncLon.get(i)<AdaptiveBlocks.clonu || ncLon.get(i)>AdaptiveBlocks.clonl)
				  {
				  	continue;
				  }
			*/
			//  blocks.addPoint2Block(loc); this should not work here because the point will change if it is a noisy point.
			  Integer id = new Integer(blocks.findBlockIdForPoint(loc));
			  
			  if(isNoise(id,i,noiseThreshold)){
				 // lat.set(i, lat.get(i-1));
				  ncLat.set(i, ncLat.get(i-1));
				 // lon.set(i, lon.get(i-1));
				  ncLon.set(i, ncLon.get(i-1));
				  id = previousId;
			  }
			  
			  if(id<-1000){
				  endPoint = i-1;
				  rawAllIntervals.add(new RuleInterval(startPoint, endPoint));
				  startPoint = i+1;
			  }
		
			 
			words.add(id.toString());
			
		//	  System.out.println("previousId, id:  "+previousId+",   "+id+"        i:   "+i+"   Lat,Lon: "+paaLat.get(i)+","+paaLon.get(i));
			  Integer trimedIndex = 0;
			  if (!id.equals(previousId))
			  {
				  
				  NumerosityReductionMapEntry<Integer, String> entry = new NumerosityReductionMapEntry<Integer, String>(new Integer(i),id.toString());
		//		  System.out.println("entry: "+i+","+id);
				  trimedTrack.add(entry);
				  mapTrimed2Original.add(i);
				  mapToOriginalTS.add(i);
				  //put the new <index,id> pair into a map 
				  //NumerosityReductionMapEntry entry = new NumerosityReductionMapEntry(i,id);
			//	  trackMap.put(i, id);
				  previousId = id;
			  }
			  
			  				  
		  }
		  System.out.print("mapTrimed2Original: ");
	//	  printArrayList(mapTrimed2Original);		  
		  /*
		   * Following is put the cleaned location data into block again
		   */
		  /*
		  for(int i=0; i<20;i++)
		  System.out.println("Orignal String: " + words.get(i));
		  */
		 // System.out.println("StringTrimedTrack:  "+trimedTrack);
	/*
		  for(int i=0; i<trimedTrack.size();i++){
			  System.out.println(i+" : "+trimedTrack.get(i).getValue()+" ");
		  }
		  */
		  System.out.println();
		  
		 		
	}

	public static ArrayList<Double> getLonOri() {
		return lonOri;
	}

	public ArrayList<Double> getLon() {
		return lon;
	}

	public static ArrayList<Double> getLatOri() {
		return latOri;
	}

	/*
	 * resample latitude and longitude when two points skip blocks.
	 */
	private void resample(double latCut,double lonCut) {
		int i = 1;
		double latPre = latOri.get(0);
		double lonPre = lonOri.get(0);
		lat.add(latPre);
		lon.add(lonPre);
		ncLat.add(latPre);
		ncLon.add(lonPre);
		boolean firstPoint = true;
		while(i<latOri.size())
		{
			if(status.get(i)==1000)
				breakPoint = lat.size();
			
			
			if(latOri.get(i)<-180)
				{
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					firstPoint = true;
				}
			else{
				if(firstPoint){
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					firstPoint = false;
				}
				else{
				double latStep = Math.abs(latOri.get(i)-latOri.get(i-1));
				double lonStep = Math.abs(lonOri.get(i)-lonOri.get(i-1));
				
				if(latStep>latCut||lonStep>lonCut){
					int skip = Math.max((int)Math.round(latStep/latCut),(int)Math.round(lonStep/lonCut));
					double latstep = (latOri.get(i)-latOri.get(i-1))/skip;
					double lonstep = (lonOri.get(i)-lonOri.get(i-1))/skip;
					for (int j = 0; j<skip; j++){
						lat.add((latOri.get(i-1)+latstep*(j+1)));
						lon.add((lonOri.get(i-1)+lonstep*(j+1)));
						ncLat.add((latOri.get(i-1)+latstep*(j+1)));
						ncLon.add((lonOri.get(i-1)+lonstep*(j+1)));
						//  System.out.println(lat.get(i+j)+" , "+lon.get(i+j));

					}
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
				}
				else
					{
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					}
				}
			}
			
		}
		
	}
	
	private void adaptiveResample(AdaptiveBlocks blocks) {
		int i = 1;
		double latPre = latOri.get(0);
		double lonPre = lonOri.get(0);
		lat.add(latPre);
		lon.add(lonPre);
		ncLat.add(latPre);
		ncLon.add(lonPre);
		boolean firstPoint = true;
		while(i<latOri.size())
		{
			
			if(status.get(i)==1000)
				breakPoint = lat.size();
			
			
			if(latOri.get(i)<-180)
				{
					
				lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					firstPoint = true;

				}
			else{
				if(firstPoint){
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					firstPoint = false;
				}
				else{
				int latSpan = blocks.latSpan(latOri.get(i), latOri.get(i-1));
				int lonSpan = blocks.lonSpan(lonOri.get(i), lonOri.get(i-1));
				
				if(latSpan>1||lonSpan>1){
					int skip = Math.max(latSpan,lonSpan);
					double latstep = (latOri.get(i)-latOri.get(i-1))/skip;
					double lonstep = (lonOri.get(i)-lonOri.get(i-1))/skip;
					for (int j = 0; j<skip; j++){
						lat.add((latOri.get(i-1)+latstep*(j+1)));
						lon.add((lonOri.get(i-1)+lonstep*(j+1)));
						ncLat.add((latOri.get(i-1)+latstep*(j+1)));
						ncLon.add((lonOri.get(i-1)+lonstep*(j+1)));
						//  System.out.println(lat.get(i+j)+" , "+lon.get(i+j));

					}
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
				}
				else
					{
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					}
				}
			}
			
		}
		
	}

	private void drawRawTrajectories() {
		
		
		
	  			
	  		for (int k=0;k<rawAllIntervals.size();k++)
	  				  {
	  					  Route singleRoute = new Route();
	  					  int startPos = rawAllIntervals.get(k).getStartPos();
	  						int endPos = rawAllIntervals.get(k).getEndPos();
	  						/*
	  						for(int index=startPos; index<=endPos;index++)
	  							isCovered[index]=true;
	  							*/
	  		//				System.out.println("startPos: "+startPos);
	  		//				System.out.println("endPos: " +endPos);
	  						
	  					//	System.out.print("track#: "+counter+":       ");
	  						for (int j = startPos; j<=endPos; j++){
	  							
	  							Location loca = new Location(lat.get(j),lon.get(j));
	  				
	  							singleRoute.addLocation(lat.get(j), lon.get(j));
	  								
	  						
	  							
	  						}
	  						rawRoutes.add(singleRoute);
	  						
	  				  }
	  		//		  System.out.println("position size: "+positions.size());
	  			//	  System.out.println("route size: "+route.size());
	  				
	  				  //  if(route.size()>2)
	
	  		  	
	}


	
	 
	  
	  /*
		   * Generate All Motifs and record them on files respectively.
		   */
	/*
		private void drawOnMap() {
			 // Generate All Motifs and record them on files respectively.
			 // String header = "type,latitude,longitude";
	//		  System.out.println("Total rules:"+chartData.getRulesNumber());
			  
			//  ArrayList<SAXMotif> allMotifs = chartData.getAllMotifs();
			//  for (int i=1; i<chartData.getRulesNumber();i++){
			    // create merged rule interval data structure corresponding to "clusters" 
			    //ruleIntervals = new ArrayList<ArrayList<RuleInterval>>();
		//        mapToOriginRules = new ArrayList<HashSet<Integer>>();
		        //jjj
			    int totalRuleCount = 0;
			    immergableRuleCount = 0;
			    
			    for(int i=0;i<filter.size();i++){
			    	// getRulePositions() was modified to show the intervals only occurred in R0
			  //  	if(!clusterMap.containsKey(i)&&chartData.getRulePositionsByRuleNum(filter.get(i)).size()>=minBlocks) 
			    		{
			    		    ArrayList<RuleInterval> ri = chartData.getRulePositionsByRuleNum(filter.get(i));
			    		  
			    			{ruleIntervals.add(chartData.getRulePositionsByRuleNum(filter.get(i)));
			    			HashSet<Integer> set = new HashSet<Integer>();
			    			set.add(filter.get(i));
			    			mapToOriginRules.add(set);
			    			totalRuleCount++;
			    			immergableRuleCount++;
			    			}
			    			  if(ri.size()<=2)
			    		    	System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+ri);
			    		}
			    }
			    
			    	
			    for(int i = 0; i< clusters.size();i++){
			    	
			    	ArrayList<RuleInterval> mergedIntervals = new ArrayList<RuleInterval>();
			    	HashSet<Integer> set = new HashSet<Integer>();
			    	if(clusters.get(i).size()>0)
			    	{
			    		//System.out.println("cluster "+i+" : {" +clusters.get(i)+"}");
			    		totalRuleCount = totalRuleCount+clusters.get(i).size();
			    	
			    	for(int r : clusters.get(i)){
			    	
			    		int rule = r; //filter.get(r);
			    		set.add(rule);
			    		if(mergedIntervals.size()==0)
			    			mergedIntervals.addAll(chartData.getRulePositionsByRuleNum(rule));
			    		else{
			    			ArrayList<RuleInterval> newIntervals = chartData.getRulePositionsByRuleNum(rule);
			    			for(int j = 0; j<newIntervals.size();j++){
			    				RuleInterval newComer = newIntervals.get(j);
			    				boolean hasMerged = false;
			    				for(int k = 0; k<mergedIntervals.size();k++){
			    					if(RuleInterval.isMergable(mergedIntervals.get(k),newComer)){
			    						System.out.println("I still need merge here.");
			    						System.out.println("1:" +mergedIntervals.get(k) +" 2:"+ newComer );
			    							
			    						RuleInterval newInterval = RuleInterval.merge(mergedIntervals.get(k), newComer);
			    						mergedIntervals.set(k, newInterval);
			    						hasMerged = true;
			    						//mergedIntervals.remove(k);
			    						//mergedIntervals.add(newInterval);
			    						//break;
			    						
			    					}
			    					
			    						
			    				}
			    				if(!hasMerged)
			    					mergedIntervals.add(newComer);
			    				
			    			}
			    		}
			    		}
			    	if(mergedIntervals.size()>2)
			    	{	
			    	ruleIntervals.add(mergedIntervals);
			    	mapToOriginRules.add(set);
			    	}
			    	else
			    		System.out.println("mergedIntervals.size = "+mergedIntervals.size());
			    	}
			    }
			    System.out.println("Immergable Rule  = "+ immergableRuleCount);
			    System.out.println("Total Rule Count = "+totalRuleCount);
			    boolean[] isCovered = new boolean[lat.size()];
			    coverCount = 0;
			    for (int i = 0; i<isCovered.length;i++)
			    	isCovered[i] = false;
			    totalSubTrajectory = 0;
				for (int i = 0; i<ruleIntervals.size();i++){
			  	totalSubTrajectory = totalSubTrajectory + ruleIntervals.get(i).size();
			  	
				  {//(countSpaces(chartData.getRule(i).getExpandedRuleString())>minBlocks){
				  ArrayList<RuleInterval> positions = ruleIntervals.get(i);//chartData.getRulePositionsByRuleNum(filteredRuleMap.get(i));
				  
				//  ArrayList<RuleInterval> positions = chartData.getRulePositionsByRuleNum(i);	  
			//	  System.out.println("rule" + i+" :  "+ positions);//.get(0).toString());
				  
				  
				  if(true)//(positions.size()>2)
					  //&&chartData.getRule(i).getMeanLength()>1)
				  {
					  
					  
						//  File fname = new File("./rules/motif_"+i+".csv");
						//  FileWriter motifPos = new FileWriter(fname);
					//Generating evaluation file
						  
						  int counter = 0;
						  ArrayList<Route> route = new ArrayList<Route>();
						 // Integer route0Id =-1;
						 // Integer route1Id =-1;
					  for (int k=0;k<positions.size();k++)
					  {
						  Route singleRoute = new Route();
				//		  motifPos.append(header+"\n");
						  int startPos = positions.get(k).getStartPos();
							int endPos = positions.get(k).getEndPos();
							
							for(int index=startPos; index<=endPos;index++)
								isCovered[index]=true;
			//				System.out.println("startPos: "+startPos);
			//				System.out.println("endPos: " +endPos);
							boolean firstPoint = true;
							
						//	System.out.print("track#: "+counter+":       ");
							for (int j = startPos; j<=endPos; j++){
								
					//			motifPos.append("T,"+lat.get(j)+","+lon.get(j));
							//	if(counter<2){
								Location loca = new Location(lat.get(j),lon.get(j));
							//	  blocks.addPoint2Block(loc);
						//		  Integer idss = new Integer(blocks.findBlockIdForPoint(loca));
						//		  System.out.print(idss+", ");
								singleRoute.addLocation(lat.get(j), lon.get(j));
									
							//	}
								if(firstPoint)
								{
						//			motifPos.append(",Track "+counter+",red\n");
									firstPoint = false;
									
								}
						//		else motifPos.append("\n");
								
							}
							route.add(singleRoute);
							
							counter++;
						//	System.out.println();
					  }
			//		  System.out.println("position size: "+positions.size());
				//	  System.out.println("route size: "+route.size());
					
					  //  if(route.size()>2)
					     routes.add(route);
					    
					    
					    
					    
					  //	motifPos.flush();
					  //	motifPos.close();
					  //	System.out.println(fname.getName());
				//	  	route.get(0).print();
					  	
					 
				  }
				  
				//  System.out.println("motif index: "+motif.getRuleIndex()+"   " +motif.toString());
				  //FileWriter motifPos = new FileWriter(new File("./motif_"+motif.getRuleIndex()+".csv"));
				 
			  	}
			  }	
				for (int i = 0;i<isCovered.length;i++){
					  if(isCovered[i]==true)
						  coverCount++;
				  }
				  System.out.println("Cover Count: "+ coverCount);
				  System.out.println("cover rate: " +(double)coverCount/isCovered.length);
			 
		}
	*/
	
	private void clusterRules() {
	      /*
       * Postprocessing merge, connect
       */
		
		/*
		 * Warning: rules in clusters are real rules, rules in clusterMap are filter rules. 
		 * 
		 */
      filterMap = new HashMap<Integer,Integer>();
      for (int i = 0; i<rules.size();i++){
				if ((rules.get(i).frequencyInR0()>=1&&countSpaces(RuleDistanceMatrix.parseRule(rules.get(i).getExpandedRuleString()))>=2))//||
					//	(originalRules.get(i).frequencyInR0()>1&&originalRules.get(i).getR0Intervals().size()>2&&originalRules.get(i).getRuleYield()>=minBlocks))
					{
					//HashSet<Integer> set = new HashSet<Integer>();
	//				System.out.println("Yield: "+rules.get(i).getRuleYield()+" string: "+rules.get(i).getExpandedRuleString());
					filterMap.put(i, filter.size());
					filter.add(i);
				/*	
					if(rules.get(i).getR0Intervals().size()<2)
						System.out.println("Bug!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+i);
					*/
					}
				
			}
      System.out.println("filter Size = "+filter.size());
      allFilters.add(filter);
      if(filter.size()>1){
      //HashMap<Integer,ArrayList<Integer>> mergeRecord = new HashMap<Integer, ArrayList<Integer>>();
      long t1s = System.currentTimeMillis();
      RuleDistanceMatrix rdm;
      System.out.println("AlphabetSize="+this.alphabetSize);
      rdm = new RuleDistanceMatrix(blocks,rules, filter,minBlocks, minLink); 
      long t1e = System.currentTimeMillis();
      long buildMatrixTime = t1e-t1s;
      
//      clusters = new ArrayList<HashSet<Integer>>(); 
     /*
      for(int i = 0; i<rdm.filter.size();i++){
    	 families.add(new HashSet<Integer>());
    	 
    	 
    	 families.get(i).add(i);
    	//  mergeRecord.put(i, family.add(i) );
      }
     */
      long t2s =System.currentTimeMillis();
      NumberFormat formatter = new DecimalFormat("#0.00");
      System.out.println("rdm.pq.size(): "+rdm.pq.size());
      int mergableCount = 0;
      while(rdm.pq.size()>0){
    	  PairDistance pair = rdm.pq.remove();
    	  int lineSize;
    	  int colSize;
    	  int totalSize;
    	  if(isMergable(rdm.matrix,clusters,pair.getLine(),pair.getCol(),clusterMap, minLink)){
    		  hasNewCluster=true;
    		  mergableCount++;
    	//	  merge(rules,rdm.filter.get(pair.getLine()),rdm.filter.get(pair.getCol()));
    		  if(clusterMap.containsKey(pair.getLine())||clusterMap.containsKey(pair.getCol()))
    		  {
    			  if(!clusterMap.containsKey(pair.getLine())){
    				  clusters.get(clusterMap.get(pair.getCol())).add(filter.get(pair.getLine()));
    				  clusterMap.put(pair.getLine(), clusterMap.get(pair.getCol()));
    			//	  System.out.println("Adding Line  to a cluster, Line:"+pair.getLine()+" Colu:"+pair.getCol()+clusters.get(clusterMap.get(pair.getCol())));
    				  //System.out.println("Map:"+clusterMap);
    				  
    			  }
    			  else if(!clusterMap.containsKey(pair.getCol())){
    				  clusters.get(clusterMap.get(pair.getLine())).add(filter.get(pair.getCol()));
    				  clusterMap.put(pair.getCol(), clusterMap.get(pair.getLine()));
    			//	  System.out.println("Adding Colum to a cluster,Colum:"+pair.getCol()+" Colu:"+pair.getCol()+clusters.get(clusterMap.get(pair.getLine())));
    				  //System.out.println("Map:"+clusterMap);
    			  }
    			  else{
    				  if(!clusterMap.get(pair.getLine()).equals(clusterMap.get(pair.getCol())))
    				  {
    				//  System.out.println("Before Merge, line in cluster:"+clusterMap.get(pair.getLine())+clusters.get(clusterMap.get(pair.getLine()))+" colu in cluster:"+clusterMap.get(pair.getCol())+clusters.get(clusterMap.get(pair.getCol())));
    				  lineSize = clusters.get(clusterMap.get(pair.getLine())).size();
    				  colSize = clusters.get(clusterMap.get(pair.getCol())).size();
    				  clusters.get(clusterMap.get(pair.getLine())).addAll(clusters.get(clusterMap.get(pair.getCol())));
    				  int colCluster = clusterMap.get(pair.getCol());
    				  for(int v : clusters.get(clusterMap.get(pair.getCol())))
    					  {
    				//	  System.out.print("v: "+v+" ");
    					  clusterMap.put(filterMap.get(v), clusterMap.get(pair.getLine()));
    				//	  clusters.get(clusterMap.get(pair.getLine())).add(v);
    					  }
    				  //System.out.println();
    				  clusters.get(colCluster).clear();
    				 // System.out.println("After  Merge, Line:"+pair.getLine()+clusters.get(clusterMap.get(pair.getLine()))+" Colu:"+pair.getCol()+clusters.get(colCluster));
    				 // System.out.println("Map:"+clusterMap);
    				  totalSize = clusters.get(clusterMap.get(pair.getLine())).size();
    				  //if((lineSize+colSize)!=totalSize){
    					//  System.out.println("Error Candidate here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    				  //}
    				  }
    				  //else
    					// System.out.println("Same Cluster! "+clusterMap.get(pair.getLine())+","+clusterMap.get(pair.getCol()));
    			  }
    		  }
    		  else{
    			  HashSet<Integer> set = new HashSet<Integer>();
    			  set.add(filter.get(pair.getLine()));            
    			  set.add(filter.get(pair.getCol()));
    			  clusters.add(set);
    			  clusterMap.put(pair.getLine(), clusters.size()-1);
    			  clusterMap.put(pair.getCol(), clusters.size()-1);
    			 // System.out.println("Created a cluster: "+clusters.get(clusters.size()-1));
    		//	  System.out.println("Map:"+clusterMap);
    		  }
    		  
    		  /*
    		  clusters.get(pair.getLine()).addAll(clusters.get(pair.getCol()));
    		  clusters.get(pair.getCol()).addAll(clusters.get(pair.getLine()));
    		  
    		  for(int i: families.get(pair.getCol()))
    			  families.get(pair.getLine()).add(i);
    		  for(int i: families.get(pair.getLine()))
    			  families.get(pair.getCol()).add(i);
    			  */	        		
    	//	  System.out.print("Merged Pair: <"+pair.getLine()+", "+pair.getCol()+"> = "+rdm.matrix[pair.getLine()][pair.getCol()]);
    	//	  System.out.print(" all distances: ");
    		  /*
    		  for (int i : clusters.get(clusterMap.get(pair.getLine())))
  				for(int j : clusters.get(clusterMap.pair.getCol()))
  				{
  				
  				System.out.print(formatter.format(rdm.matrix[i][j])+", ");
  					
  				}*/
    	//	  System.out.println();
    	  }
      }
      
	  
      
      
      System.out.println("MergableCount: "+mergableCount);
      
      /*
      ArrayList<HashSet<Integer>> tempCluster = new ArrayList<HashSet<Integer>>();
      for(int i=0;i<clusters.size();i++)
      {
    	  if(clusters.get(i).size()>0)
    		  tempCluster.add(clusters.get(i));
      }
      clusters = tempCluster; // be aware!!!! hashMap did not update here, but who cares?
      */
      allClusters.add(clusters);
      long t2e = System.currentTimeMillis();
      long clusterTime = t2e -t2s;
  	System.out.println("build matrix: "+(double)(buildMatrixTime/1000.0));
		  System.out.println("Clustering Time: "+(clusterTime/1000.0));
      /*
      for(int i = 0; i<clusters.size();i++){
    	  System.out.println("i = "+i+" : "+clusters.get(i));
      }
      */
     
		  System.out.println("cluster map size = "+ clusterMap.size());
		    System.out.println("clusterMap:   "+clusterMap);
      }
      
	}
/*
	private void AnomalyDetection() {
		ArrayList<RuleInterval> anomalyCandidate = new ArrayList<RuleInterval>();
		int start = 0;
		int end = 0;
		for(int i = 0; i<r0.length; i++){
			
			if(!isNumeric(r0[i])){
				end = getPositionInOriginalTrimedString(i)-1;
				if(end>0) 
					{
						RuleInterval ruleInterval = new RuleInterval(start,end);
						anomalyCandidate.add(ruleInterval);
						System.out.println("r0[i]: "+r0[i]+ruleInterval);//anomalyCandidate.get(anomalyCandidate.size()-1));
					}
				start = end + 2;	
			}
			else if(Integer.valueOf(r0[i])<0){
				end = getPositionInOriginalTrimedString(i)-1;
				if(end>0) 
					{
						RuleInterval ruleInterval = new RuleInterval(start,end);
						anomalyCandidate.add(ruleInterval);
						System.out.println("r0[i]: "+r0[i]+ruleInterval);//anomalyCandidate.get(anomalyCandidate.size()-1));
					}
				start = end + 2;
			}
		}
	}
	*/
	/*
	private int getPositionInOriginalTrimedString(int index) {
		int ans = -1;
		int idx = index;
		int iter = allMapToPreviousR0.size()-1;
		//int ans = -1;
		ArrayList<Integer> map = new ArrayList<Integer>();
		while(iter>= 0){
			map = allMapToPreviousR0.get(iter);
			idx = map.get(idx);
		}
		ans = 
		return ans;
	}

*/
	private void drawOnMap(){
			  // Generate All Motifs and record them on files respectively.
        //new added part for testing yifeng's input
//        HashMap<String, ArrayList<RuleInterval>> newRule = new HashMap<String, ArrayList<RuleInterval>> ();
//        ArrayList<RuleInterval> arrRule = new ArrayList<RuleInterval>();
//        arrRule.add(new RuleInterval(12241, 12574));
//        arrRule.add(new RuleInterval(12576, 12909));
//        arrRule.add(new RuleInterval(13399, 13731));
//        newRule.put("R0",arrRule);		     
//        rules = convert(newRule);
			
			boolean flag=true;
			if(flag)
			{
		    trueAnomalyCount = 0;
		    falsePositiveCount = 0;
		    trueNegativeCount = 0;
		    falseNegativeCount = 0;
			for(int i = 0; i<isCovered.length;i++)
				{
					isCovered[i] = true;
					ruleCovered[i] = false;
				}
		  	finalIntervals = new HashMap<String, ArrayList<RuleInterval>>();
			ruleIntervals = new ArrayList<ArrayList<RuleInterval>>();
			anomalyIntervals = new ArrayList<RuleInterval>();
			routes = new ArrayList<ArrayList<Route>>();
			ruleMapLength = new ArrayList<Integer>();
			immergableRuleCount = 0;
		    //for (int i = 0 ; i<r0.length; i++){
		  	
		  	//new added part for yifeng's algorithm
//	        ArrayList<RuleInterval> arrRule = new ArrayList<RuleInterval>();
//	        arrRule.add(new RuleInterval(12241, 12574));
//	        arrRule.add(new RuleInterval(12576, 12909));
//	        arrRule.add(new RuleInterval(13399, 13731));
//	        
//	        ArrayList<RuleInterval> arrRule2 = new ArrayList<RuleInterval>();
//	        arrRule2.add(new RuleInterval(14241, 14574));
//	        arrRule2.add(new RuleInterval(14576, 14909));
//	        arrRule2.add(new RuleInterval(15399, 15731));
//	        
//		  	ruleIntervals = new ArrayList<ArrayList<RuleInterval>>();
//		  	ruleIntervals.add(arrRule);
//		  	ruleIntervals.add(arrRule2);
	}
		  	//newRules is the global variable for the new rules.
		  	this.setNewRules(tmprule);
		  	convert(newRules);
		  	
		  	totalSubTrajectory = 0;
		  	//	evaluateResult();
				
		  }

	
	private int getNextNonTerminal(int i) {
		int j = i+1;
	//	System.out.println("j: "+j);
		while(j<r0.length&&isNumeric(r0[j])&&Integer.valueOf(r0[j])>=0)
		{
			j++;
			
		}
		
		return j;
	}

	private boolean isNumberAhead( int i) {
		
		for (int j = i; j<r0.length&&j<(i+minBlocks);j++)
			{
				if(!isNumeric(r0[j])||Integer.valueOf(r0[j])<0)
			
				return false;
			}
		
		System.out.print("r0_"+i+"_"+(i+minBlocks-1)+": [");
		for (int j = i; j<r0.length&&j<(i+minBlocks);j++)
		{
			System.out.print(r0[j]+" ");
		}
		System.out.println("]");
		
		return true;
	}

	private Integer getPositionsInTS(ArrayList<Integer> mapToPreviousR0,ArrayList<Integer> previousMapToOriginalTS, int index) {
		/*
		if(mapToPreviousR0.get(index)>76576)
				System.out.println("index = "+index +" mapToPreviousR0.get(index) ="+mapToPreviousR0.get(index)+"  previousMapToOriginalTS.get(mapToPreviousR0.get(index))  ="+previousMapToOriginalTS.get(mapToPreviousR0.get(index)));
	   */
		return previousMapToOriginalTS.get(mapToPreviousR0.get(index));
	}

	/*
	   * evaluation
	   */
	private void evaluateResult() {
		
	
	   
		  double[] evalResult = evaluateMotifs(routes);
		  double avgIntraDistance = evalResult[0];
		  double avgIntraDistanceStdDev = evalResult[1];
		  double minInterDistance = evalResult[2];
		  double avgSilhouetteCoefficient = evalResult[3];
		  
		  
	//	  eBlocks = new Blocks(EVAL_RESOLUTION,latMin, latMax, lonMin, lonMax);    // establish a grid map to evaluate the similarity
		  
	//	  String evalHead = "DataName,PaaSize,AlphabetSize,MinimalContinuousBlocks,NoiseCancellationThreshold\n";
		  
		  try{
		  File evalFile = new File("./evaluation/"+"evaluate_"+(int)(minLink*1000)+"_"+alphabetSize+"_"+minBlocks+"_"+noiseThreshold+"_"+lat.size()+"_"+fileNameOnly);
		  FileWriter fr = new FileWriter(evalFile);
		  String sb1; // = new StringBuffer();
		  //sb1.append(fileNameOnly+",");
		  sb1 = (fileNameOnly+","+minLink+","+alphabetSize+","+minBlocks+","+noiseThreshold+","+runTime+","+avgIntraDistance+","+avgIntraDistanceStdDev+","+ minInterDistance+","+avgSilhouetteCoefficient+","+routes.size()+","+lat.size()+','+totalSubTrajectory+","+coverCount+","+immergableRuleCount+"\n");
		  fr.append(sb1);
		  System.out.println(EVALUATION_HEAD);
		  System.out.println(sb1);
		 // .append("running time: "+runTime+"\n");
		  //fr.append(sb1);
		//  fr.append(Average distances amon)
		  this.log(EVALUATION_HEAD);
		  this.log(sb1.toString());
		  
		  fr.flush();
		  fr.close();
		  }
		  catch (IOException e){
			 
			  e.printStackTrace();
		  }
		  
		 		
	}

	

	
	
	
	

	  /*
	   * Generate All Motifs and record them on files respectively.
	   */
/*
	private void drawOnMap() {
		 // Generate All Motifs and record them on files respectively.
		 // String header = "type,latitude,longitude";
//		  System.out.println("Total rules:"+chartData.getRulesNumber());
		  
		//  ArrayList<SAXMotif> allMotifs = chartData.getAllMotifs();
		//  for (int i=1; i<chartData.getRulesNumber();i++){
		    // create merged rule interval data structure corresponding to "clusters" 
		    //ruleIntervals = new ArrayList<ArrayList<RuleInterval>>();
	//        mapToOriginRules = new ArrayList<HashSet<Integer>>();
	        
		    int totalRuleCount = 0;
		    immergableRuleCount = 0;
		    
		    for(int i=0;i<filter.size();i++){
		    	// getRulePositions() was modified to show the intervals only occurred in R0
		  //  	if(!clusterMap.containsKey(i)&&chartData.getRulePositionsByRuleNum(filter.get(i)).size()>=minBlocks) 
		    		{
		    		    ArrayList<RuleInterval> ri = chartData.getRulePositionsByRuleNum(filter.get(i));
		    		  
		    			{ruleIntervals.add(chartData.getRulePositionsByRuleNum(filter.get(i)));
		    			HashSet<Integer> set = new HashSet<Integer>();
		    			set.add(filter.get(i));
		    			mapToOriginRules.add(set);
		    			totalRuleCount++;
		    			immergableRuleCount++;
		    			}
		    			  if(ri.size()<=2)
		    		    	System.out.println("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+ri);
		    		}
		    }
		    
		    	
		    for(int i = 0; i< clusters.size();i++){
		    	
		    	ArrayList<RuleInterval> mergedIntervals = new ArrayList<RuleInterval>();
		    	HashSet<Integer> set = new HashSet<Integer>();
		    	if(clusters.get(i).size()>0)
		    	{
		    		//System.out.println("cluster "+i+" : {" +clusters.get(i)+"}");
		    		totalRuleCount = totalRuleCount+clusters.get(i).size();
		    	
		    	for(int r : clusters.get(i)){
		    	
		    		int rule = r; //filter.get(r);
		    		set.add(rule);
		    		if(mergedIntervals.size()==0)
		    			mergedIntervals.addAll(chartData.getRulePositionsByRuleNum(rule));
		    		else{
		    			ArrayList<RuleInterval> newIntervals = chartData.getRulePositionsByRuleNum(rule);
		    			for(int j = 0; j<newIntervals.size();j++){
		    				RuleInterval newComer = newIntervals.get(j);
		    				boolean hasMerged = false;
		    				for(int k = 0; k<mergedIntervals.size();k++){
		    					if(RuleInterval.isMergable(mergedIntervals.get(k),newComer)){
		    						System.out.println("I still need merge here.");
		    						System.out.println("1:" +mergedIntervals.get(k) +" 2:"+ newComer );
		    							
		    						RuleInterval newInterval = RuleInterval.merge(mergedIntervals.get(k), newComer);
		    						mergedIntervals.set(k, newInterval);
		    						hasMerged = true;
		    						//mergedIntervals.remove(k);
		    						//mergedIntervals.add(newInterval);
		    						//break;
		    						
		    					}
		    					
		    						
		    				}
		    				if(!hasMerged)
		    					mergedIntervals.add(newComer);
		    				
		    			}
		    		}
		    		}
		    	if(mergedIntervals.size()>2)
		    	{	
		    	ruleIntervals.add(mergedIntervals);
		    	mapToOriginRules.add(set);
		    	}
		    	else
		    		System.out.println("mergedIntervals.size = "+mergedIntervals.size());
		    	}
		    }
		    System.out.println("Immergable Rule  = "+ immergableRuleCount);
		    System.out.println("Total Rule Count = "+totalRuleCount);
		    boolean[] isCovered = new boolean[lat.size()];
		    coverCount = 0;
		    for (int i = 0; i<isCovered.length;i++)
		    	isCovered[i] = false;
		    totalSubTrajectory = 0;
			for (int i = 0; i<ruleIntervals.size();i++){
		  	totalSubTrajectory = totalSubTrajectory + ruleIntervals.get(i).size();
		  	
			  {//(countSpaces(chartData.getRule(i).getExpandedRuleString())>minBlocks){
			  ArrayList<RuleInterval> positions = ruleIntervals.get(i);//chartData.getRulePositionsByRuleNum(filteredRuleMap.get(i));
			  
			//  ArrayList<RuleInterval> positions = chartData.getRulePositionsByRuleNum(i);	  
		//	  System.out.println("rule" + i+" :  "+ positions);//.get(0).toString());
			  
			  
			  if(true)//(positions.size()>2)
				  //&&chartData.getRule(i).getMeanLength()>1)
			  {
				  
				  
					//  File fname = new File("./rules/motif_"+i+".csv");
					//  FileWriter motifPos = new FileWriter(fname);
				//Generating evaluation file
					  
					  int counter = 0;
					  ArrayList<Route> route = new ArrayList<Route>();
					 // Integer route0Id =-1;
					 // Integer route1Id =-1;
				  for (int k=0;k<positions.size();k++)
				  {
					  Route singleRoute = new Route();
			//		  motifPos.append(header+"\n");
					  int startPos = positions.get(k).getStartPos();
						int endPos = positions.get(k).getEndPos();
						
						for(int index=startPos; index<=endPos;index++)
							isCovered[index]=true;
		//				System.out.println("startPos: "+startPos);
		//				System.out.println("endPos: " +endPos);
						boolean firstPoint = true;
						
					//	System.out.print("track#: "+counter+":       ");
						for (int j = startPos; j<=endPos; j++){
							
				//			motifPos.append("T,"+lat.get(j)+","+lon.get(j));
						//	if(counter<2){
							Location loca = new Location(lat.get(j),lon.get(j));
						//	  blocks.addPoint2Block(loc);
					//		  Integer idss = new Integer(blocks.findBlockIdForPoint(loca));
					//		  System.out.print(idss+", ");
							singleRoute.addLocation(lat.get(j), lon.get(j));
								
						//	}
							if(firstPoint)
							{
					//			motifPos.append(",Track "+counter+",red\n");
								firstPoint = false;
								
							}
					//		else motifPos.append("\n");
							
						}
						route.add(singleRoute);
						
						counter++;
					//	System.out.println();
				  }
		//		  System.out.println("position size: "+positions.size());
			//	  System.out.println("route size: "+route.size());
				
				  //  if(route.size()>2)
				     routes.add(route);
				    
				    
				    
				    
				  //	motifPos.flush();
				  //	motifPos.close();
				  //	System.out.println(fname.getName());
			//	  	route.get(0).print();
				  	
				 
			  }
			  
			//  System.out.println("motif index: "+motif.getRuleIndex()+"   " +motif.toString());
			  //FileWriter motifPos = new FileWriter(new File("./motif_"+motif.getRuleIndex()+".csv"));
			 
		  	}
		  }	
			for (int i = 0;i<isCovered.length;i++){
				  if(isCovered[i]==true)
					  coverCount++;
			  }
			  System.out.println("Cover Count: "+ coverCount);
			  System.out.println("cover rate: " +(double)coverCount/isCovered.length);
		 
	}
*/

	public static void printArrayList(ArrayList<Integer> al) {
		if(al == null || al.size()==0)
			System.out.println("Null or empty ArrayList");
		else 
		{	
		//	System.out.print("[ ");
			for (int i = 0; i<al.size();i++)
				System.out.println(al.get(i)+" ");
			System.out.println();
		}
	}

	private boolean isMergable(double[][] distance, ArrayList<HashSet<Integer>> families, int x, int y, HashMap<Integer, Integer> map, double minLink) {
		//boolean mergable = true;
		if(map.containsKey(x)||map.containsKey(y)){
			if(!map.containsKey(x)){
				for(int j: families.get(map.get(y)))
				{
					int i = filterMap.get(j);
					if(distance[x][i]>(minLink*2))
						return false;
			
				}
			}
			else if(!map.containsKey(y)){
				for(int j: families.get( map.get(x)))
					{
					int i = filterMap.get(j);
					if(distance[i][y]>(minLink*2))
					
						return false;
			
					}
			}
			else
			{	
			for (int m : families.get(map.get(x)))
				for(int n : families.get(map.get(y)))

				{
				int i =filterMap.get(m);
				int j =filterMap.get(n);
		//		int xSibling = families.get(x).get(i);
		//		int ySibling = families.get(y).get(j);
				if(distance[i][j]>(minLink*2))
					return false;
				}
			}
		}
		else if(distance[x][y]>(minLink*2))
			return false;
		
		return true;
	}

	/*
	   * evaluate the distances intr
	   */
	  private double[] evaluateMotifs(ArrayList<ArrayList<Route>> routes) {
		double[] result = new double[4];  
		StringBuffer sb = new StringBuffer();
		eBlocks = new Blocks(EVAL_RESOLUTION,latMin, latMax, lonMin, lonMax);    // establish a grid map to evaluate the similarity
		ArrayList<Double> allDistances = new ArrayList<Double>();     // The ArrayList of Average Distances of each motif
		ArrayList<Double> allStdDev = new ArrayList<Double>();
		ArrayList<Double> allMinimalInterDistances = new ArrayList<Double>();
		ArrayList<ArrayList<ArrayList<Integer>>> allRules = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for (int i=0;i<routes.size();i++){   // iterate all motifs
			ArrayList<ArrayList<Integer>> allTracks = new ArrayList<ArrayList<Integer>>();
			for(int j = 0; j<routes.get(i).size();j++){			// iterate all tracks under each motif
				ArrayList<Integer> trackIds = new ArrayList<Integer>();
				Route tracks = routes.get(i).get(j);
				Integer previousId = (Integer)(-1);
				for(int k = 0;k<tracks.getLats().size();k++){
					Location loc = new Location(tracks.getLats().get(k),tracks.getLons().get(k));
					Integer id = new Integer(eBlocks.findBlockIdForPoint(loc));
					if(!id.equals(previousId)){
						trackIds.add(id);
						previousId = id;
					}
				}
				allTracks.add(trackIds);
			}
			allRules.add(allTracks);   // after the whole loop all trajectory should be represented in seq of Ids
			ArrayList<Double> pairwiseDistances = getSimilarities(allTracks);
			double sums = 0;
			for (int x = 0; x<pairwiseDistances.size(); x++)
				{
					sums = sums+pairwiseDistances.get(x);
		//			System.out.print(pairwiseDistances.get(x)+", ");
				}
		//	System.out.println();
		//	System.out.println("sum of pairwise distance: "+sums);
		//	System.out.println("pairSize = "+pairwiseDistances.size());
			double avgDistance = avg(pairwiseDistances);
			allDistances.add(avgDistance);
			
			Double stdDev = (Double)dev(pairwiseDistances);
			
			allStdDev.add(stdDev);
		/*	System.out.println("pairwire distances of motif "+i+": mean = "+avgDistance+",  Std.Dev ="+stdDev);
			for (int m = 0; m<pairwiseDistances.size();m++)
				System.out.print(" "+pairwiseDistances.get(m));
			System.out.println();
			*/
			
		}
		
		// evaluate distances inter-rules
		for(int i = 0; i<allRules.size();i++){
			ArrayList<Double> pairwiseInterDistances = new ArrayList<Double>();
			for(int j=0; j<allRules.size();j++){
				if(i!=j)
				 pairwiseInterDistances.add(avg(getSimilaritiesInterRules(allRules.get(i),allRules.get(j))));
			
			}
			if(pairwiseInterDistances.size()>0)
				allMinimalInterDistances.add(min(pairwiseInterDistances));

		}
		
//		System.out.println("average distances among all motifs: "+avg(allDistances));
	//	System.out.println("average standard deviation among all motifs: "+avg(allStdDev));
		//sb.append(avg(allDistances)+","+avg(allStdDev)+"\n");
		
		//return sb.toString();
		result[0] = avg(allDistances);  // avg intra distances
		result[1] = avg(allStdDev);  // avg std. dev. intra distances
		result[2] = avg(allMinimalInterDistances);  // avg minimal inter distances
		ArrayList<Double> silhouetteCoefficients = new ArrayList<Double>();
		for (int i = 0; i<routes.size(); i++)
		{
			
			double sc;
			
			if(allMinimalInterDistances.size()>0&&allDistances.get(i)<allMinimalInterDistances.get(i)){
		//		if(allDistances.get(i)<allMinimalInterDistances.get(i)){

				sc = 1 - allDistances.get(i)/allMinimalInterDistances.get(i);
			}
			else{
				if(allDistances.get(i)==0||allMinimalInterDistances.size()==0)
					sc = 1;
				else
				sc = allMinimalInterDistances.get(i)/allDistances.get(i) - 1;
				
			}
	//		System.out.println("compare: "+allDistances.get(i)+"/"+allMinimalInterDistances.get(i)+" = "+sc);
			silhouetteCoefficients.add(sc);
		}
		result[3] = avg(silhouetteCoefficients);
		return result;
	  }

	private double min(ArrayList<Double> list) throws NullPointerException {
		double min;// = -1000000000;
		if(list == null||list.size()==0)
			throw new NullPointerException();
		
			else
			{
				min = list.get(0);
				for(int i = 1; i<list.size();i++)
					if(min>list.get(i))
						min = list.get(i);
				
			}
		return min;
	}

	private ArrayList<Double> getSimilaritiesInterRules(
			ArrayList<ArrayList<Integer>> rule1,
			ArrayList<ArrayList<Integer>> rule2) {
		ArrayList<Double> pairwiseInterDistances = new ArrayList<Double>(); 
		for(int i = 0; i<rule1.size(); i++)
			for (int j=0;j<rule2.size();j++){
				pairwiseInterDistances.add(avgDTWDistance(eBlocks, rule1.get(i),rule2.get(j)));
			}
		
		return pairwiseInterDistances;
	}

	private double dev(ArrayList<Double> pairwiseDistances) {
		double avg = avg(pairwiseDistances);
		double sum = 0;
		for (int i=0; i<pairwiseDistances.size(); i++)
			sum = sum + (pairwiseDistances.get(i)-avg)*(pairwiseDistances.get(i)-avg);
		return Math.sqrt(sum);
	}

	private ArrayList<Double> getSimilarities(ArrayList<ArrayList<Integer>> allTracks) {
		ArrayList<Double> pairwiseDistance = new ArrayList<Double>();
		for(int i = 0; i<allTracks.size();i++){
			for(int j=i+1;j<allTracks.size();j++){
			//	System.out.println("i="+i+" j="+j);
				double similarity = avgDTWDistance(eBlocks, allTracks.get(i),allTracks.get(j));
				pairwiseDistance.add(similarity);
			}
		}
		return pairwiseDistance;
	}

	private double avgDTWDistance(Blocks blocks, ArrayList<Integer> s,
			ArrayList<Integer> t) {
		
	//	System.out.print("s::::::::::size:"+s.size());
		/*
		for(int i=0; i<s.size();i++)
			System.out.print(" "+s.get(i));
			*/
	//	System.out.println();
	//	System.out.print("t::::::::::size:"+t.size()+"   ");
		/*
		for(int i=0; i<t.size();i++)
			System.out.print(" "+t.get(i));
			*/
	//	System.out.println();
		int n = s.size();
		int m = t.size();
		double[][] DTW = new double[n+1][m+1];
		double cost = 0;
		for(int i=0;i<n;i++)
			DTW[i+1][0]=Double.MAX_VALUE;
		for(int i=0;i<m;i++)
			DTW[0][i+1] = Double.MAX_VALUE;
		DTW[0][0] = 0;
		for (int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				cost = blocks.distance(s.get(i),t.get(j));
			//	System.out.println("cost_"+i+","+j+": "+cost);
				DTW[i+1][j+1]=cost+minimum(DTW[i][j+1],		// insertion
										   DTW[i+1][j], 	// deletion
										   DTW[i][j]);	// match
			}
		}
	//	System.out.println("DTW:::::"+DTW[n][m]);
		int step = 1;
		int x = n;
		int y = m;
		while(!((x==1)&&(y==1))){
			step = step + 1;
			switch(min(DTW[x-1][y-1],DTW[x-1][y],DTW[x][y-1])){
			case 1: x--; y--; break;
			case 2: x--; break;
			case 3: y--; break;
			default: System.out.println("Error!!!!");
			}
			
		}
	//	System.out.println("step: "+step);
		double avg = DTW[n][m]/step;
	//	System.out.println("avgDTW:::::"+avg);
		return avg;
	}

	private int min(double d, double e, double f) {
		if(d<=e&&d<=f)
			return 1;
		if(e<=d&&e<=f)
			return 2;
		if(f<=e&&f<=d)
			return 3;
		return 0;
	}

	private double minimum(double a, double b, double c) {
		return Math.min(Math.min(a, b), Math.min(b, c));
	}

	

	private boolean isNoise(Integer id, int i,int noiseThreshold) {
		  if(i<1)
			  return false;
		  if(id.intValue()<0)
	  		{
	  		//System.out.println("id: "+id);
	  		return false;
	  		}
		  if((i+noiseThreshold)>lat.size())
			  return false;
		  for(int j = 1; j<noiseThreshold;j++)
		  {	Location loc = new Location(lat.get(i+j),lon.get(i+j));
		  	//blocks.addPoint2Block(loc);
		  	Integer currentId = new Integer(blocks.findBlockIdForPoint(loc));
		  	
		  	if(!currentId.equals(id))
		  		{
		  	//	System.out.println("id   currentId:  "+id+"       "+currentId);	
		  		return true;
		  		}
		  }
		return false;
	}
	
	 /* 

	private String getTrimedIds(
			ArrayList<NumerosityReductionMapEntry> track) {
		
	//	  String ans;
		  StringBuffer sb = new StringBuffer();
			for (int i = 0; i<track.size();i++){
				sb.append(track.get(i).getValue());
				sb.append(" ");
				
			}
			return sb.toString();
	}
	*/

/*
 * Compute the avg. value of the given ArrayList
 */
	private Double avg(ArrayList<Double> list){
		Double sum= new Double(0);
		for (int i = 0; i< list.size(); i++){
			if(Double.isNaN(list.get(i)))
				throw new NullPointerException();
			sum = sum + list.get(i);
	//		System.out.print(list.get(i)+" ");
		}
	//	System.out.println();
		
	//	System.out.println("sum = "+sum+" avg = "+sum/list.size());
		if(list.size()>0)
		return sum/list.size();
		else
			return -88888.0;
	}
	@SuppressWarnings("rawtypes")
	public ArrayList<Integer> getTrimedPositions(
			ArrayList<NumerosityReductionMapEntry> track) {
		  ArrayList<Integer> ans = new ArrayList<Integer>();
		for (int i = 0; i<track.size();i++){
			ans.add((Integer) track.get(i).getKey());
		}
		return ans;
	}
    public static ArrayList<ArrayList<Route>> getMotifs(){
    	return routes;
    }
    public static ArrayList<Route> getRawTrajectory(){
    	return rawRoutes; 
    }
    public static ArrayList<Route> getAnomaly(){
    	return anomalyRoutes; 
    }
    private void drawAnomaly() {
    	this.anomalyRoutes = new ArrayList<Route>();
		boolean flag=true;
		if(flag)
			return;
		
			
  		for (int k=0;k<anomalyIntervals.size();k++)
  				  {
  					  Route singleRoute = new Route();
  					  int startPos = anomalyIntervals.get(k).getStartPos();
  						int endPos = anomalyIntervals.get(k).getEndPos();
  						double distance = 0;
  						/*
  						for(int index=startPos; index<=endPos;index++)
  							isCovered[index]=true;
  							*/
  		//				System.out.println("startPos: "+startPos);
  		//				System.out.println("endPos: " +endPos);
  						
  					//	System.out.print("track#: "+counter+":       ");
  						
  						Location loca = new Location(ncLat.get(startPos),ncLon.get(startPos));
  						//Location endLoc = new Location(lat.get(startPos),lon.get(startPos));
  						
  						for (int j = startPos; j<=endPos; j++){
  							Location previousLoc =loca;
  							loca = new Location(ncLat.get(j),ncLon.get(j));
  							distance = distance + blocks.distance(blocks.findBlockIdForPoint(previousLoc), blocks.findBlockIdForPoint(loca)); 
  							singleRoute.addLocation(ncLat.get(j), ncLon.get(j));
  								
  						
  							
  						}
  						if (distance>0.1) // remove the false anomalies in the same block.
  						{
  						anomalyRoutes.add(singleRoute);
  						}
  				  }
  		//		  System.out.println("position size: "+positions.size());
  			//	  System.out.println("route size: "+route.size());
  				
  				  //  if(route.size()>2)

  		  	
}

	public static String map2String(HashMap map){
		  String string = new String();
		  Iterator it = map.entrySet().iterator();
		  while(it.hasNext()){
			  
			  string.concat(it.next().toString());
		  }
		  return string;
	  }
		  
	  /**
	   * Performs logging messages distribution.
	   * 
	   * @param message the message to log.
	   */
	  private void log(String message) {
	    this.setChanged();
	    notifyObservers(new SequiturMessage(SequiturMessage.STATUS_MESSAGE, "model: " + message));
	  }

	  /**
	   * Counts spaces in the string.
	   * 
	   * @param str The string.
	   * @return The number of spaces.
	   */
	  public static int countSpaces(String str) {
	    int counter = 0;
	    
	    for (int i = 0; i < str.length(); i++) {
	      if (str.charAt(i) == ' ') {
	        counter++;
	      }
	    }
	//    System.out.println("string: "+str+"   length = "+counter);
	    return counter;
	  }
	  public static boolean isNumeric(String str)  
	  {  
	    try  
	    {  
	      double d = Double.parseDouble(str);  
	    }  
	    catch(NumberFormatException nfe)  
	    {  
	      return false;  
	    }  
	    return true;  
	  }

	public static String parseRule(String string) {
		StringBuffer sb = new StringBuffer();
		//System.out.println("string: "+string);
		ArrayList<String> sa = new ArrayList<String>();
		String[] stringArray = string.split(" ");
		for (String s:stringArray){
			if (s.charAt(0)=='I')
			{
				if(s.contains("r")){
					int rIndex = s.indexOf("r");
					Integer iteration = Integer.valueOf(s.substring(1, rIndex));
					Integer rule = Integer.valueOf(s.substring(rIndex+1));
				//	System.out.println("s: "+s+" iteration: "+iteration+" rule: "+rule);
					String subRule = parseRule(allRules.get(iteration).get(rule).getExpandedRuleString());
					sa.add(subRule);
			//		System.out.println(s+" = "+subRule );
					
				}
				else if(s.contains("C")){
					int cIndex = s.indexOf("C");
					Integer iteration = Integer.valueOf(s.substring(1, cIndex));
					Integer cluster = Integer.valueOf(s.substring(cIndex+1));
				//	System.out.println("s: "+s+" iteration: "+iteration+" cluster: "+cluster);
					Integer ruleInCluster = (Integer)allClusters.get(iteration).get(cluster).toArray()[0];
					String subRule = parseRule(allRules.get(iteration).get(ruleInCluster).getExpandedRuleString());
					sa.add(subRule);
				//	System.out.println(s+" = "+subRule );
	
				}
			}
			else if (s.charAt(0)=='R'){
				throw new IllegalArgumentException("expect 'I' encounter 'R'");
			}
			
			else	//Base Case
			{
				Integer test = Integer.valueOf(s);
				sa.add(s);
		//		System.out.println("s: "+ s);
			}
		}
		for (int i = 0; i<sa.size()-1;i++){
			sb.append(sa.get(i));
			sb.append(" ");
		}
		if(sa.size()>0)
		   sb.append(sa.get(sa.size()-1));
		//System.out.println("sb: "+sb.toString());
		String ans = sb.toString();
		return ans;
	}

	public ArrayList<ArrayList<RuleInterval>> getNewRules() {
		return newRules;
	}

	public void setNewRules(ArrayList<ArrayList<RuleInterval>> newRules) {
		this.newRules = newRules;
	}

}