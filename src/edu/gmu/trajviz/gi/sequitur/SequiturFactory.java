package edu.gmu.trajviz.gi.sequitur;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.gmu.trajviz.logic.NumerosityReductionMapEntry;
//import edu.hawaii.jmotif.logic.RuleInterval;
//import edu.hawaii.jmotif.sax.NumerosityReductionStrategy;
//import edu.hawaii.jmotif.sax.SAXFactory;
//import edu.hawaii.jmotif.sax.alphabet.NormalAlphabet;
import edu.gmu.trajviz.sax.datastructures.SAXRecords;
import edu.gmu.trajviz.timeseries.TSException;
//import edu.gmu.trajviz.timeseries.TSUtils;

/**
 * a stand-alone factory to analyze discretized trajectory data with ItrSequitur. 
 * Develop under GrammarViz framework developed by psenin
 * 
 * @author ygao12,Qingzhe Li, psenin
 * 
 */
public final class SequiturFactory {

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

}
