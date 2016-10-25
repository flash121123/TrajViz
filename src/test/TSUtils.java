package test;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

import core.agi.AGrammarRuleRecord;
import core.agi.AGrammarRules;
import core.agi.RuleInterval;
import edu.gmu.trajviz.model.SequiturMessage;
import edu.gmu.trajviz.util.StackTrace;

/**
 * Implements algorithms for low-level data manipulation.
 * 
 * @author Pavel Senin
 * 
 */
public final class TSUtils {

  /** The latin alphabet, lower case letters a-z. */
  static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
      'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9','!','"','#','$','%','&','(',')','*','+',',','-','.','/'};

  // logging stuff
  //
  public static double[] cuts;
  public static double[] cuts2;
  public static double BESTFACT=0.8;

public static int count=0;
  public static void get_c1(double[] c)
  {
	  cuts=c;
  }
  public static void get_c2(double[] c)
  {
	  cuts2=c;
  }
  
  
  public static double[] send_c1()
  {
	  return cuts;
  }
  public static double[] send_c2()
  {
	  return cuts2;
  }
  
  
  /**
   * Constructor.
   */
  private TSUtils() {
    super();
  }

  
	public void loadData(String dataFileName,String limitStr) {
		
		int trajCounter=0;
		ArrayList<Double> latOri=new ArrayList<Double>();
		ArrayList<Double> lonOri=new ArrayList<Double>();
		Double latMax,latMin,lonMax,lonMin;
		
		
		if ((null == dataFileName) || dataFileName.isEmpty()) {
			return;
		}
		Path path = Paths.get(dataFileName);
		if (!Files.exists(path)) {
			return;
		}
		// read the input
		// init the data array
		ArrayList<Double> data = new ArrayList<Double>();
		ArrayList<Double> data1 = new ArrayList<Double>();
		ArrayList<Integer> status = new ArrayList<Integer>(); // taxi loading status
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

			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
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

				/*
				 * if(value2==1000) breakPoint = status.size();
				 */
				// if
				// (value>=37.7254&&value<=37.8212&&value1>=-122.5432&&value1<=-122.3561)
				{
					if ((lineCounter <= 1)
					    || (Math.abs(value3 - timeAsUnixEpoc.get(timeAsUnixEpoc.size() - 1)) <= 180
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
			reader.close();
		} catch (Exception e) {
			String stackTrace = StackTrace.toString(e);
			System.err.println(StackTrace.toString(e));
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
	// test loaded points
			// System.out.println(this.lat.get(i)+",
			// "+this.lon.get(i)+","+data.get(i)+", "+data1.get(i));
		}
		data = new ArrayList<>();
		data1 = new ArrayList<>();
		
	}
	
  /**
   * Reads timeseries from a file. Assumes that file has a single double value on every line.
   * Assigned timestamps are the line numbers.
   * 
   * @param filename The file to read from.
   * @param sizeLimit The number of lines to read.
   * @return Timeseries.
   * @throws NumberFormatException if error occurs.
   * @throws IOException if error occurs.
   * @throws TSException if error occurs.
   */


  /**
   * Reads timeseries from a file. Assumes that file has a single double value on every line.
   * Assigned timestamps are the line numbers.
   * 
   * @param filename The file to read from.
   * @param columnIdx The column index.
   * @param sizeLimit The number of lines to read, 0 == all.
   * @return data.
   * @throws NumberFormatException if error occurs.
   * @throws IOException if error occurs.
   * @throws TSException if error occurs.
   */

  /**
   * Finds the maximal value in timeseries.
   * 
   * @param series The timeseries.
   * @return The max value.
   */


  /**
   * Finds the maximal value in timeseries.
   * 
   * @param series The timeseries.
   * @return The max value.
   */
  public static double max(double[] series) {
    if (countNaN(series) == series.length) {
      return Double.NaN;
    }
    double max = Double.MIN_VALUE;
    for (int i = 0; i < series.length; i++) {
      if (max < series[i]) {
        max = series[i];
      }
    }
    return max;
  }

  public static double[] znorm(double[] series, double normalizationThreshold) {
	  
	    double[] res = new double[series.length];
	    double mean = mean(series);
	    double sd = stDev(series);
	    if (sd < normalizationThreshold) {
	      return series.clone();
	    }
	    for (int i = 0; i < res.length; i++) {
	      res[i] = (series[i] - mean) / sd;
	    }
	    return res;
	  }

  

  /**
   * Finds the minimal value in timeseries.
   * 
   * @param series The timeseries.
   * @return The min value.
   */
  public static double min(double[] series) {
    if (countNaN(series) == series.length) {
      return Double.NaN;
    }
    double min = Double.MAX_VALUE;
    for (int i = 0; i < series.length; i++) {
      if (min > series[i]) {
        min = series[i];
      }
    }
    return min;
  }

  /**
   * Computes the mean value of timeseries.
   * 
   * @param series The timeseries.
   * @return The mean value.
   */

  /**
   * Computes the mean value of timeseries.
   * 
   * @param series The timeseries.
   * @return The mean value.
   */
  public static double mean(double[] series) {
    double res = 0D;
    int count = 0;
    for (double tp : series) {
      if (Double.isNaN(tp) || Double.isInfinite(tp)) {
        continue;
      }
      else {
        res += tp;
        count += 1;
      }
    }
    if (count > 0) {
      return res / ((Integer) count).doubleValue();
    }
    return Double.NaN;
  }

  /**
   * Computes the mean for integer series.
   * 
   * @param series
   * @return
   */
  public static Integer mean(int[] series) {
    int res = 0;
    int count = 0;
    for (int tp : series) {
      res += tp;
      count += 1;
    }
    return res / count;
  }

  
  /**
   * Computes the mean of double time series
 * @param series
 * @return
 */
public static double mean(ArrayList<Double> series) {
	    Double res = new Double(0);
	    int count = 0;
	    for (Double tp : series) {
	      res += tp;
	      count += 1;
	    }
	    return res / count;
	  }
  /**
   * Speed-optimized implementation.
   * 
   * @param series The timeseries.
   * @return The mean value.
   */
  public static double optimizedMean(double[] series) {
    double res = 0D;
    int count = 0;
    for (double tp : series) {
      res += tp;
      count += 1;
    }
    return res / ((Integer) count).doubleValue();
  }

  /**
   * Computes the autocorrelation value of timeseries. according to algorithm in:
   * http://www.itl.nist.gov/div898/handbook/eda/section3/eda35c.htm
   * 
   * @param series The timeseries.
   * @param lag The lag
   * @return The autocorrelation value.
   */

  /**
   * Computes the autocorrelation value of timeseries. according to algorithm in:
   * http://www.itl.nist.gov/div898/handbook/eda/section3/eda35c.htm
   * 
   * @param series The timeseries.
   * @param lag The lag
   * @return The autocorrelation value.
   */
  public static double autocorrelation(double series[], int lag) {
    double ac = 0;

    double avg = mean(series);
    double numerator = 0;
    for (int i = 0; i < series.length - lag; i++) {
      if (Double.isNaN(series[i]) || Double.isInfinite(series[i])) {
        continue;
      }
      numerator += (series[i] - avg) * (series[i + lag] - avg);
    }
    double denominator = 0;
    for (int i = 0; i < series.length; i++) {
      if (Double.isNaN(series[i]) || Double.isInfinite(series[i])) {
        continue;
      }
      denominator += (series[i] - avg) * (series[i] - avg);
    }
    ac = numerator / denominator;
    return ac;
  }

  /**
   * Compute the variance of timeseries.
   * 
   * @param series The timeseries.
   * @return The variance.
   */
  /**
   * Compute the variance of timeseries.
   * 
   * @param series The timeseries.
   * @return The variance.
   */
  public static double var(double[] series) {
    double res = 0D;
    double mean = mean(series);
    if (Double.isNaN(mean) || Double.isInfinite(mean)) {
      return Double.NaN;
    }
    int count = 0;
    for (double tp : series) {
      if (Double.isNaN(tp) || Double.isInfinite(tp)) {
        continue;
      }
      else {
        res += (tp - mean) * (tp - mean);
        count += 1;
      }
    }
    if (count > 0) {
      return res / ((Integer) (count - 1)).doubleValue();
    }
    return Double.NaN;
  }

  /**
   * Computes the standard deviation of timeseries.
   * 
   * @param series The timeseries.
   * @return the standard deviation.
   */
  /**
   * Computes the standard deviation of timeseries.
   * 
   * @param series The timeseries.
   * @return the standard deviation.
   */
  public static double stDev(double[] series) {
    double num0 = 0D;
    double sum = 0D;
    int count = 0;
    for (double tp : series) {
      if (Double.isNaN(tp) || Double.isInfinite(tp)) {
        continue;
      }
      else {
        num0 = num0 + tp * tp;
        sum = sum + tp;
        count += 1;
      }
    }
    if (count > 0) {
      double len = ((Integer) count).doubleValue();
      return Math.sqrt((len * num0 - sum * sum) / (len * (len - 1)));
    }
    return Double.NaN;
  }

  /**
   * Speed-optimized implementation.
   * 
   * @param series The timeseries.
   * @return the standard deviation.
   */
  public static double optimizedStDev(double[] series) {
    double num0 = 0D;
    double sum = 0D;
    int count = 0;
    for (double tp : series) {
      num0 = num0 + tp * tp;
      sum = sum + tp;
      count += 1;
    }
    double len = ((Integer) count).doubleValue();
    return Math.sqrt((len * num0 - sum * sum) / (len * (len - 1)));
  }

  public static boolean isCoveried(core.gi.RuleInterval x1, core.gi.RuleInterval x2)
  {
	  double t=0;
	  if(x1.getLength()>x2.getLength())
	  {
		  double a=Math.min(x1.getStart(), x2.getStart());
		  double b=Math.max(x1.getEnd(), x2.getEnd());
		  t=x2.getLength()/(b-a);
	  }
	  else
	  {
		  double a=Math.min(x1.getStart(), x2.getStart());
		  double b=Math.max(x1.getEnd(), x2.getEnd());
		  t=x1.getLength()/(b-a);
	  }
	  
	  if(t>BESTFACT)
		  return true;
	  return false;
  }
  
  public static double Coverage(core.gi.RuleInterval x1, core.gi.RuleInterval x2)
  {
	  double t=0;
	  if(x1.getLength()>x2.getLength())
	  {
		  int a=Math.max(x1.getStart(), x2.getStart());
		  int b=Math.max(x1.getEnd(), x2.getEnd());
		  t=x2.getLength()/(b-a);
	  }
	  else
	  {
		  int a=Math.max(x1.getStart(), x2.getStart());
		  int b=Math.max(x1.getEnd(), x2.getEnd());
		  t=x1.getLength()/(b-a);
	  }
	  return t;
  }
  public static boolean isCovering(core.gi.RuleInterval x1, core.gi.RuleInterval x2) {
		// TODO Auto-generated method stub
		if(x1.getLength()>x2.getLength())
  		return isCovering(x1.getStart(),x1.getEnd(),x1.getLength(),x2.getStart(),x2.getEnd(),x2.getLength());
  	else
  		return isCovering(x2.getStart(),x2.getEnd(),x2.getLength(),x1.getStart(),x1.getEnd(),x1.getLength());
	}


	public ArrayList<Double> hist(AGrammarRules rules)
	{
		ArrayList<Double> a=new ArrayList<Double>();
		for(AGrammarRuleRecord x : rules)
		{
			if(x.err()>15)
				continue;
			ArrayList<RuleInterval> y = x.getRuleintervels();
			Integer l=70000;
			for(int i=0;i<y.size();i++)
			{
				RuleInterval x1=y.get(i);
				for(int j=i+1;j<y.size();j++)
				{
					RuleInterval x2=y.get(j);
					
					if(!isCovering(x1,x2));
					{
						Integer tmp=Math.min(x1.getLength(),x2.getLength());
						if(tmp<l)
							l=tmp;
					}
				}
				
			}
			if(l<1500)
				a.add((double)l);
		}
		return a;
	}
	
  public static boolean isCovering(RuleInterval x1, RuleInterval x2) {
		// TODO Auto-generated method stub
  	if(x1.getLength()>x2.getLength())
  		return isCovering(x1.getStart(),x1.getEnd(),x1.getLength(),x2.getStart(),x2.getEnd(),x2.getLength());
  	else
  		return isCovering(x2.getStart(),x2.getEnd(),x2.getLength(),x1.getStart(),x1.getEnd(),x1.getLength());
	}


	public static boolean isCovering(Integer largeLoc1, Integer largeLoc2, int largeLength, Integer smallLoc1, Integer smallLoc2, int smallLength)
  {
      //assumes loc1 < loc2

      double c = 0.05;
      if (largeLoc1 - smallLoc1 >= 0 && largeLoc1 - smallLoc1 < (1 - c) * smallLength)
          return true;
      else if (largeLength - smallLoc1 + largeLoc1 > c * smallLength && largeLength - smallLoc1 + largeLoc1 <= largeLength)
          return true;
      else if (largeLoc2 - smallLoc2 >= 0 && largeLoc2 - smallLoc2 < (1 - c) * smallLength)
          return true;
      else if (largeLength - smallLoc2 + largeLoc2 > c * smallLength && largeLength - smallLoc2 + largeLoc2 <= largeLength)
          return true;
      else if (largeLoc1 - smallLoc2 >= 0 && largeLoc1 - smallLoc2 < (1 - c) * smallLength)
          return true;
      else if (largeLength - smallLoc2 + largeLoc1 > c * smallLength && largeLength - smallLoc2 + largeLoc1 <= largeLength)
          return true;
      else if (largeLoc1 - smallLoc2 >= 0 && largeLoc1 - smallLoc1 < (1 - c) * smallLength)
          return true;
      else if (largeLength - smallLoc2 + largeLoc1 > c * smallLength && largeLength - smallLoc2 + largeLoc1 <= largeLength)
          return true;

      else
          return false;
  }
	
	public static int DataConvert(long unixSeconds)
	{
		Date date = new Date(unixSeconds*1000); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-7")); // give a timezone reference for formating (see comment at the bottom
		String formattedDate = sdf.format(date);
		String hour=formattedDate.substring(11, 13);
		System.out.println(formattedDate);
		return Integer.parseInt(hour);
	}
	
	 public static double distanceEucidean(double[] TS, Integer i, Integer k, Integer length)
	 {
		return length;
	  }
	 public static double distance(double[] TS, Integer i, Integer k, Integer length)
	    {
	        double xy = 0, x = 0, y = 0, x2 = 0, y2 = 0;
	        for (int ii = 0; ii < length; ii++)
	        {
	            xy += TS[ii + i] * TS[ii + k];
	            x += TS[ii + i];
	            x2 += TS[ii + i] * TS[ii + i];

	            y += TS[ii + k];
	            y2 += TS[ii + k] * TS[ii + k];

	        }


	        double meanX = x / length;
	        double sigmaX = Math.sqrt((x2 / length) - (meanX * meanX));
	        double meanY = y / length;
	        double sigmaY = Math.sqrt((y2 / length) - (meanY * meanY));


	        //Compute Distance
	        double corr = (xy - (length * meanX * meanY)) / (length * sigmaX * sigmaY);
	        double dis = Math.sqrt(2 * length * (1 - (float)corr));
	        count++;
	        return dis;

	    }
	 
  public static double distance(double[] x,double[] y)
  {
		double bsf=999999;
      double sum = 0;
      double bsf2 = bsf * bsf;
      int j;
      for (j = 0; j < x.length && sum <= bsf2; j++)
      {
          sum += (x[j] -y[j]) * (x[j] -y[j]);

      }
      return Math.sqrt(sum)/x.length;
  }
      
  public static double pack(double[] x,double[] y)
  {
		double bsf=999999;
      double sum = 0;
      double bsf2 = bsf * bsf;
      int j;
      for (j = 0; j < x.length && sum <= bsf2; j++)
      {
          if(sum<Math.abs(x[j]-y[j]))
        	  sum=Math.abs(x[j]-y[j]);
      }
      return sum;
  }
  
	
	
	public static double compareToDist(double[] ts, Integer loc1, Integer loc2, int k) {
		// TODO Auto-generated method stub
		//double[] X1=tp.znorm(tp.subseriesByCopy(ts, x.loc1, x.loc1+k),0.05);
		//double[] X2=tp.znorm(tp.subseriesByCopy(ts, x.loc2, x.loc2+k),0.05);
		//double d1=distance(X1,X2);
		if(loc1+k>ts.length || loc2+k>ts.length)
			return 1;
		double[] Y1=znorm(TSUtils.subseriesByCopy(ts, loc1, loc1+k),0.05);
		double[] Y2=znorm(TSUtils.subseriesByCopy(ts, loc2, loc2+k),0.05);
		double d2=distance(Y1,Y2);
		
		return d2;
	}
	
  /**
   * Z-Normalize timeseries to the mean zero and standard deviation of one.
   * 
   * @param series The timeseries.
   * @return Z-normalized time-series.
   * @throws TSException if error occurs.
   * @throws CloneNotSupportedException
   */

  /**
   * Z-Normalize timeseries to the mean zero and standard deviation of one.
   * 
   * @param series The timeseries.
   * @return Z-normalized time-series.
   * @throws TSException if error occurs.
   */
  public static double[] zNormalize(double[] series) throws Exception {

    // this is the resulting normalization
    //
    double[] res = new double[series.length];

    // get mean and sdev, NaN's will be handled
    //
    double mean = mean(series);
    double sd = stDev(series);

    // check if we hit special case, where something got NaN
    //
    if (Double.isInfinite(mean) || Double.isNaN(mean) || Double.isInfinite(sd) || Double.isNaN(sd)) {

      // case[1] single value within the timeseries, normalize this value to 1.0 - magic number
      //
      int nanNum = countNaN(series);
      if ((series.length - nanNum) == 1) {
        for (int i = 0; i < res.length; i++) {
          if (Double.isInfinite(series[i]) || Double.isNaN(series[i])) {
            res[i] = Double.NaN;
          }
          else {
            res[i] = 1.0D;
          }
        }
      }

      // case[2] all values are NaN's
      //
      else if (series.length == nanNum) {
        for (int i = 0; i < res.length; i++) {
          res[i] = Double.NaN;
        }
      }
    }

    // another special case, where SD happens to be close to a zero, i.e. they all are the same for
    // example
    //
    else if (sd <= 0.01D) {

      // here I assign another magic value - 0.001D which makes to middle band of the normal
      // Alphabet
      //
      for (int i = 0; i < res.length; i++) {
    	  res[i]=(series[i]-mean);
      }
    }

    // normal case, everything seems to be fine
    //
   
    else {
      // sd and mean here, - go-go-go
    	
      for (int i = 0; i < res.length; i++) {
        res[i] = (series[i] - mean) / sd;
      }
    }
    return res;

  }

  /**
   * Speed-optimized Z-Normalize routine, doesn't care about normalization threshold.
   * 
   * @param series The timeseries.
   * @param normalizationThreshold
   * @return Z-normalized time-series.
   * @throws TSException if error occurs.
   */
  public static double[] optimizedZNorm(double[] series, double normalizationThreshold) {
    double[] res = new double[series.length];
    double mean = optimizedMean(series);
    double sd = optimizedStDev(series);
    if (sd < normalizationThreshold) {
      return series.clone();
    }
    for (int i = 0; i < res.length; i++) {
      res[i] = (series[i] - mean) / sd;
    }
    return res;
  }

  /**
   * Approximate the timeseries using PAA. If the timeseries has some NaN's they are handled as
   * follows: 1) if all values of the piece are NaNs - the piece is approximated as NaN, 2) if there
   * are some (more or equal one) values happened to be in the piece - algorithm will handle it as
   * usual - getting the mean.
   * 
   * @param ts The timeseries to approximate.
   * @param paaSize The desired length of approximated timeseries.
   * @return PAA-approximated timeseries.
   * @throws TSException if error occurs.
   * @throws CloneNotSupportedException if error occurs.
   */

      //
      // now, here is a new trick comes in game - because we have so many
      // "lost" values
      // PAA game rules will change - we will omit NaN values and put NaNs
      // back to PAA series
      //
      //
      // this is the old line of code here:
      // double[] newVals = MatrixFactory.colMeans(res);
      //
      // i will need to test this though
      //
      //

  /**
   * Approximate the timeseries using PAA. If the timeseries has some NaN's they are handled as
   * follows: 1) if all values of the piece are NaNs - the piece is approximated as NaN, 2) if there
   * are some (more or equal one) values happened to be in the piece - algorithm will handle it as
   * usual - getting the mean.
   * 
   * @param ts The timeseries to approximate.
   * @param paaSize The desired length of approximated timeseries.
   * @return PAA-approximated timeseries.
   * @throws TSException if error occurs.
   */
  public static double[] paa(double[] ts, int paaSize) throws Exception {
    // fix the length
    int len = ts.length;
    // check for the trivial case
    if (len == paaSize) {
      return Arrays.copyOf(ts, ts.length);
    }
    else {
      // get values and timestamps
      double[][] vals = asMatrix(ts);
      // work out PAA by reshaping arrays
      double[][] res;
      if (len % paaSize == 0) {
        res = MatrixFactory.reshape(vals, len / paaSize, paaSize);
      }
      else {
        double[][] tmp = new double[paaSize][len];
        for (int i = 0; i < paaSize; i++) {
          for (int j = 0; j < len; j++) {
            tmp[i][j] = vals[0][j];
          }
        }
        double[][] expandedSS = MatrixFactory.reshape(tmp, 1, len * paaSize);
        res = MatrixFactory.reshape(expandedSS, len, paaSize);
      }
      double[] newVals = MatrixFactory.colMeans(res);

      return newVals;
    }

  }

  /**
   * Approximate the timeseries using PAA. If the timeseries has some NaN's they are handled as
   * follows: 1) if all values of the piece are NaNs - the piece is approximated as NaN, 2) if there
   * are some (more or equal one) values happened to be in the piece - algorithm will handle it as
   * usual - getting the mean.
   * 
   * @param ts The timeseries to approximate.
   * @param paaSize The desired length of approximated timeseries.
   * @return PAA-approximated timeseries.
   * @throws TSException if error occurs.
   */
  public static double[] optimizedPaa(double[] ts, int paaSize) throws Exception {
    // fix the length
    int len = ts.length;
    // check for the trivial case
    if (len == paaSize) {
      return Arrays.copyOf(ts, ts.length);
    }
    else {
      if (len % paaSize == 0) {
        return MatrixFactory.colMeans(MatrixFactory.reshape(asMatrix(ts), len / paaSize, paaSize));
      }
      else {
        double[] paa = new double[paaSize];
        for (int i = 0; i < len * paaSize; i++) {
          int idx = i / len; // the spot
          int pos = i / paaSize; // the col spot
          paa[idx] = paa[idx] + ts[pos];
        }
        for (int i = 0; i < paaSize; i++) {
          paa[i] = paa[i] / (double) len;
        }
        return paa;
      }
    }

  }

  /**
   * Converts a timeseries into the string using alphabet cuts.
   * 
   * @param series The timeseries to convert.
   * @param alphabet The alphabet to use.
   * @param alphabetSize The alphabet size.
   * @return Symbolic (SAX) representation of timeseries.
   * @throws TSException if error occurs.
   */
  

  /**
   * Converts the timeseries into string using given cuts intervals. Useful for not-normal
   * distribution cuts.
   * 
   * @param vals The timeseries.
   * @param cuts The cut intervals.
   * @return The timeseries SAX representation.
   */
  public static char[] ts2String(double[] vals, double[] cuts) {
    char[] res = new char[vals.length];
    for (int i = 0; i < vals.length; i++) {
      res[i] = num2char(vals[i], cuts);
    }
    
    return res;
  }

  public static String ts2StringS(double[] vals, double[] cuts) {
	    String[] res = new String[vals.length];
	    String s="";
	    for (int i = 0; i < vals.length; i++) {
	      res[i] = num2grid(vals[i], cuts);
	      s=s+"x"+res[i];
	    }
	    
	    return s;
	  }
  /**
   * Converts a timeseries into the string paying attention to NaN values.
   * 
   * @param series The timeseries to convert.
   * @param alphabet The alphabet to use.
   * @param alphabetSize The alphabet size in use.
   * @return SAX representation of timeseries.
   * @throws TSException if error occurs.
   */

  /**
   * Converts a timeseries into the string paying attention to NaN values.
   * 
   * @param series The timeseries to convert.
   * @param cuts The cuts for alphabet.
   * @return SAX representation of timeseries.
   * @throws TSException if error occurs.
   */

  /**
   * Get mapping of a number to char.
   * 
   * @param value the value to map.
   * @param cuts the array of intervals.
   * @return character corresponding to numeric value.
   */
  public static char num2char(double value, double[] cuts) {
    int count = 0;
    while ((count < cuts.length) && (cuts[count] <= value)) {
      count++;
    }
    return ALPHABET[count];
  }

  
  public static String num2grid(double value, double[] cuts) {
	    Integer count = 0;
	    while ((count < cuts.length) && (cuts[count] <= value)) {
	      count++;
	    }
	    return count.toString();
	  }
  
  /**
   * Converts index into char.
   * 
   * @param idx The index value.
   * @return The char by index.
   */
  public static char num2char(int idx) {
    return ALPHABET[idx];
  }

  /**
   * Convert the timeseries into the index using SAX cuts.
   * 
   * @param series The timeseries to convert.
   * @param alphabet The alphabet to use.
   * @param alphabetSize The alphabet size in use.
   * @return SAX representation of timeseries.
   * @throws TSException if error occurs.
   */

  /**
   * Get mapping of number to cut index.
   * 
   * @param value the value to map.
   * @param cuts the array of intervals.
   * @return character corresponding to numeric value.
   */
  public static int num2index(double value, double[] cuts) {
    int count = 0;
    while ((count < cuts.length) && (cuts[count] <= value)) {
      count++;
    }
    return count;
  }

  /**
   * Counts the number of NaNs' in the timeseries.
   * 
   * @param series The timeseries.
   * @return The count of NaN values.
   */
  public static int countNaN(double[] series) {
    int res = 0;
    for (double d : series) {
      if (Double.isInfinite(d) || Double.isNaN(d)) {
        res += 1;
      }
    }
    return res;
  }

  /**
   * Counts the number of NaNs' in the timeseries.
   * 
   * @param series The timeseries.
   * @return The count of NaN values.
   */

  /**
   * Converts the vector into one-row matrix.
   * 
   * @param vector The vector.
   * @return The matrix.
   */
  public static double[][] asMatrix(double[] vector) {
    double[][] res = new double[1][vector.length];
    for (int i = 0; i < vector.length; i++) {
      res[0][i] = vector[i];
    }
    return res;
  }

  /**
   * Extract subseries out of series.
   * 
   * @param series The series array.
   * @param start Start position
   * @param length Length of subseries to extract.
   * @return The subseries.
   * @throws IndexOutOfBoundsException If error occurs.
   */
  public static double[] subseries(double[] series, int start, int length)
      throws IndexOutOfBoundsException {
    if (start + length > series.length) {
      throw new IndexOutOfBoundsException("Unable to extract subseries, series length: "
          + series.length + ", start: " + start + ", subseries length: " + length);
    }
    double[] res = new double[length];
    for (int i = 0; i < length; i++) {
      res[i] = series[start + i];
    }
    return res;
  }

  /**
   * Extract subseries out of series.
   * 
   * @param series The series array.
   * @param start Start position
   * @param length Length of subseries to extract.
   * @return The subseries.
   * @throws IndexOutOfBoundsException If error occurs.
   */
  public static double[] subseriesByCopy(double[] series, int start, int end)
      throws IndexOutOfBoundsException {
    if ((start > end) || (start < 0) || (end > series.length)) {
      throw new IndexOutOfBoundsException("Unable to extract subseries, series length: "
          + series.length + ", start: " + start + ", end: " + String.valueOf(end - start));
    }
    return Arrays.copyOfRange(series, start, end);
  }

  /**
   * Extract subseries out of series.
   * 
   * @param series The series array.
   * @param start Start position
   * @param length Length of subseries to extract.
   * @return The subseries.
   * @throws IndexOutOfBoundsException If error occurs.
   */
  public static double[] subseries(Double[] series, int start, int length)
      throws IndexOutOfBoundsException {
    if (start + length > series.length) {
      throw new IndexOutOfBoundsException("Unable to extract subseries, series length: "
          + series.length + ", start: " + start + ", subseries length: " + length);
    }
    double[] res = new double[length];
    for (int i = 0; i < length; i++) {
      res[i] = series[start + i];
    }
    return res;
  }

  /**
   * Implements Gaussian smoothing.
   * 
   * @param series Data to process.
   * @param filterWidth The filter width.
   * @return smoothed series.
   * @throws TSException if error occurs.
   */
  public static double[] gaussFilter(double[] series, double filterWidth) throws Exception {

    double[] smoothedSignal = new double[series.length];
    double sigma = filterWidth / 2D;
    int maxShift = (int) Math.floor(4D * sigma); // Gaussian curve is reasonably > 0

    if (maxShift < 1) {
      throw new Exception("NOT smoothing: filter width too small - " + filterWidth);
    }
    for (int i = 0; i < smoothedSignal.length; i++) {
      smoothedSignal[i] = series[i];

      if (maxShift < 1) {
        continue;
      }
      for (int j = 1; j <= maxShift; j++) {

        double gaussFilter = Math.exp(-(j * j) / (2. * sigma * sigma));
        double leftAmpl, rightAmpl;

        // go left
        if ((i - j) >= 0) {
          leftAmpl = series[i - j];
        }
        else {
          leftAmpl = series[i];
        }

        // go right
        if ((i + j) <= smoothedSignal.length - 1) {
          rightAmpl = series[i + j];
        }
        else {
          rightAmpl = series[i];
        }

        smoothedSignal[i] += gaussFilter * (leftAmpl + rightAmpl);

      }

      double normalizingCoef = Math.sqrt(2. * Math.PI) * sigma;
      smoothedSignal[i] /= normalizingCoef;

    }
    return smoothedSignal;
  }

  public double gaussian(double x, double filterWidth) {
    double sigma = filterWidth / 2.;
    return Math.exp(-(x * x) / (2. * sigma * sigma));
  }

  public static String seriesToString(double[] series, NumberFormat df) {
    StringBuffer sb = new StringBuffer();
    sb.append('[');
    for (double d : series) {
      sb.append(df.format(d)).append(',');
    }
    sb.delete(sb.length() - 2, sb.length() - 1).append("]");
    return sb.toString();
  }

  /**
   * Brute force discord search implementation. BRUTE FORCE algorithm.
   * 
   * @param series
   * @param windowSize
   * @param discordCollectionSize
   * @param largeWindowAlgorithm
   * @return
   * @throws TSException
   */
  /**
   * Finds the best discord. BRUTE FORCE algorithm.
   * 
   * @param series
   * @param windowSize
   * @param globalRegistry
   * @param marker
   * @return
   * @throws TSException
   */
  /*
  private static DiscordRecord findBestDiscord(double[] series, Integer windowSize,
      VisitRegistry globalRegistry, LargeWindowAlgorithm marker) throws TSException {

    Date start = new Date();

    long distanceCallsCounter = 0;

    double bestSoFarDistance = -1;
    int bestSoFarPosition = -1;

    // make an array of all subsequences
    //
    int[] locations = globalRegistry.getUnvisited();

    for (int i : locations) { // outer loop

      // check the global visits registry
      if (globalRegistry.isVisited(i, i + windowSize)) {
        continue;
      }

      double[] cw = TSUtils.subseriesByCopy(series, i, i + windowSize);
      double nearestNeighborDistance = Double.MAX_VALUE;

      for (int j = 1; j < series.length - windowSize + 1; j++) { // inner loop

        if (Math.abs(i - j) >= windowSize) {

          double[] currentSubsequence = TSUtils.subseriesByCopy(series, j, j + windowSize);

          double dist = EuclideanDistance.earlyAbandonedDistance(cw, currentSubsequence,
              nearestNeighborDistance);

          distanceCallsCounter++;

          if ((!Double.isNaN(dist)) && dist < nearestNeighborDistance) {
            nearestNeighborDistance = dist;
          }

        }
      }

      if (!(Double.isInfinite(nearestNeighborDistance))
          && nearestNeighborDistance > bestSoFarDistance) {
        bestSoFarDistance = nearestNeighborDistance;
        bestSoFarPosition = i;
      }
    }
    Date firstDiscord = new Date();

    consoleLogger.debug("best discord found at " + bestSoFarPosition + ", best distance: "
        + bestSoFarDistance + ", in "
        + SAXFactory.timeToString(start.getTime(), firstDiscord.getTime()) + " distance calls: "
        + distanceCallsCounter);

    DiscordRecord res = new DiscordRecord(bestSoFarPosition, bestSoFarDistance);
    res.setInfo("distance calls: " + distanceCallsCounter);
    return res;
  }
*/
  /**
   * Generic method to convert the milliseconds into the elapsed time string.
   * 
   * @param start Start timestamp.
   * @param finish End timestamp.
   * @return String representation of the elapsed time.
   */
  public static String timeToString(long start, long finish) {
    long diff = finish - start;

    long secondInMillis = 1000;
    long minuteInMillis = secondInMillis * 60;
    long hourInMillis = minuteInMillis * 60;
    long dayInMillis = hourInMillis * 24;
    long yearInMillis = dayInMillis * 365;

    @SuppressWarnings("unused")
    long elapsedYears = diff / yearInMillis;
    diff = diff % yearInMillis;

    @SuppressWarnings("unused")
    long elapsedDays = diff / dayInMillis;
    diff = diff % dayInMillis;

    // @SuppressWarnings("unused")
    long elapsedHours = diff / hourInMillis;
    diff = diff % hourInMillis;

    long elapsedMinutes = diff / minuteInMillis;
    diff = diff % minuteInMillis;

    long elapsedSeconds = diff / secondInMillis;
    diff = diff % secondInMillis;

    long elapsedMilliseconds = diff % secondInMillis;

    return elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + "s "
        + elapsedMilliseconds + "ms";
  }

  public static double findMax(double[] timeseries) {
		// TODO Auto-generated method stub
		
		
		double max = timeseries[0];
		for (int i=0;i<timeseries.length;i++)
			if (max<timeseries[i])
				max = timeseries[i];
		return max;
	}
	//requires: timeseries.length>0
	public static double findMin(double[] timeseries) {
		// TODO Auto-generated method stub
		
		
		double min = timeseries[0];
		for (int i=0;i<timeseries.length;i++)
			if (min>timeseries[i])
				min = timeseries[i];
		return min;
	}
	public static ArrayList<Double> subseriesByCopy(ArrayList<Double> series, int start,
			int end) {
		// TODO Auto-generated method stub
		if ((start > end) || (start < 0) || (end > series.size())) {
		      throw new IndexOutOfBoundsException("Unable to extract subseries, series length: "
		          + series.size() + ", start: " + start + ", end: " + String.valueOf(end - start));
		    }
		    return (ArrayList<Double>)series.subList(start, end);
		  }
	public static boolean isCoveried(RuleInterval x1, RuleInterval x2) {
		// TODO Auto-generated method stub
		 double t=0;
		  if(x1.getLength()>x2.getLength())
		  {
			  double a=Math.min(x1.getStart(), x2.getStart());
			  double b=Math.max(x1.getEnd(), x2.getEnd());
			  t=x2.getLength()/(b-a);
		  }
		  else
		  {
			  double a=Math.min(x1.getStart(), x2.getStart());
			  double b=Math.max(x1.getEnd(), x2.getEnd());
			  t=x1.getLength()/(b-a);
		  }
		  
		  if(t>BESTFACT)
			  return true;
		  return false;
	}
	
	public static boolean isCoveried(core.gi.RuleInterval x1,
			core.gi.RuleInterval x2, double d) {
		// TODO Auto-generated method stub
		double t=0;
		  if(x1.getLength()>x2.getLength())
		  {
			  double a=Math.min(x1.getStart(), x2.getStart());
			  double b=Math.max(x1.getEnd(), x2.getEnd());
			  t=x2.getLength()/(b-a);
		  }
		  else
		  {
			  double a=Math.min(x1.getStart(), x2.getStart());
			  double b=Math.max(x1.getEnd(), x2.getEnd());
			  t=x1.getLength()/(b-a);
		  }
		  
		  if(t>d)
			  return true;
		  return false;
	}
	public static boolean isCoveried(RuleInterval x1, RuleInterval x2, double d) {
		// TODO Auto-generated method stub
		double t=0;
		  if(x1.getLength()>x2.getLength())
		  {
			  double a=Math.min(x1.getStart(), x2.getStart());
			  double b=Math.max(x1.getEnd(), x2.getEnd());
			  t=x2.getLength()/(b-a);
		  }
		  else
		  {
			  double a=Math.min(x1.getStart(), x2.getStart());
			  double b=Math.max(x1.getEnd(), x2.getEnd());
			  t=x1.getLength()/(b-a);
		  }
		  
		  if(t>d)
			  return true;
		  return false;
	}
	public static ArrayList<Double> subseriesByCopy2Arr(double[] ts, int start,
			int end) {
		// TODO Auto-generated method stub
		double[] t2=subseriesByCopy(ts,start,end);
		ArrayList<Double> tmp=new ArrayList<Double>();
		for(int i=0;i<t2.length;i++)
			tmp.add(t2[i]);
		
		return tmp;
	}
	public static double distance(double[] x, double[] y, int start, int start2, int length) {
		// TODO Auto-generated method stub
		double tmp[]=new double[length];
		double tmp2[]=new double[length];
		double tmp3[]=new double[length];
		double tmp4[]=new double[length];
		
		
		for(int j=start;j<start+length;j++)
			tmp[j-start]=x[j];
		for(int j=start2;j<start2+length;j++)
			tmp2[j-start2]=x[j];
		
		for(int j=start;j<start+length;j++)
			tmp3[j-start]=y[j];
		for(int j=start2;j<start2+length;j++)
			tmp4[j-start2]=y[j];
		double sum=0;
		for(int i=0;i<tmp.length;i++)
		{
			sum=sum+(tmp[i]-tmp2[i])*(tmp[i]-tmp2[i])+(tmp3[i]-tmp4[i])*(tmp3[i]-tmp4[i]);
		}
		
		return sum/length;
	}
}
