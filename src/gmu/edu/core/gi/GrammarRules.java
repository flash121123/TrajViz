package gmu.edu.core.gi;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.gmu.core.agi.AGrammarRuleRecord;
import edu.gmu.core.agi.AGrammarRules;

public class GrammarRules implements Iterable<GrammarRuleRecord>, Cloneable{

  private SortedMap<Integer, GrammarRuleRecord> rules;

  protected AGrammarRules ar;
  
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

  public Iterator<GrammarRuleRecord> iterator() {
    return rules.values().iterator();
  }

  public GrammarRuleRecord get(Integer ruleIndex) {
    return rules.get(ruleIndex);
  }

  public int size() {
    return this.rules.size();
  }

public AGrammarRules toApproximate() throws CloneNotSupportedException {
	// TODO Auto-generated method stub
	AGrammarRules ar=new AGrammarRules();
	
	for(Integer k : this.rules.keySet())
	{
		AGrammarRuleRecord r=rules.get(k).toApprox();
		ar.addRule(r);
	}
	return ar;
}

public AGrammarRules toApproximate(Integer layer) throws CloneNotSupportedException {
	// TODO Auto-generated method stub
	AGrammarRules ar=new AGrammarRules();
	
	ar.setLayer(layer);
	
	for(Integer k : this.rules.keySet())
	{
		AGrammarRuleRecord r=rules.get(k).toApprox();
		ar.addRule(r);
	}
	return ar;
}

}