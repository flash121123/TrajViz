package core.agi;

import java.util.ArrayList;




import base.CountSet;
import test.SequiturFactory;
import core.Word;
import core.Collections.AWordList;
import core.Collections.WordList;
import core.gi.GrammarRules;
import core.agi.RuleInterval;

public class ItrSequitur  {

	private static AWordList universal_word=new AWordList();
	
	private static AGrammarRules ar=new AGrammarRules();
	
	public static void restart()
	{
		ar=new AGrammarRules();
	}
	public static AGrammarRules run(AWordList w) throws Exception {
		// TODO Auto-generated method stub
		if(ar.getLayer()==-1)
			ar.inc();
		else
			ar.inc(w);

		GrammarRules rules=SequiturFactory.runSequitur(w.toStringC()).toGrammarRulesData();
		if(ar.getLayer()==0)
			ar.g=rules;
		AGrammarRules ar2=rules.toApproximate(ar.getLayer());
		addOccurrences(ar2,w);
		updateInterval(ar2, w);
		updateOccurrences(ar2,w);
		
		updateBasic(ar2);
		update2ndExpandRule(ar2);
		
		updateError(ar2);
		ar.addAll(ar2);
		updateRule(ar);
		//removeReduancy(ar);
		return ar;
	}

	private static void addOccurrences(AGrammarRules ar2, AWordList w) {
		// TODO Auto-generated method stub
		CountSet c=new CountSet();
		AWordList tmp=new AWordList(ar2.get(0).getRuleString());
		RecordRule(tmp,ar2,c,0);
	
		for(String x : c.keySet())
		{
			Word a=new Word(0,0,x);
			
			ar2.get(a.rule()).setOccurrences(toArray(c.get(x)));
		
		}
		
	}
	
	public static Integer RecordRule(AWordList w,AGrammarRules ar,CountSet c,Integer t)
	{
		for(int i=0;i<w.size();i++)
		{
			
			Word ws=w.get(i);
			if(ws.isrule())
			{
				c.put(ws.str(), t);
				t=RecordRule(ar.get(ws.rule()).getRuleArray(),ar,c,t);
			}
			else
			{
				t++;
			}
		}
		return t;
	}
	
	protected static int[] toArray(ArrayList<Integer> x)
	{
		int[] y=new int[x.size()];
		for(int i=0;i<x.size() ;i++)
		{
			y[i]=x.get(i);
		}
		return y;
	}
	private static void updateError(AGrammarRules ar2) {
		
		for(int i=1;i<ar2.count();i++)
		{
			ar2.get(i).seterr(ErrorControl.LengthErr(ar2.get(i)));
		}
	}

	private static void update2ndExpandRule(AGrammarRules ar) {
		// TODO Auto-generated method stub
		for(int i=1;i<ar.count();i++)
		{
			AGrammarRuleRecord r = ar.get(i);
			if(r.isBasic())
				continue;
			
				AWordList w=r.getRuleArray(ar);
				for(int j=0;j<w.size();j++)
				{
					Word x=w.get(j);
					if(x.isrule() && !ar.get(x.rule()).isBasic())
					{
						w.expand(x,ar.get(x.rule()).getRuleArray(ar),ar);
						j--;
					}
					
				
				r.setExpend2ruleArray(w);
			}
				
			
		}
	}

	private static void updateBasic(AGrammarRules ar) {
		// TODO Auto-generated method stub
		for(int i=1;i<ar.count();i++)
		{
			AGrammarRuleRecord r = ar.get(i);
			AWordList w=r.getRuleArray();
			boolean flag=true;
			for(Word x : w)
			{
				if(x.isrule())
				{
					flag=false;
					break;
				}
			}
			r.setBasic(flag);
		}
	}

	public static void updateOccurrences(AGrammarRules ar, WordList w) {
		// TODO Auto-generated method stub
		for(int j=0;j<ar.count();j++)
		{
			AGrammarRuleRecord r = ar.get(j);
			ArrayList<Integer> x = r.getOccurrences();
			int[] n=new int[x.size()];
			for(int i=0;i<n.length;i++)
			{
				n[i]=w.get(x.get(i)).getStart();
			}
			r.setOccurrences(n);
		}
	}

	public static void updateRule(AGrammarRules ar) {
		// TODO Auto-generated method stub
		for(int i=0;i<ar.count();i++)
		{
			AGrammarRuleRecord r = ar.get(i);
			if(ar.getLayer()<=0)
				r.setTrueRuleYield(r.getRuleYield());
			else
				r.setTrueRuleYield(RuleYield(r,ar));
			
		}
	}
	
	public static void updateInterval(AGrammarRules ar, AWordList w) {
		// TODO Auto-generated method stub
		for(int i=0;i<ar.count();i++)
		{
			AGrammarRuleRecord r = ar.get(i);
			ArrayList<Integer> x = r.getOccurrences();
			for(Integer x2 : x)
			{
				//ystem.out.println(w);
				if(w.get(x2).getStart()>w.get(x2+r.getRuleYield()-1).getEnd())
						continue;
				AWordList p = universal_word.subLists(w.get(x2).getStart(), w.get(x2+r.getRuleYield()-1).getEnd());
				r.addIntervals(p);
				RuleInterval rx=new RuleInterval(p.get(0).start,p.get(p.size()-1).end);
				r.add(rx);
			}
			
			
		}
	}

	private static Integer RuleYield(AGrammarRuleRecord a,AGrammarRules r)
	{
		AWordList aw=new AWordList(a.getExpandedRuleString());
		Integer t=0;
		for (Word s : aw) {
			
			if(s.isrule())
			{
				t=t+r.get(s.rule()).getTrueRuleYield();
			}
			else
			{
				if(s.isword())
				{
					t=t+r.get(s.ruleU()).getTrueRuleYield();
				}
				else
				{
					t++;
				}
			}
			//System.out.println(word_len(r,s));
			//w.add(s);
		}
		return t;
	}
	
	
	public Integer geterr() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer seterr(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer err() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setUniversal_word(AWordList universal) {
		universal_word = new AWordList(universal);
	}

}
