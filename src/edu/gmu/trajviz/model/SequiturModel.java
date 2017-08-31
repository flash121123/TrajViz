package edu.gmu.trajviz.model;

/*
 * Author: Qingzhe Li
 */
import java.io.BufferedReader;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;

import com.roots.map.ColorBar;
import com.roots.map.MapPanel;

import edu.gmu.itr.Direction;
import edu.gmu.itr.ItrSeq;
import edu.gmu.itr.RuleDensityEstimator;
import edu.gmu.trajviz.gi.GrammarRuleRecord;
import edu.gmu.trajviz.gi.GrammarRules;
import edu.gmu.trajviz.gi.sequitur.SequiturFactory;
import edu.gmu.trajviz.logic.*;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.gmu.trajviz.sax.datastructures.SAXRecords;
import edu.gmu.trajviz.timeseries.TSException;
import edu.gmu.trajviz.util.StackTrace;
import test.TSUtils;

public class SequiturModel extends Observable {
	// public static double MINLINK = 0.0;
	// public final static double (minLink*2) = 0.0;
	public final static int EVAL_RESOLUTION = 100;
	private ArrayList<Integer> ruleMapLength;
	public String pems_station = "./station.txt";
	public String pems = "./station_day_2008-517-610.txt";

	// new added global variable for new rules
	private ArrayList<ArrayList<RuleInterval>> newRules;

	final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	public final static String EVALUATION_HEAD = "DataName,MinLink,AlphabetSize,MinBlocks,NCThreshold,RunningTime,AvgDistance,AvgeStdDev,MinInterDistance,SilhouetteCoefficient,TotalRules,TotalDataPoints, TotalSubTrajectories,CoveredPoints, ImmergableRuleCount\n";

	private ArrayList<Integer> status;
	private static final String SPACE = " ";

	private void runSequitur(int iteration) throws Exception {
		chartData = new MotifChartData(this.dataFileName, lat, lon, 1, alphabetSize); // PAA
		                                                                              // is
		                                                                              // always
		station_location.clear();
		//readStations();
		//readPemsTraffic();
		//updatepp();
		SequiturModel.isColorBarPlot=true;
		clusters = new ArrayList<HashSet<Integer>>();
		filter = new ArrayList<Integer>();
		clusterMap = new HashMap<Integer, Integer>();
		mapToPreviousR0 = new ArrayList<Integer>();

		rules = new GrammarRules();
		try {
			SAXRecords saxFrequencyData = null;
			saxFrequencyData = SequiturFactory.entries2SAXRecords(trimedTrack);
			System.out.println("Input String Length: " + countSpaces(saxFrequencyData.getSAXString(SPACE)));
			consoleLogger.trace("String: " + saxFrequencyData.getSAXString(SPACE));
			// System.out.println("String: "+ saxFrequencyData.getSAXString(SPACE));
			consoleLogger.debug("running sequitur...");

			ItrSeq ss = new ItrSeq(saxFrequencyData.getSAXString(" "), this.dataFileName);
			ss.setAlt(lat);
			tmprule = ss.run(saxFrequencyData);
			consoleLogger.debug("collecting grammar rules data ...");

			consoleLogger.debug("mapping rule intervals on timeseries ...");
			consoleLogger.debug("done ...");

			chartData.setGrammarRules(ItrSeq.arules);
			System.out.println("chartData size: " + chartData.getRulesNumber());

		} catch (TSException e) {
			this.log("error while processing data " + StackTrace.toString(e));
			e.printStackTrace();
		}

	}

	private void updatepp() {
		// TODO Auto-generated method stub
		double[][] dx=new double[pems_traffic.size()][4];
		ArrayList<Integer> flows=new ArrayList<Integer>();
		int i=0;
		for(Integer k : pems_traffic.keySet())
		{
			dx[i][0]=k;
			dx[i][1]=pems_traffic.get(k)/25;
			flows.add(pems_traffic.get(k)/25);
			dx[i][2]=station_location.get(k).get(0);
			dx[i][3]=station_location.get(k).get(1);
					i++;
		}
		ColorBar tmp;
		if(flows.isEmpty())
			tmp=new ColorBar();
		else
			tmp=new ColorBar(Collections.min(flows),Collections.max(flows));
		MapPanel.flowcolor=tmp;
		MapPanel.pp=dx;
		
	}

