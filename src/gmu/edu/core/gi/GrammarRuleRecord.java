package gmu.edu.core.gi;

import java.util.ArrayList;


import java.util.Arrays;

import edu.gmu.core.agi.AGrammarRuleRecord;
import gmu.edu.core.Collections.AWordList;
import gmu.edu.core.gi.RuleInterval;

/**
 * Data container for SAX rules. Provides an abstraction which is used for transferring grammars
 * from various algorithms to front-end GUI.
 * 
 * @author Manfred Lerner, seninp
 * 
 */
public class GrammarRuleRecord implements Cloneable{

  /* The rule number in Sequitur grammar. */
  protected int ruleNumber;

  /* The rule string, this may contain non-terminal symbols. */
  protected String ruleString;

  /* The expanded rule string, this contains only terminal symbols. */
  protected String expandedRuleString;

  /* The indexes at which the rule occurs in the discretized time series. */
  protected ArrayList<Integer> timeSeriesOccurrenceIndexes = new ArrayList<Integer>();

  /* This rule intervals on the original time series. */
  private ArrayList<RuleInterval> ruleIntervals;

  /* The rule use frequency - how many time that rule is used by other rules. */
  protected int ruleUsageFrequency;

  /* The rule level in the hierarchy */
  protected int ruleLevel;

  /* The rule's minimal length. */
  protected int minLength;

  /* The rule's maximal length. */
  protected int maxLength;

  /* The rule mean length - i.e. mean value of all subsequences corresponding to the rule. */
  protected Integer meanLength;

  /* The rule mean period - i.e. the mean length of intra-rule intervals. */
  protected double period;

  /* The rule period error. */
  protected double periodError;

  /* The rule yield - how many terminals it produces in extended form. */
  protected int ruleYield;

  /**
   * @return the grammar's rule number.
   */
  public int ruleNumber() {
    return ruleNumber;
  }

  public Integer getMeanLength() {
    return meanLength;
  }

  public void setMeanLength(Integer length) {
    this.meanLength = length;
  }

  /**
   * @param ruleNum index of the rule
   */
  public void setRuleNumber(int ruleNum) {
    this.ruleNumber = ruleNum;
  }

  /**
   * @return frequency of the rule
   */
  public int getRuleUseFrequency() {
    return ruleUsageFrequency;
  }

  /**
   * @param ruleFrequency frequency of the rule
   */
  public void setRuleUseFrequency(int ruleFrequency) {
    this.ruleUsageFrequency = ruleFrequency;
  }

  /**
   * @return name of the rule, something like R1 or R30 etc.
   */
  public String getRuleName() {
    return "R" + this.ruleNumber;
  }

  /**
   * @return textual representation of the rule
   */
  public String getRuleString() {
    return ruleString;
  }

  /**
   * @param ruleString textual representation of the rule
   */
  public void setRuleString(String ruleString) {
    this.ruleString = ruleString;
  }

  /**
   * @return expanded textual representation of the rule
   */
  public String getExpandedRuleString() {
    return expandedRuleString;
  }

  /**
   * @param expandedRuleString expanded textual representation of the rule
   */
  public void setExpandedRuleString(String expandedRuleString) {
    this.expandedRuleString = expandedRuleString;
  }

  public String occurrencesToString() {
    return Arrays.toString(this.timeSeriesOccurrenceIndexes
        .toArray(new Integer[this.timeSeriesOccurrenceIndexes.size()]));
  }

  public ArrayList<Integer> getOccurrences() {
    return this.timeSeriesOccurrenceIndexes;
  }

  public void setOccurrences(int[] indexes) {
    this.timeSeriesOccurrenceIndexes = new ArrayList<Integer>();
    for (Integer idx : indexes) {
      this.timeSeriesOccurrenceIndexes.add(idx);
    }
  }

  public double getPeriod() {
    return period;
  }

  public void setPeriod(double period) {
    this.period = period;
  }

  public double getPeriodError() {
    return periodError;
  }

  public void setPeriodError(double periodError) {
    this.periodError = periodError;
  }

  /**
   * Set min and max lengths.
   * 
   * @param lengths the lengths to set.
   */
  public void setMinMaxLength(int[] lengths) {
    Arrays.sort(lengths);
    this.minLength = lengths[0];
    this.maxLength = lengths[lengths.length - 1];
  }

  /**
   * Returns a string of min and max values.
   * 
   * @return min and mas as a string.
   */
  public String minMaxLengthAsString() {
    return (String.valueOf(this.minLength) + " - " + String.valueOf(this.maxLength));
  }

  /**
   * Set the rule level.
   * 
   * @param ruleLevel the new rule level.
   */
  public void setRuleLevel(int ruleLevel) {
    this.ruleLevel = ruleLevel;
  }

  /**
   * The rule level.
   * 
   * @return the rule level.
   */
  public int getRuleLevel() {
    return this.ruleLevel;
  }

  public int getRuleYield() {
    return this.ruleYield;
  }

  public void setRuleYield(int ruleYield) {
    this.ruleYield = ruleYield;
  }

  public ArrayList<RuleInterval> getRuleInterval() {
    return this.ruleIntervals;
  }

  public void setRuleInterval(ArrayList<RuleInterval> resultIntervals) {
    this.ruleIntervals = resultIntervals;
  }

  public String toString() {
	  if(this.ruleString.length()>20)
		  	return "R" + this.ruleNumber + " -> " + this.ruleString.substring(0, 20);
	  else
		  return "R" + this.ruleNumber + " -> " + this.ruleString;
  }

  public int getRuleNumber() {
    return ruleNumber;
  }

@Override
public GrammarRuleRecord clone() throws CloneNotSupportedException {
	// TODO Auto-generated method stub
	GrammarRuleRecord other=new GrammarRuleRecord();
	other.expandedRuleString=this.expandedRuleString;
	other.maxLength=this.maxLength;
	other.meanLength=this.meanLength;
	other.minLength=this.minLength;
	other.period=this.period;
	other.periodError=this.periodError;
	other.ruleLevel=this.ruleLevel;
	other.ruleIntervals=this.ruleIntervals;
	other.ruleString=this.ruleString;
	other.ruleUsageFrequency=this.ruleUsageFrequency;
	other.ruleYield=this.ruleYield;
	other.timeSeriesOccurrenceIndexes=this.timeSeriesOccurrenceIndexes;
	return other;
}

public AGrammarRuleRecord toApprox() throws CloneNotSupportedException {
	// TODO Auto-generated method stub
	AGrammarRuleRecord other=new AGrammarRuleRecord();
	other.expandedRuleString=this.expandedRuleString;
	other.maxLength=this.maxLength;
	other.meanLength=this.meanLength;
	other.minLength=this.minLength;
	other.period=this.period;
	other.periodError=this.periodError;
	other.ruleLevel=this.ruleLevel;
	other.ruleNumber=this.ruleNumber;
	
	other.ruleString=this.ruleString;
	AWordList w = new AWordList(this.ruleString);
	other.setRuleArray(w);
	other.ruleUsageFrequency=this.ruleUsageFrequency;
	other.ruleYield=this.ruleYield;
	other.timeSeriesOccurrenceIndexes=this.timeSeriesOccurrenceIndexes;
	other.setRuleName("X");
	return other;
}
}