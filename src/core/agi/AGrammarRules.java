package core.agi;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import core.Collections.AWordList;
import core.agi.AGrammarRuleRecord;
import core.gi.GrammarRules;

/**
 * @author yfeng
 *
 */
public class AGrammarRules implements Iterable<AGrammarRuleRecord>{

      public GrammarRules g;
	  private SortedMap<String, AGrammarRuleRecord> rules;

	  private Integer layer=-1;
	  private Integer count=0;
	  
	  public AGrammarRules() {
	    super();
	    layer=-1;
	    count=0;
	    this.rules = new TreeMap<String, AGrammarRuleRecord>();
	  }

	  public AGrammarRules(GrammarRules rules2) {
		// TODO Auto-generated constructor stub
		  
	}

	public void addRule(AGrammarRuleRecord arrRule) {
	    String key = "R"+layer+"-"+count;
	    arrRule.setRuleName(key);
	    this.rules.put(key, arrRule);
	    count++;
	  }

	public void addRuleWithName(AGrammarRuleRecord arrRule,Integer num) {
	    String key = "R"+layer+"-"+num;
	    arrRule.setRuleName(key);
	    this.rules.put(key, arrRule);
	    count++;
	  }
	  public AGrammarRuleRecord getRuleRecord(String ruleIdx) {
	    return this.rules.get(ruleIdx);
	  }

	  public Iterator<AGrammarRuleRecord> iterator() {
	    return rules.values().iterator();
	  }

	  public AGrammarRuleRecord get(String ruleIndex) {
	    return rules.get(ruleIndex);
	  }

	  public AGrammarRuleRecord get(Integer ruleIndex) {
		    return rules.get("R"+layer+"-"+ruleIndex);
		  }
	  
	  public int size() {
	    return this.rules.size();
	  }

	  public int count() {
		    return this.count;
		  }
	public Integer getLayer() {
		return layer;
	}

	public void inc(AWordList w) {
		this.layer++;
		this.count=0;
		w.inc(layer);
	}

	@Override
	public String toString() {
		return "AGrammarRules [layer=" + layer + ", count=" + count + ", size= "+ this.size() + "]";
	}

	public void addAll(AGrammarRules app) {
		// TODO Auto-generated method stub
		for(AGrammarRuleRecord r : app)
		{
			this.addRuleWithName(r,r.getRuleNumber());
		}
	}

	public void inc() {
		// TODO Auto-generated method stub
		this.layer++;
		this.count=0;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public AGrammarRuleRecord get(Integer[] ruleU) {
		// TODO Auto-generated method stub
		ruleU[0]=ruleU[0]-1;
	    return rules.get("R"+ruleU[0]+"-"+ruleU[1]);
	}

	public AGrammarRuleRecord remove(String arg0) {
		return rules.remove(arg0);
	}

	public AGrammarRuleRecord remove(Integer a) {
		
		return rules.remove(this.get(a).getRuleName());
	}

	public ArrayList<RuleInterval> getAll() {
		// TODO Auto-generated method stub
		ArrayList<RuleInterval> x=new ArrayList<RuleInterval>();
		
		for(AGrammarRuleRecord g : rules.values())
		{
			x.addAll(g.getRuleintervels());
		}
		return x;
	}

	public boolean containsKey(Object arg0) {
		return rules.containsKey(arg0);
	}

	public Set<String> keySet() {
		return rules.keySet();
	}

	public Collection<AGrammarRuleRecord> values() {
		return rules.values();
	}
	
	public ArrayList<Integer> lens;

	private int k;

	public void setLayerLength(ArrayList<Integer> lens) {
		// TODO Auto-generated method stub
		this.lens=lens;
		int k=0;
		for(int i=1;i<this.lens.size();i++)
		{
			k=k+this.lens.get(i);
			
		}
		if(lens.size()==1)
			this.k=0;
		else
			this.k=k/(lens.size()-1);
		
	}
	
	public Integer getThrLength() {
		// TODO Auto-generated method stub
		return k;
	}
}
