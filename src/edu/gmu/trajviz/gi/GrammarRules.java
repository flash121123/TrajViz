package edu.gmu.trajviz.gi;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class GrammarRules implements Iterable<GrammarRuleRecord> {

  private SortedMap<Integer, GrammarRuleRecord> rules;

  public GrammarRules() {
    super();
    this.rules = new TreeMap<Integer, GrammarRuleRecord>();
  }

  public void addRule(GrammarRuleRecord arrRule) {
    int key = arrRule.getRuleNumber();
    this.rules.put(key, arrRule);
  }

  public GrammarRuleRecord getRuleRecord(Integer ruleIdx) {
    return this.rules.get(ruleIdx);
  }

  @Override
  public Iterator<GrammarRuleRecord> iterator() {
    return rules.values().iterator();
  }

  public GrammarRuleRecord get(Integer ruleIndex) {
    return rules.get(ruleIndex);
  }

  public int size() {
    return this.rules.size();
  }
// add 2 method by qz
public void merge(int i, int j) {
	// TODO Auto-generated method stub
	
}
private void remove(int id){
	
}

public void setParsedString() {
	for(int i = 0; i<rules.size();i++)
		rules.get(i).setParsedStringAndRuleYield();
}

}
