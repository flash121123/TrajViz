package core.gi;

import java.util.ArrayList;


import test.SequiturFactory;
import core.Collections.WordList;
import core.frame.GInterface;
import core.gi.RuleInterval;

public class ItrSequitur implements GInterface{
	Integer layer=0;
	public GrammarRules run(WordList w) throws Exception
	{
		GrammarRules rules=SequiturFactory.runSequitur(w.toStringC()).toGrammarRulesData();
		
		updateOccurrences(rules,w);
		updateInterval(rules, w);
		return rules;
	}
	
	public void inc()
	{
		layer++;
	}
	
	public void updateOccurrences(GrammarRules rules, WordList w)
	{
		for(GrammarRuleRecord r : rules)
		{
			ArrayList<Integer> x = r.getOccurrences();
			int[] n=new int[x.size()];
			for(int i=0;i<n.length;i++)
			{
				n[i]=w.get(x.get(i)).getStart();
			}
			r.setOccurrences(n);
		}
	}

	@Override
	public void updateInterval(GrammarRules rules, WordList w) {
		// TODO Auto-generated method stub
		for(GrammarRuleRecord r : rules)
		{
			ArrayList<Integer> x = r.getOccurrences();
			ArrayList<RuleInterval> rn=new ArrayList<RuleInterval>();
			for(Integer x2 : x)
			{
				RuleInterval rx=new RuleInterval(w.get(x2).getStart(),w.get(x2+r.getRuleYield()).getEnd());
				rn.add(rx);
			}
			r.setRuleInterval(rn);
		}
	}
	
	public void updateRuleName(GrammarRules rules) {
		// TODO Auto-generated method stub
		for(GrammarRuleRecord r : rules)
		{
			r.getRuleName();
		}
	}
}
