package test;

import java.io.BufferedWriter;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import core.gi.GrammarRuleRecord;
import core.gi.GrammarRules;
import core.gi.RuleInterval;
import test.SAXRule;
import test.SAXSymbol;
import test.SAXTerminal;
import sax.NumerosityReductionStrategy;
import test.TSProcessor;
import base.GIHelper;

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

  private static TSProcessor tp = new TSProcessor();

  // logging stuff
  //
  

  /**
   * Disabling the constructor.
   */
  private SequiturFactory() {
    assert true;
  }

  /**
   * Digests a string of symbols separated by space.
   * 
   * @param inputString The string to digest. Symbols expected to be separated by space.
   * 
   * @return The top rule handler.
   * @throws Exception
   */
  public static SAXRule runSequitur(String inputString) throws Exception {

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
    while (st.hasMoreTokens()) {

      String token = st.nextToken();
      // System.out.println("  processing the token " + token);

      // extract next token
      SAXTerminal symbol = new SAXTerminal(token, currentPosition);

      // append to the end of the current sequitur string
      // ... As each new input symbol is observed, append it to rule S....
      resRule.last().insertAfter(symbol);

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


  
  
  /*

  public static int[] series2RulesDensity(double[] originalTimeSeries, int saxWindowSize,
      int saxPaaSize, int saxAlphabetSize) throws Exception, IOException {

    SAXRecords saxFrequencyData = new SAXRecords();

    SAXRule.numRules = new AtomicInteger(0);
    SAXRule.theRules.clear();
    SAXSymbol.theDigrams.clear();
    SAXSymbol.theSubstituteTable.clear();
    SAXRule.arrRuleRecords = new ArrayList<GrammarRuleRecord>();

    SAXRule grammar = new SAXRule();

    String previousString = "";

    // scan across the time series extract sub sequences, and convert
    // them to strings
    int stringPosCounter = 0;
    for (int i = 0; i < originalTimeSeries.length - (saxWindowSize - 1); i++) {

      // fix the current subsection
      double[] subSection = Arrays.copyOfRange(originalTimeSeries, i, i + saxWindowSize);

      // Z normalize it
      subSection = tp.znorm(subSection, NORMALIZATION_THRESHOLD);

      // perform PAA conversion if needed
      double[] paa = tp.paa(subSection, saxPaaSize);

      // Convert the PAA to a string.
      char[] currentString = tp.ts2String(paa, normalA.getCuts(saxAlphabetSize));

      // NumerosityReduction
      if (!previousString.isEmpty()
          && previousString.equalsIgnoreCase(String.valueOf(currentString))) {
        continue;
      }

      previousString = String.valueOf(currentString);

      grammar.last().insertAfter(new SAXTerminal(String.valueOf(currentString), stringPosCounter));
      grammar.last().p.check();

      saxFrequencyData.add(currentString, i);

      stringPosCounter++;

    }

    saxFrequencyData.buildIndex();

    GrammarRules rules = grammar.toGrammarRulesData();

    SequiturFactory.updateRuleIntervals(rules, saxFrequencyData, true, originalTimeSeries,
        saxWindowSize, saxPaaSize);

    int[] coverageArray = new int[originalTimeSeries.length];

    for (GrammarRuleRecord r : rules) {
      if (0 == r.ruleNumber()) {
        continue;
      }

      ArrayList<RuleInterval> arrPos = r.getRuleInterval();
      for (RuleInterval saxPos : arrPos) {
        int startPos = saxPos.getStart();
        int endPos = saxPos.getEnd();
        for (int j = startPos; j < endPos; j++) {
          coverageArray[j] = coverageArray[j] + 1;
        }
      }
    }

    return coverageArray;
  }
*/


  
  
  
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