	HashMap<Integer, Integer> pems_traffic;
/*
	private void readPemsTraffic() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		pems_traffic= new HashMap<Integer, Integer>();
		for(Integer name : station_location.keySet())
		{
			pems_traffic.put(name, 0);
		}
		BufferedReader br = null;
		String line = "\n";
		String cvsSplitBy = ",";
		try {

			br = new BufferedReader(new FileReader(this.pems));
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i == 0) {
					i++;
					continue;
				}
				// use comma as separator
				String[] traffic = line.split(cvsSplitBy);
				
				if(traffic.length<15)
					continue;
				if (traffic[9].isEmpty())
					continue;
				if (traffic[1].isEmpty())
					continue;
				int key = Integer.parseInt(traffic[1]);
				if (station_location.containsKey(key)) {
					int flow = Integer.parseInt(traffic[9]);
					
						int x=pems_traffic.get(key);
						x=x+flow;
						pems_traffic.put(key, x);
					
					
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
*/
	// private static final int DEFAULT_TIME_GAP = 6;//180;
	private static final int DEFAULT_TIME_GAP = 180;
	private boolean[] isCovered;
	private boolean[] ruleCovered;

	// private static final int NOISYELIMINATIONTHRESHOLD = 5;
	public static int alphabetSize;
	private double minLink;
	private int noiseThreshold;
	private GrammarRules rules;
	public static HashMap<String, ArrayList<String>> allPostions;
	public static ArrayList<GrammarRules> allRules;
	public static TreeMap<String, GrammarRuleRecord> sortedRuleMap;
	public static ArrayList<ArrayList<HashSet<Integer>>> allClusters;
	private ArrayList<HashSet<Integer>> clusters;
	private ArrayList<Integer> filter;
	public static ArrayList<ArrayList<Integer>> allFilters;
	private HashMap<Integer, Integer> filterMap;
	HashMap<Integer, Integer> clusterMap;
	private ArrayList<Integer> mapTrimed2Original;
	private ArrayList<Integer> mapToPreviousR0;
	private ArrayList<Integer> mapToOriginalTS;
	public static ArrayList<ArrayList<Integer>> allMapToOriginalTS;
	public static ArrayList<ArrayList<Integer>> allMapToPreviousR0;
	public static HashMap<String, ArrayList<RuleInterval>> finalIntervals;
	private int sortedCounter;
	private String dataFileName;
	private static double lat_center;
	public static double latMax;
	public static double latMin;
	public static double lonMin;
	public static double lonMax;
	public int trajCounter;
	private String[] r0;
	public static ArrayList<String[]> allR0;
	private static double lon_center;
	// The outer arrayList includes all rules, the inner arrayList includes all
	// route under the same rule
	private static ArrayList<ArrayList<Route>> routes;

	private static ArrayList<Route> rawRoutes;

	private static ArrayList<Route> anomalyRoutes;
	public static ArrayList<Double> lat;
	private ArrayList<Double> ncLat = new ArrayList<Double>();
	private ArrayList<Double> ncLon = new ArrayList<Double>();
	// public ArrayList<Double> paaLat;
	// public ArrayList<Double> paaLon;
	public static ArrayList<Double> lon;
	public static ArrayList<Double> latOri;
	public static ArrayList<Double> lonOri;
	private MotifChartData chartData;
	private ArrayList<ArrayList<RuleInterval>> ruleIntervals;
	private ArrayList<RuleInterval> rawAllIntervals;
	// private ArrayList<HashSet<Integer>> mapToOriginRules;
	private double runTime = -1;
	// private static GrammarRules filteredRules;
	@SuppressWarnings("rawtypes")
	public ArrayList<NumerosityReductionMapEntry> trimedTrack;
	// index is the rule# after filtering, Integer value is the actual rule number
	// private ArrayList<Integer> filteredRuleMap = new ArrayList<Integer>();
	private ArrayList<String> words;
	// public Blocks blocks, eBlocks;
	public Blocks eBlocks;
	// public Blocks tblocks;
	public AdaptiveBlocks blocks;
	private int minBlocks;

	private ArrayList<ArrayList<RuleInterval>> tmprule;
	private int breakPoint;

	public static int minYield;
	public static int mfthreshold;
	public static int anomalythreshold;

	public static ArrayList<ArrayList<Route>> motifs;
	private static Logger consoleLogger;
	private static Level LOGGING_LEVEL = Level.DEBUG;
	public static boolean isColorBarPlot;

	static {
		consoleLogger = (Logger) LoggerFactory.getLogger(SequiturModel.class);
		consoleLogger.setLevel(LOGGING_LEVEL);
	}

