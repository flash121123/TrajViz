package edu.gmu.trajviz.gi.sequitur;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.gmu.trajviz.gi.GrammarRuleRecord;
import edu.gmu.trajviz.logic.NumerosityReductionMapEntry;
import edu.gmu.trajviz.logic.RuleInterval;
import edu.gmu.trajviz.model.SequiturModel;
import edu.gmu.trajviz.gi.GrammarRules;
//import edu.hawaii.jmotif.logic.RuleInterval;
//import edu.hawaii.jmotif.sax.NumerosityReductionStrategy;
//import edu.hawaii.jmotif.sax.SAXFactory;
//import edu.hawaii.jmotif.sax.alphabet.NormalAlphabet;
import edu.gmu.trajviz.sax.datastructures.SAXRecords;
import edu.gmu.trajviz.timeseries.TSException;
//import edu.gmu.trajviz.timeseries.TSUtils;

/**
 * Sort of a stand-alone factory to digesting strings with Sequitur.
 * 
 * @author psenin
 * 
 */
public final class SequiturFactory {

  /** Chunking/Sliding switch action key. */
  protected static final String USE_SLIDING_WINDOW_ACTION_KEY = "sliding_window_key";

  private static final double NORMALIZATION_THRESHOLD = 0.5D;

 // private static final NormalAlphabet normalA = new NormalAlphabet();

