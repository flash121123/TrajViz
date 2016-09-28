package edu.gmu.trajviz.gi;
import java.util.ArrayList;
import java.util.Arrays;

import edu.gmu.trajviz.gi.sequitur.SAXRule;
import edu.gmu.trajviz.logic.RuleInterval;
import edu.gmu.trajviz.model.SequiturModel;

/**
 * Data container for SAX rules. Provides an abstraction which is used for transferring grammars
 * from various algorithms to front-end GUI.
 * 
 * @author Manfred Lerner, seninp
 * 
 */
public class GrammarRuleRecord {

  /* The rule number in Sequitur grammar. */
  private int ruleNumber;

  /* The rule string, this may contain non-terminal symbols. */
  private String ruleString;

  /* The expanded rule string, this contains previous symbols. */
  private String expandedRuleString;
  
  /* The expanded rule string, this contains only terminal symbols. */
  private String actualRuleString;
  
  
  /* The indexes at which the rule occurs in the discretized time series. */
  private ArrayList<Integer> timeSeriesOccurrenceIndexes = new ArrayList<Integer>();
  
  /* The indexes at which the rule occurs in the R0 on discretized time serires */
  private ArrayList<Integer> tsR0OccurenceIndexes;// = new ArrayList<Integer>();

  /* This rule intervals on the original time series. */
  private ArrayList<RuleInterval> ruleIntervals;
  
  /* This rule intervals only in R0 on the original time series -qz */
  private ArrayList<RuleInterval> r0Intervals;

  /* The rule use frequency - how many time that rule is used by other rules. */
  private int ruleUsageFrequency;

  /* The rule level in the hierarchy */
  private int ruleLevel;

  /* The rule's minimal length. */
  private int minLength;

  /* The rule's maximal length. */
  private int maxLength;

  /* The rule mean length - i.e. mean value of all subsequences corresponding to the rule. */
  private Integer meanLength;

  /* The rule mean period - i.e. the mean length of intra-rule intervals. */
  private double period;

  /* The rule period error. */
  private double periodError;

  /* The rule yield - how many terminals it produces in previous extended form. */
  private int ruleYield;
  
  /* The rule yield - how many terminals it produces in extended form. */
  private int actualRuleYield;
  /*-qz
   * Frequency in R0
   */
  private int fR0;
  
  
  
  /*
   * cursor
   */
  private int cursor;
  public GrammarRuleRecord(){
	  cursor = 0;
	  tsR0OccurenceIndexes = new ArrayList<Integer>();
  }
  public void setCursor(int i){
	  if(i>r0Intervals.size())
		 // cursor = ruleIntervals.size()-1;
		  throw new IndexOutOfBoundsException("can't set cursor");
	  else
		  cursor = i;
  }
  public int getCursor(){
	  return this.cursor;
  }
  
  public int frequencyInR0(){
	  
	  return fR0;
  }
  public void setFrequencyInR0(int fre){
	  fR0 = fre;
  }
 
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
  public void setMeanLength(int[] length){
	  if (length.length>0)
	  {
		  int sum = 0;
	  
	  for (int i =0; i<length.length; i++)
		  sum =  sum +length[i];
	  this.meanLength = sum/length.length;
	  }
	  else this.meanLength = 0;
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
  public String r0OccurrencesToString() {
	    return Arrays.toString(this.tsR0OccurenceIndexes
	        .toArray(new Integer[this.tsR0OccurenceIndexes.size()]));
	  }

  public ArrayList<Integer> getOccurrences() {
    return this.timeSeriesOccurrenceIndexes;
  }
  
  public ArrayList<Integer> getR0Occurrences(){
	  return this.tsR0OccurenceIndexes;
  }
  
  public void addR0Occurrence(int idx){
	  	  
		  this.tsR0OccurenceIndexes.add(idx);
	  
  }

  public void setOccurrences(int[] indexes) {
    this.timeSeriesOccurrenceIndexes = new ArrayList<Integer>();
    for (Integer idx : indexes) {
    	//qz survive from 0 index error
    	//if(!idx.equals(0))
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

  public ArrayList<RuleInterval> getRuleIntervals() {
    return this.ruleIntervals;
  }
  
  public ArrayList<RuleInterval> getR0Intervals(){
	  return this.r0Intervals;
  }
  
public void setR0Intervals(ArrayList<RuleInterval> r0Intervals){
	this.r0Intervals =  r0Intervals;
  }

  public void setRuleIntervals(ArrayList<RuleInterval> resultIntervals) {
    this.ruleIntervals = resultIntervals;
  }
  

  public String toString() {
    return "R" + this.ruleNumber + " -> " + this.ruleString + " -> " + this.actualRuleString;
  }

  public int getRuleNumber() {
    return ruleNumber;
  }

public void setMinLength() {
	// TODO Auto-generated method stub
	
}
public void setParsedStringAndRuleYield() {
	this.actualRuleString = SequiturModel.parseRule((this.getExpandedRuleString()))+" ";
	this.actualRuleYield = SequiturModel.countSpaces(actualRuleString);
//	System.out.println("actualRuleString: "+this.actualRuleString + ": "+this.actualRuleYield);
}
public String getParsedString(){
	return this.actualRuleString;
}
public int getActualRuleYield(){
	return this.actualRuleYield;
}

}