	/**
	 * The file name setter.
	 * 
	 * @param filename
	 *          The file name to set.
	 */
	/*
	 * public static GrammarRules getFilteredGrammarRules(){ return filteredRules;
	 * }
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
	 * Load the data which is supposedly in the file which is selected as the data
	 * source.
	 * 
	 * @param limitStr
	 *          the limit of lines to read.
	 */
	public synchronized void loadData(String limitStr) {

		if ((null == this.dataFileName) || this.dataFileName.isEmpty()) {
			this.log("unable to load data - no data source select yet");
			return;
		}
		SequiturModel.isColorBarPlot=false;
		Path path = Paths.get(this.dataFileName);
		if (!Files.exists(path)) {
			this.log("file" + this.dataFileName + "doesn't exist.");
			return;
		}
		// read the input
		// init the data array
		ArrayList<Double> data = new ArrayList<Double>();
		ArrayList<Double> data1 = new ArrayList<Double>();
		status = new ArrayList<Integer>(); // taxi loading status
		ArrayList<Integer> time = new ArrayList<Integer>();

		ArrayList<Double> tmp1 = new ArrayList<Double>();
		ArrayList<Double> tmp2 = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> dx = new ArrayList<ArrayList<Double>>();

		ArrayList<Long> timeAsUnixEpoc = new ArrayList<Long>(); // number of seconds
		                                                        // since Jan. 1 1970
		                                                        // midnight GMT, if
		                                                        // the time is in
		                                                        // milliseconds, it
		                                                        // will need to be
		                                                        // converted to
		                                                        // seconds,otherwise
		                                                        // it may over
		                                                        // Integer's limit.
		ArrayList<Double> as = new ArrayList<Double>();
		try {
			long loadLimit = 0l;
			if (!(null == limitStr) && !(limitStr.isEmpty())) {

				loadLimit = Long.parseLong(limitStr);
			}

			BufferedReader reader = Files.newBufferedReader(path, DEFAULT_CHARSET);
			String line = null;
			long lineCounter = 0;
			int trajectoryCounter = -1001;

			while ((line = reader.readLine()) != null) {
				String[] lineSplit = line.trim().split("\\s+|,");
				double value = new BigDecimal(lineSplit[0]).doubleValue();

				double value1 = new BigDecimal(lineSplit[1]).doubleValue();
				int value2 = Integer.parseInt(lineSplit[2]);
				long value3 = Long.parseLong(lineSplit[3]);
				as.add((double) value3);
				// time.add(TSUtils.DataConvert(value3));

				/*
				 * if(value2==1000) breakPoint = status.size();
				 */
				// if
				// (value>=37.7254&&value<=37.8212&&value1>=-122.5432&&value1<=-122.3561)
				{
					if ((lineCounter <= 1)
					    || (Math.abs(value3 - timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1)) <= DEFAULT_TIME_GAP
					        && (value3 - timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1)) != 0)) {

						if ((value <= 90) && (value >= -90)) {
							data.add(value);

							data1.add(value1);
							status.add(value2);

							timeAsUnixEpoc.add(value3);
						} else {
							data.add(value1);
							data1.add(value);
							status.add(value2);

							timeAsUnixEpoc.add(value3);
						}
					} else {
						data.add((double) trajectoryCounter); // adding dummy point to split
						                                      // two trajectories
						data1.add((double) trajectoryCounter);
						trajectoryCounter--;
						status.add(-1);
						time.add(-1);
						timeAsUnixEpoc.add(value3);
						// following is adding the first point of a new trajectories
						if ((value <= 90) && (value >= -90)) {
							data.add(value);
							data1.add(value1);
							status.add(value2);
							timeAsUnixEpoc.add(value3);
						} else {
							data.add(value1);
							data1.add(value);
							status.add(value2);
							timeAsUnixEpoc.add(value3);
						}
					}
					lineCounter++;
				}
				if ((loadLimit > 0 && (lineCounter >= loadLimit))) {
					break;
				}
			}
			data.add((double) trajectoryCounter);
			data1.add((double) trajectoryCounter);
			trajCounter = 0 - (trajectoryCounter + 1000);
			status.add(-1);
			timeAsUnixEpoc.add(timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1));
			time.add(-1);
			reader.close();
		} catch (Exception e) {
			String stackTrace = StackTrace.toString(e);
			System.err.println(StackTrace.toString(e));
			this.log("error while trying to read data from " + this.dataFileName + ":\n" + stackTrace);
		}

		latOri = new ArrayList<Double>();
		lonOri = new ArrayList<Double>();

		latMax = Double.valueOf(data.get(0));
		lonMax = Double.valueOf(data1.get(0));
		latMin = Double.valueOf(data.get(0));
		lonMin = Double.valueOf(data1.get(0));
		for (int i = 0; i < data.size(); i++) {
			double temp_latitude = Double.valueOf(data.get(i));
			double temp_longitude = Double.valueOf(data1.get(i));
			// System.out.println("i = "+i+": "+temp_latitude+","+temp_longitude);
			latOri.add(temp_latitude);
			lonOri.add(temp_longitude);
			if ((temp_latitude >= -90) && temp_latitude > latMax)

			{

				latMax = temp_latitude;
			}
			if ((temp_latitude >= -90) && temp_latitude < latMin) {

				latMin = temp_latitude;
			}
			if (temp_longitude >= -180 && temp_longitude > lonMax)
				lonMax = temp_longitude;
			if (temp_longitude >= -180 && temp_longitude < lonMin)
				lonMin = temp_longitude;
			// test loaded points
			// System.out.println(this.lat.get(i)+",
			// "+this.lon.get(i)+","+data.get(i)+", "+data1.get(i));
		}
		data = new ArrayList<>();
		data1 = new ArrayList<>();
		lat_center = (latMax + latMin) / 2;

		lon_center = (lonMax + lonMin) / 2;
		System.out.println("lonMax:  " + lonMax + "       lonMin: " + lonMin);

		System.out.println("latMax:  " + latMax + "       latMin: " + latMin);
		System.out.println("Number of trajectories: " + trajCounter);
		consoleLogger.debug("loaded " + latOri.size() + " points and " + trajCounter + " Trajecoties... ");

		this.log("loaded " + latOri.size() + " points from " + this.dataFileName);

		setChanged();
		notifyObservers(new SequiturMessage(SequiturMessage.TIME_SERIES_MESSAGE, latOri, lonOri));

	}

	public synchronized void loadData2(String limitStr) {

		if ((null == this.dataFileName) || this.dataFileName.isEmpty()) {
			this.log("unable to load data - no data source select yet");
			return;
		}

		Path path = Paths.get(this.dataFileName);
		if (!Files.exists(path)) {
			this.log("file" + this.dataFileName + "doesn't exist.");
			return;
		}
		// read the input
		// init the data array
		ArrayList<Double> data = new ArrayList<Double>();
		ArrayList<Double> data1 = new ArrayList<Double>();
		status = new ArrayList<Integer>(); // taxi loading status
		ArrayList<Integer> time = new ArrayList<Integer>();

		ArrayList<Double> tmp1 = new ArrayList<Double>();
		ArrayList<Double> tmp2 = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> dx = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> dx2 = new ArrayList<ArrayList<Double>>();

		ArrayList<Long> timeAsUnixEpoc = new ArrayList<Long>(); // number of seconds
		                                                        // since Jan. 1 1970
		                                                        // midnight GMT, if
		                                                        // the time is in
		                                                        // milliseconds, it
		                                                        // will need to be
		                                                        // converted to
		                                                        // seconds,otherwise
		                                                        // it may over
		                                                        // Integer's limit.
		ArrayList<Double> as = new ArrayList<Double>();
		try {
			long loadLimit = 0l;
			if (!(null == limitStr) && !(limitStr.isEmpty())) {

				loadLimit = Long.parseLong(limitStr);
			}

			BufferedReader reader = Files.newBufferedReader(path, DEFAULT_CHARSET);
			String line = null;
			long lineCounter = 0;
			int trajectoryCounter = -1001;

			while ((line = reader.readLine()) != null) {
				String[] lineSplit = line.trim().split("\\s+|,");
				double value = new BigDecimal(lineSplit[0]).doubleValue();

				double value1 = new BigDecimal(lineSplit[1]).doubleValue();
				int value2 = Integer.parseInt(lineSplit[2]);
				long value3 = Long.parseLong(lineSplit[3]);
				as.add((double) value3);
				time.add(TSUtils.DataConvert(value3));
				if ((lineCounter <= 1) || (Math.abs(value3 - timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1)) <= DEFAULT_TIME_GAP
				    && (value3 - timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1)) != 0)) {

					if ((value <= 90) && (value >= -90)) {
						data.add(value);
						data1.add(value1);
						tmp1.add(value);
						tmp2.add(value1);
						status.add(value2);
						timeAsUnixEpoc.add(value3);
					} else {
						data.add(value1);
						data1.add(value);
						tmp1.add(value);
						tmp2.add(value1);
						status.add(value2);
						timeAsUnixEpoc.add(value3);
					}
				} else {
					data.add((double) trajectoryCounter); // adding dummy point to split
					                                      // two trajectories
					data1.add((double) trajectoryCounter);

					dx.add(tmp1);
					dx2.add(tmp2);
					tmp1 = new ArrayList<Double>();
					tmp2 = new ArrayList<Double>();

					trajectoryCounter--;
					status.add(-1);
					time.remove(time.size() - 1);

					time = new ArrayList<Integer>();
					timeAsUnixEpoc.add(value3);
					// following is adding the first point of a new trajectories
					if ((value <= 90) && (value >= -90)) {
						data.add(value);
						data1.add(value1);
						status.add(value2);
						timeAsUnixEpoc.add(value3);
					} else {
						data.add(value1);
						data1.add(value);
						status.add(value2);
						timeAsUnixEpoc.add(value3);
					}
				}
				lineCounter++;

				if ((loadLimit > 0 && (lineCounter >= loadLimit))) {
					break;
				}
			}
			data.add((double) trajectoryCounter);
			data1.add((double) trajectoryCounter);
			trajCounter = 0 - (trajectoryCounter + 1000);
			status.add(-1);
			timeAsUnixEpoc.add(timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1));
			time.add(-1);
			reader.close();
		} catch (Exception e) {
			String stackTrace = StackTrace.toString(e);
			System.err.println(StackTrace.toString(e));
			this.log("error while trying to read data from " + this.dataFileName + ":\n" + stackTrace);
		}

		latOri = new ArrayList<Double>();
		lonOri = new ArrayList<Double>();

		latMax = Double.valueOf(data.get(0));
		lonMax = Double.valueOf(data1.get(0));
		latMin = Double.valueOf(data.get(0));
		lonMin = Double.valueOf(data1.get(0));
		for (int i = 0; i < data.size(); i++) {
			double temp_latitude = Double.valueOf(data.get(i));
			double temp_longitude = Double.valueOf(data1.get(i));
			// System.out.println("i = "+i+": "+temp_latitude+","+temp_longitude);
			latOri.add(temp_latitude);
			lonOri.add(temp_longitude);
			if ((temp_latitude >= -90) && temp_latitude > latMax)

			{

				latMax = temp_latitude;
			}
			if ((temp_latitude >= -90) && temp_latitude < latMin) {

				latMin = temp_latitude;
			}
			if (temp_longitude >= -180 && temp_longitude > lonMax)
				lonMax = temp_longitude;
			if (temp_longitude >= -180 && temp_longitude < lonMin)
				lonMin = temp_longitude;
			// test loaded points
			// System.out.println(this.lat.get(i)+",
			// "+this.lon.get(i)+","+data.get(i)+", "+data1.get(i));
		}
		data = new ArrayList<>();
		data1 = new ArrayList<>();
		lat_center = (latMax + latMin) / 2;
		lon_center = (lonMax + lonMin) / 2;
		System.out.println("lonMax:  " + lonMax + "       lonMin: " + lonMin);
		System.out.println("latMax:  " + latMax + "       latMin: " + latMin);
		System.out.println("Number of trajectories: " + trajCounter);
		consoleLogger.debug("loaded " + latOri.size() + " points and " + trajCounter + " Trajecoties... ");
		this.log("loaded " + latOri.size() + " points from " + this.dataFileName);
		setChanged();
		notifyObservers(new SequiturMessage(SequiturMessage.TIME_SERIES_MESSAGE, latOri, lonOri));
	}

	public static double getLatitudeCenter() {
		return lat_center;
	}

	public static double getLongitudeCenter() {
		return lon_center;
	}

	// This is the method to transform yifeng's rule output to the type of
	// GrammarRules

	private void convert(ArrayList<ArrayList<RuleInterval>> rules) {
		
		ArrayList<ArrayList<Route>> r = new ArrayList<ArrayList<Route>>(rules.size());
		for (ArrayList<RuleInterval> x : rules) {
			ArrayList<Route> tmp = new ArrayList<Route>();
			for (RuleInterval y : x) {
				ArrayList<Double> m1 = new ArrayList<Double>(y.getLength());
				ArrayList<Double> m2 = new ArrayList<Double>(y.getLength());

				for (int i = y.startPos; i <= y.endPos - 1; i = i + 2) {
					m1.add(lat.get(i));
					m2.add(lon.get(i));
				}
				m1.add(lat.get(y.endPos));
				m2.add(lon.get(y.endPos));
				Route ro = new Route(m1, m2);
				tmp.add(ro);
		
			}
			r.add(tmp);
		}
		
		
		motifs = r;
		
		MapPanel.createDenseMap(motifs);
		for(Direction<Integer> x : RuleDensityEstimator.az)
		{
			ArrayList<RuleInterval> ris = new ArrayList<RuleInterval>();
			ArrayList<Double> m1 = new ArrayList<Double>();
			ArrayList<Double> m2 = new ArrayList<Double>();
			ArrayList<Route> tmp = new ArrayList<Route>();
			for (int i = x.start; i <= x.end - 1; i = i + 2) {
				m1.add(lat.get(i));
				m2.add(lon.get(i));
			}
			m1.add(lat.get(x.end));
			m2.add(lon.get(x.end));
			Route ro = new Route(m1, m2);
		tmp.add(ro);
		r.add(tmp);
		ris.add(new RuleInterval(x.start,x.end));
		rules.add(ris);
		}
		ruleIntervals = rules;
		motifs = r;

	}

	public synchronized void processData(double minLink, int alphabetSize, int minBlocks, int noiseThreshold)
	    throws IOException {
		sortedCounter = 0;
		mfthreshold = (int) minLink;
		minLink = 0.2;
		this.minLink = minLink;

		this.minBlocks = minBlocks;
		minYield = minBlocks;
		anomalythreshold = noiseThreshold;

		noiseThreshold = 2;
		this.noiseThreshold = noiseThreshold;

		SequiturModel.alphabetSize = alphabetSize;
		allRules = new ArrayList<GrammarRules>();
		allFilters = new ArrayList<ArrayList<Integer>>();
		allClusters = new ArrayList<ArrayList<HashSet<Integer>>>();
		allR0 = new ArrayList<String[]>();
		allMapToPreviousR0 = new ArrayList<ArrayList<Integer>>();
		allMapToOriginalTS = new ArrayList<ArrayList<Integer>>();
		rawRoutes = new ArrayList<Route>();
		anomalyRoutes = new ArrayList<Route>();
		lat = new ArrayList<Double>();
		motifs = new ArrayList<ArrayList<Route>>();
		lon = new ArrayList<Double>();

		Comparator<String> expandedRuleComparator = new Comparator<String>() {
			@Override
			public int compare(String r1, String r2) {
				Integer iteration1 = 0;
				Integer iteration2 = 0;
				Integer rule1 = 0;
				Integer rule2 = 0;
				if (r1.charAt(0) == 'I') {
					if (r1.contains("r")) {
						int rIndex = r1.indexOf("r");
						iteration1 = Integer.valueOf(r1.substring(1, rIndex));
						rule1 = Integer.valueOf(r1.substring(rIndex + 1));
						// System.out.println("r1: "+r1+" iteration: "+iteration1+" rule1:
		        // "+rule1);

						// System.out.println(s+" = "+subRule );
					} else
						throw new IllegalArgumentException(r1 + " is not comparable with " + r2);
				} else
					throw new IllegalArgumentException(r1 + " is not comparable with " + r2);
				if (r2.charAt(0) == 'I') {
					if (r2.contains("r")) {
						int rIndex = r2.indexOf("r");
						iteration2 = Integer.valueOf(r2.substring(1, rIndex));
						rule2 = Integer.valueOf(r2.substring(rIndex + 1));
						// System.out.println("r2: "+r2+" iteration2: "+iteration2+" rule2:
		        // "+rule2);

						// System.out.println(s+" = "+subRule );

					} else
						throw new IllegalArgumentException(r1 + " is not comparable with " + r2);
				} else
					new IllegalArgumentException(r1 + " is not comparable with " + r2);

				if (allRules.get(iteration2).get(rule2).getActualRuleYield() > allRules.get(iteration1).get(rule1)
		        .getActualRuleYield())
					return 1;

				else
					return -1;

			}
		};

		SequiturModel.sortedRuleMap = new TreeMap<String, GrammarRuleRecord>(expandedRuleComparator);

		StringBuffer sb = new StringBuffer();
		if (null == this.latOri || null == this.lonOri || this.latOri.size() == 0 || this.lonOri.size() == 0) {
			this.log("unable to \"Process data\" - no data were loaded...");
		} else {

			consoleLogger.info("setting up GI with params: ");
			sb.append(" algorithm: Sequitur");
			sb.append(" Motif Filter Threshold: ").append(mfthreshold);

			sb.append(" Grid size: ").append(alphabetSize);
			sb.append(" Minimal Continuous Blocks: ").append(minBlocks);
			sb.append(" Anomaly Threshold: ").append(noiseThreshold);

			consoleLogger.info(sb.toString());
			this.log(sb.toString());
		}
		rawAllIntervals = new ArrayList<RuleInterval>();
		long beginTime = System.currentTimeMillis();
		System.out.println("begin time: " + beginTime);
		buildModel();

		drawRawTrajectories();

		allMapToPreviousR0.add(mapToPreviousR0);

		/*
		 * 
		 * replace rules with rules' ids and clusters' ids
		 * 
		 */

		Integer iteration = 0;
		try {
			runSequitur(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iteration = iteration + 1;

		drawOnMap();
		System.out.println("total anomalies: " + anomalyRoutes.size());

		System.out.println("Sorted Map.size = " + sortedRuleMap.size() + "sortedCounter = " + sortedCounter);
		System.out.println(Collections.max(lat));
		System.out.println(Collections.min(lat));
		System.out.println(Collections.max(lon));
		System.out.println(Collections.min(lon));

		this.log("processed data, painting on map");
		consoleLogger.info("process finished");
		setChanged();

		System.out.println("running time: " + runTime);
		ArrayList<Integer> frequency = new ArrayList<Integer>();

		notifyObservers(new SequiturMessage(SequiturMessage.CHART_MESSAGE, this.chartData, ruleIntervals, ruleMapLength));

	}

	private void buildModel() {
		routes = new ArrayList<ArrayList<Route>>();
		double avgLat;
		double avgLon;
		/*
		 * use the centroid(paaLat,paaLon) to represent the data
		 */
		blocks = new AdaptiveBlocks(alphabetSize, latOri, lonOri);
		ncLat = new ArrayList<Double>();
		ncLon = new ArrayList<Double>();
		adaptiveResample(blocks);

		isCovered = new boolean[lat.size()];

		ruleCovered = new boolean[lat.size()];
		for (int i = 0; i < lat.size(); i++) {
			isCovered[i] = true;

		}
		words = new ArrayList<String>();
		// add all points into blocks.
		Integer previousId = (Integer) (-1);

		trimedTrack = new ArrayList<NumerosityReductionMapEntry>();
		mapTrimed2Original = new ArrayList<Integer>(); // The index is the position
		                                               // in trimmed array, and the
		                                               // content is position in
		                                               // original time series.
		mapToOriginalTS = new ArrayList<Integer>();
		int startPoint = 0;
		int endPoint = 0;
		for (int i = 0; i < ncLat.size(); i++) {

			Location loc = new Location(ncLat.get(i), ncLon.get(i));
			// blocks.addPoint2Block(loc); this should not work here because the point
			// will change if it is a noisy point.
			Integer id = new Integer(blocks.findBlockIdForPoint(loc));

			if (isNoise(id, i, noiseThreshold)) {
				// lat.set(i, lat.get(i-1));
				ncLat.set(i, ncLat.get(i - 1));
				// lon.set(i, lon.get(i-1));
				ncLon.set(i, ncLon.get(i - 1));
				
				id = previousId;
			}

			if (id < -1000) {
				endPoint = i - 1;
				rawAllIntervals.add(new RuleInterval(startPoint, endPoint));
				startPoint = i + 1;
			}

		
			words.add(id.toString());

			Integer trimedIndex = 0;
			if (!id.equals(previousId)) {

				NumerosityReductionMapEntry<Integer, String> entry = new NumerosityReductionMapEntry<Integer, String>(
				    new Integer(i), id.toString());
				trimedTrack.add(entry);
				mapTrimed2Original.add(i);
				mapToOriginalTS.add(i);
				previousId = id;
			}

		}
		System.out.print("mapTrimed2Original: ");
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

	private void adaptiveResample(AdaptiveBlocks blocks) {
		int i = 1;
		double latPre = latOri.get(0);
		double lonPre = lonOri.get(0);
		lat.add(latPre);
		lon.add(lonPre);
		ncLat.add(latPre);
		ncLon.add(lonPre);
		boolean firstPoint = true;
		while (i < latOri.size()) {

			if (status.get(i) == 1000)
				breakPoint = lat.size();

			if (latOri.get(i) < -180) {

				lat.add(latOri.get(i));
				lon.add(lonOri.get(i));

				ncLat.add(latOri.get(i));
				ncLon.add(lonOri.get(i));
				i++;

				firstPoint = true;

			} else {
				if (firstPoint) {
					lat.add(latOri.get(i));
					lon.add(lonOri.get(i));
					ncLat.add(latOri.get(i));
					ncLon.add(lonOri.get(i));
					i++;
					firstPoint = false;
				} else {
					int latSpan = blocks.latSpan(latOri.get(i), latOri.get(i - 1));
					int lonSpan = blocks.lonSpan(lonOri.get(i), lonOri.get(i - 1));

					if (latSpan > 1 || lonSpan > 1) {
					
						int skip = Math.max(latSpan,lonSpan);
						double latstep = (latOri.get(i) - latOri.get(i - 1)) / skip;
						double lonstep = (lonOri.get(i) - lonOri.get(i - 1)) / skip;
						for (int j = 0; j < skip; j++) {
							lat.add((latOri.get(i - 1) + latstep * (j + 1)));
							lon.add((lonOri.get(i - 1) + lonstep * (j + 1)));
							ncLat.add((latOri.get(i - 1) + latstep * (j + 1)));
							ncLon.add((lonOri.get(i - 1) + lonstep * (j + 1)));
							// System.out.println(lat.get(i+j)+" , "+lon.get(i+j));

						}
						lat.add(latOri.get(i));
						lon.add(lonOri.get(i));
						ncLat.add(latOri.get(i));
						ncLon.add(lonOri.get(i));
						i++;
					} else {
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

		for (int k = 0; k < rawAllIntervals.size(); k++) {

			Route singleRoute = new Route();
			int startPos = rawAllIntervals.get(k).getStartPos();
			int endPos = rawAllIntervals.get(k).getEndPos();
			for (int j = startPos; j <= endPos; j++) {

				Location loca = new Location(lat.get(j), lon.get(j));

				singleRoute.addLocation(lat.get(j), lon.get(j));

			}
			rawRoutes.add(singleRoute);

		}

	}

	private void drawOnMap() {
		boolean flag = true;
		if (flag) {
			for (int i = 0; i < isCovered.length; i++) {
				isCovered[i] = true;
				ruleCovered[i] = false;
			}
			finalIntervals = new HashMap<String, ArrayList<RuleInterval>>();
			ruleIntervals = new ArrayList<ArrayList<RuleInterval>>();
			new ArrayList<RuleInterval>();
			routes = new ArrayList<ArrayList<Route>>();
			ruleMapLength = new ArrayList<Integer>();
		}
		// newRules is the global variable for the new rules.
		this.setNewRules(tmprule);
		convert(newRules);

	}

	public static void printArrayList(ArrayList<Integer> al) {
		if (al == null || al.size() == 0)
			System.out.println("Null or empty ArrayList");
		else {
			// System.out.print("[ ");
			for (int i = 0; i < al.size(); i++)
				System.out.println(al.get(i) + " ");
			System.out.println();
		}
	}

	private boolean isNoise(Integer id, int i, int noiseThreshold) {
		if (i < 1)
			return false;

		if (id.intValue() < 0) {
			// System.out.println("id: "+id);
			return false;
		}
		if ((i + noiseThreshold) > lat.size())
			return false;
		for (int j = 1; j < noiseThreshold; j++) {
			Location loc = new Location(lat.get(i + j), lon.get(i + j));
			// blocks.addPoint2Block(loc);
			Integer currentId = new Integer(blocks.findBlockIdForPoint(loc));

			if (!currentId.equals(id)) {
				// System.out.println("id currentId: "+id+" "+currentId);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Integer> getTrimedPositions(ArrayList<NumerosityReductionMapEntry> track) {
		ArrayList<Integer> ans = new ArrayList<Integer>();
		for (int i = 0; i < track.size(); i++) {
			ans.add((Integer) track.get(i).getKey());
		}
		return ans;
	}

	public static ArrayList<ArrayList<Route>> getMotifs() {
		return routes;
	}

	public static ArrayList<Route> getRawTrajectory() {
		return rawRoutes;
	}

	public static ArrayList<Route> getAnomaly() {
		return anomalyRoutes;
	}

	public static String map2String(HashMap map) {
		String string = new String();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {

			string.concat(it.next().toString());
		}
		return string;
	}

	/**
	 * Performs logging messages distribution.
	 * 
	 * @param message
	 *          the message to log.
	 */
	private void log(String message) {
		this.setChanged();
		notifyObservers(new SequiturMessage(SequiturMessage.STATUS_MESSAGE, "model: " + message));
	}

	/**
	 * Counts spaces in the string.
	 * 
	 * @param str
	 *          The string.
	 * @return The number of spaces.
	 */
	public static int countSpaces(String str) {
		int counter = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				counter++;
			}
		}
		return counter;
	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String parseRule(String string) {
		StringBuffer sb = new StringBuffer();
		// System.out.println("string: "+string);
		ArrayList<String> sa = new ArrayList<String>();
		String[] stringArray = string.split(" ");
		for (String s : stringArray) {
			if (s.charAt(0) == 'I') {
				if (s.contains("r")) {
					int rIndex = s.indexOf("r");
					Integer iteration = Integer.valueOf(s.substring(1, rIndex));
					Integer rule = Integer.valueOf(s.substring(rIndex + 1));
					String subRule = parseRule(allRules.get(iteration).get(rule).getExpandedRuleString());
					sa.add(subRule);

				} else if (s.contains("C")) {
					int cIndex = s.indexOf("C");
					Integer iteration = Integer.valueOf(s.substring(1, cIndex));
					Integer cluster = Integer.valueOf(s.substring(cIndex + 1));
					Integer ruleInCluster = (Integer) allClusters.get(iteration).get(cluster).toArray()[0];
					String subRule = parseRule(allRules.get(iteration).get(ruleInCluster).getExpandedRuleString());
					sa.add(subRule);

				}
			} else if (s.charAt(0) == 'R') {
				throw new IllegalArgumentException("expect 'I' encounter 'R'");
			}

			else // Base Case
			{
				Integer test = Integer.valueOf(s);
				sa.add(s);
			}
		}
		for (int i = 0; i < sa.size() - 1; i++) {
			sb.append(sa.get(i));
			sb.append(" ");
		}
		if (sa.size() > 0)
			sb.append(sa.get(sa.size() - 1));
		String ans = sb.toString();
		return ans;
	}

	public ArrayList<ArrayList<RuleInterval>> getNewRules() {
		return newRules;
	}

	HashMap<Integer, ArrayList<Double>> station_location = new HashMap<Integer, ArrayList<Double>>();

	public void setNewRules(ArrayList<ArrayList<RuleInterval>> newRules) {
		this.newRules = newRules;
	}

	public void readStations() {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		String line = "\n";
		String cvsSplitBy = "\t";
		try {
			br = new BufferedReader(new FileReader(this.pems_station));
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i == 0) {
					i++;
					continue;
				}
				// use comma as separator
				String[] station = line.split(cvsSplitBy);
	
				if (station[8].isEmpty())
					continue;
				if (station[9].isEmpty())
					continue;

				ArrayList<Double> loc = new ArrayList<Double>(2);
				loc.add(Double.parseDouble(station[8]));
				loc.add(Double.parseDouble(station[9]));
				int stat = Integer.parseInt(station[0]);

				if (loc.get(0) < latMax && loc.get(0) > latMin)
					if (loc.get(1) < lonMax && loc.get(1) > lonMin) {
						station_location.put(stat, loc);
					}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}