  // logging stuff
  //
  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;
  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(SequiturFactory.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  /**
   * Disabling the constructor.
   */
  private SequiturFactory() {
    assert true;
  }
  /*
   * This a SAXRecords adapter,
   * Given a string,return in types of SAXRecords, no PAA used
   *  -qz
   */
  public static SAXRecords entries2SAXRecords(ArrayList<NumerosityReductionMapEntry> entry ){
	  SAXRecords saxFrequencyData = new SAXRecords();
	  
	  for (int i = 0; i<entry.size(); i++){
		  saxFrequencyData.add(entry.get(i).getValue().toString(), (Integer)entry.get(i).getKey());
	  }
	  return saxFrequencyData;
  }
  /**
   * Digests a string of symbols separated by space.
   * 
   * @param inputString The string to digest. Symbols expected to be separated by space.
   * 
   * @return The top rule handler.
   * @throws TSException
   */
  public static SAXRule runSequitur(String inputString) throws TSException {

    consoleLogger.trace("digesting the string " + inputString);
    System.out.println("digesting the string " + inputString);
    // clear global collections
    //
    SAXRule.numRules = new AtomicInteger(0);
    SAXRule.theRules.clear();
    SAXSymbol.theDigrams.clear();
    SAXSymbol.theSubstituteTable.clear();

    // init the top-level rule
    //
    SAXRule resRule = new SAXRule();

    // tokenize the input string
    //
    StringTokenizer st = new StringTokenizer(inputString, " ");

    // while there are tokens
    int currentPosition = 0;
    //int line = 0;
    while (st.hasMoreTokens()) {
    
      String token = st.nextToken();
      // System.out.println("  processing the token " + token);
    // System.out.println("line:"+line+":"+token);
      //line++;
      // extract next token
      SAXTerminal symbol = new SAXTerminal(token, currentPosition);

      // append to the end of the current sequitur string
      // ... As each new input symbol is observed, append it to rule S....
      resRule.last().insertAfter(symbol);
   //   System.out.println(sym);
      // once appended, check if the resulting digram is new or recurrent
      //
      // ... Each time a link is made between two symbols if the new digram is repeated elsewhere
      // and the repetitions do not overlap, if the other occurrence is a complete rule,
      // replace the new digram with the non-terminal symbol that heads the rule,
      // otherwise,form a new rule and replace both digrams with the new non-terminal symbol
      // otherwise, insert the digram into the index...
    
      resRule.last().p.check();
            currentPosition++;

      // consoleLogger.debug("Current grammar:\n" + SAXRule.getRules());
    }

    return resRule;
  }
public static void updateRuleIntervals(GrammarRules rules,
		SAXRecords saxFrequencyData, int originalLength ) {
	ArrayList<Integer> saxWordsIndexes = new ArrayList<Integer>(saxFrequencyData.getAllIndices());
	//ArrayList<Integer> indexesInR0 = new ArrayList<Integer>(saxFrequencyData.getAllIndices());
//	System.out.println("saxwordsIndexes: "+saxWordsIndexes);
	for (GrammarRuleRecord ruleContainer : rules) {
	//	System.out.println("minmaxLenth: "+ruleContainer.minMaxLengthAsString());
	//	System.out.println("ruleIntervals: "+ruleContainer.getRuleIntervals());
	      // here we construct the array of rule intervals
	      ArrayList<RuleInterval> resultIntervals = new ArrayList<RuleInterval>();
	      ArrayList<RuleInterval> r0Intervals = new ArrayList<RuleInterval>();
	      // array of all words of this rule expanded form
	      // String[] expandedRuleSplit = ruleContainer.getExpandedRuleString().trim().split(" ");
	     
	      int expandedRuleLength = recursiveCountSpaces(ruleContainer.getExpandedRuleString());
	//      System.out.println("getExStr: "+ruleContainer.getExpandedRuleString());
     
	      // the auxiliary array that keeps lengths of all rule occurrences
	      int[] lengths = new int[ruleContainer.getOccurrences().size()];
	      int[] r0Lengths = new int [ruleContainer.getR0Occurrences().size()];
	      int lengthCounter = 0;
	      int r0LengthCounter = 0 ;
	      // iterate over all occurrences of this rule
	      // the currentIndex here is the position of the rule in the input string
	      //
	      for (Integer currentIndex : ruleContainer.getOccurrences()) {

	        // System.out.println("Index: " + currentIndex);
	        // String extractedStr = "";

	        // what we do here is to extract the positions of sax words in the real time-series
	        // by using their positions at the input string
	        //
	        // int[] extractedPositions = new int[expandedRuleSplit.length];
	        // for (int i = 0; i < expandedRuleSplit.length; i++) {
	        // extractedStr = extractedStr.concat(" ").concat(
	        // saxWordsToIndexesMap.get(saxWordsIndexes.get(currentIndex + i)));
	        // extractedPositions[i] = saxWordsIndexes.get(currentIndex + i);
	        // }

	        int startPos = saxWordsIndexes.get(currentIndex);
	        int endPos;
	        
	        
	        
	        /* modified here to adapt multiple runs  -qz on 11022015
	         * */
	        if (currentIndex+expandedRuleLength>=saxWordsIndexes.size())
	        	endPos = originalLength-1;
	        else
	        	endPos = saxWordsIndexes.get(currentIndex+expandedRuleLength)-1;
	        	
	        	
	        
	        
	       // System.out.println("expandedRuleLength: "+expandedRuleLength);
	        /*
	        if ((currentIndex + expandedRuleLength) >= saxWordsIndexes.size()) {
	          endPos = originalLength - 1;
	        }
	        else {
	        	endPos = Long.valueOf(Math.round(startPos + expandedRuleLength)).intValue();
	        	/*
	        	double step = (double) originalLength;
	           // double step = (double) originalLength / (double) saxPAASize;
	            endPos = Long.valueOf(Math.round(startPos + expandedRuleLength * step)).intValue();
	          
	        }
*/
	        resultIntervals.add(new RuleInterval(startPos, endPos));

	        lengths[lengthCounter] = endPos - startPos;
	        lengthCounter++;
	      }
	      
	      /*
	       * add by qingzhe to set rule intervals in R0
	       * 
	       */
	      for (Integer r0Index : ruleContainer.getR0Occurrences()) {

		        // System.out.println("Index: " + currentIndex);
		        // String extractedStr = "";

		        // what we do here is to extract the positions of sax words in the real time-series
		        // by using their positions at the input string
		        //
		        // int[] extractedPositions = new int[expandedRuleSplit.length];
		        // for (int i = 0; i < expandedRuleSplit.length; i++) {
		        // extractedStr = extractedStr.concat(" ").concat(
		        // saxWordsToIndexesMap.get(saxWordsIndexes.get(currentIndex + i)));
		        // extractedPositions[i] = saxWordsIndexes.get(currentIndex + i);
		        // }

		        int startPos = saxWordsIndexes.get(r0Index);
		        int endPos;
		        
		        
		        
		        /* modified here to adapt multiple runs  -qz on 11022015
		         * */
		        if (r0Index+expandedRuleLength>=saxWordsIndexes.size())
		        	endPos = originalLength-1;
		        else
		        	endPos = saxWordsIndexes.get(r0Index+expandedRuleLength)-1;
		        	
		        	
		        
		    
		        r0Intervals.add(new RuleInterval(startPos, endPos));

		        r0Lengths[r0LengthCounter] = endPos - startPos;
		        r0LengthCounter++;
		      }
	      
	      
	      if (0 == ruleContainer.getRuleNumber()) {
	        resultIntervals.add(new RuleInterval(0, originalLength - 1));
	        r0Intervals.add(new RuleInterval(0, originalLength -1));
	        lengths = new int[1];
	        lengths[0] = originalLength;
	      }
	      ruleContainer.setRuleIntervals(resultIntervals);
	      ruleContainer.setR0Intervals(r0Intervals);
	      ruleContainer.setMeanLength(lengths);
	      ruleContainer.setMinMaxLength(lengths);
	  //    System.out.println("minmaxLenth: "+ruleContainer.minMaxLengthAsString());
	  //    System.out.println("ruleIntervals: "+ruleContainer.getRuleIntervals());
         
          }

}

private static int recursiveCountSpaces(String expandedRuleString) {
	
	
	
	return 0;
}

private String parseRule(String string) {
	StringBuffer sb = new StringBuffer();
	System.out.println("string: "+string);
	ArrayList<String> sa = new ArrayList<String>();
	String[] stringArray = string.split(" ");
	for (String s:stringArray){
		if (s.charAt(0)=='I')
		{
			if(s.contains("r")){
				int rIndex = s.indexOf("r");
				Integer iteration = Integer.valueOf(s.substring(1, rIndex));
				Integer rule = Integer.valueOf(s.substring(rIndex+1));
				System.out.println("s: "+s+" iteration: "+iteration+" rule: "+rule);
				String subRule = parseRule(SequiturModel.allRules.get(iteration).get(rule).getExpandedRuleString());
				sa.add(subRule);
				System.out.println(s+" = "+subRule );
				
			}
			else if(s.contains("C")){
				int cIndex = s.indexOf("C");
				Integer iteration = Integer.valueOf(s.substring(1, cIndex));
				Integer cluster = Integer.valueOf(s.substring(cIndex+1));
				System.out.println("s: "+s+" iteration: "+iteration+" cluster: "+cluster);
				Integer ruleInCluster = (Integer)SequiturModel.allClusters.get(iteration).get(cluster).toArray()[0];
				String subRule = parseRule(SequiturModel.allRules.get(iteration).get(ruleInCluster).getExpandedRuleString());
				sa.add(subRule);
				System.out.println(s+" = "+subRule );

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
	System.out.println("sb: "+sb.toString());
	return sb.toString();
}
/**
 * Counts spaces in the string.
 * 
 * @param str The string.
 * @return The number of spaces.
 */
private static int countSpaces(String str) {
  int counter = 0;
  for (int i = 0; i < str.length(); i++) {
    if (str.charAt(i) == ' ') {
      counter++;
    }
  }
  return counter;
}

}
