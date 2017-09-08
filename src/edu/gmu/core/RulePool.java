package edu.gmu.core;

import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.gmu.base.Interval;
import edu.gmu.sax.SAXRecords;
import gmu.edu.core.gi.GrammarRuleRecord;
import gmu.edu.core.gi.GrammarRules;


public class RulePool {
	private   HashMap<String, ArrayList<Interval>> h = new HashMap<String, ArrayList<Interval>>();
	private Integer END_LONG=10000;
	private Integer layer=0;
	private SAXRecords sax;
	public ArrayList<GrammarRules> ar=new ArrayList<GrammarRules>(); 
	public RulePool()
	{
		return;
	}
	public void setEL(Integer in)
	{
		END_LONG=in;
	}
	public RulePool(ArrayList<Word> w)
	{
		for(int i=0;i<w.size();i++)
		{
			Word s=w.get(i);
			if(IterativeController.isrule(s))
			{
				if(h.containsKey(layer.toString()+"-"+s.words))
				{
					ArrayList<Interval> inx=new ArrayList<Interval>(h.get(layer.toString()+"-"+s.words));
					if(i!=w.size()-1)
					{
						Interval in=new Interval(s.start,w.get(i+1).start-1);
						inx.add(in);
					}
					else
					{
						Interval in=new Interval(s.start,END_LONG);	
						inx.add(in);
					}
					h.put(layer.toString()+"-"+s.words, inx);
				}
				else
				{
					ArrayList<Interval> inx=new ArrayList<Interval>();
					if(i!=w.size()-1)
					{
						Interval in=new Interval(s.start,w.get(i+1).start-1);
						inx.add(in);
					}
					else
					{
						Interval in=new Interval(s.start,END_LONG);	
						inx.add(in);
					}
					h.put(layer.toString()+"-"+s.words, inx);
				}
				
			}
		}
		layer++;
	}

	public void add(ArrayList<Word> w)
	{
		ArrayList<Integer> pos=sax.getAllIndices();
		for(int i=0;i<w.size();i++)
		{
			Word s=w.get(i);
			if(IterativeController.isrule(s))
			{
				if(h.containsKey(layer.toString()+"-"+s.words))
				{
					ArrayList<Interval> inx=new ArrayList<Interval>(h.get(layer.toString()+"-"+s.words));
					if(i!=w.size()-1)
					{
						Interval in=new Interval(pos.get(s.start),pos.get(w.get(i+1).start-1));
						inx.add(in);
					}
					else
					{
						Interval in=new Interval(pos.get(s.start),pos.get(pos.size()-1));	
						inx.add(in);
					}
					h.put(layer.toString()+"-"+s.words, inx);
				}
				else
				{
					ArrayList<Interval> inx=new ArrayList<Interval>();
					if(i!=w.size()-1)
					{
						Interval in=new Interval(pos.get(s.start),pos.get(w.get(i+1).start-1));
						inx.add(in);
					}
					else
					{
						Interval in=new Interval(pos.get(s.start),pos.get(pos.size()-1));	
						inx.add(in);
					}
					h.put(layer.toString()+"-"+s.words, inx);
				}
				
			}
		}
		layer++;
	}
	
	public Set<String> getRules()
	{
		return h.keySet();
	}
	
	public void setRules(GrammarRules rules)
	{
		ar.add(rules);
	}
	
	public ArrayList<Interval> getInterval(String w)
	{
		return h.get(w);
	}
	
	public ArrayList<Interval> getInterval(String w, Integer layer)
	{
		return h.get(layer.toString()+"-"+w);
	}
	
	public Set<String> getRules(Integer l)
	{
		//plan to return the l-th layer's rule
		//TO-DO
		return null;
		
	}
	
	public void generateCsvFile(String sFileName) {
		try {
			FileWriter writer = new FileWriter(sFileName);
			Set<String> s=new HashSet<String>(getRules());
			for (String x : s) {
				ArrayList<Interval> q = h.get(x);
				for(Interval l : q)
				{
					String ss= l.get_start().toString() + ',' + l.get_end().toString() + '\n';
					writer.append(ss);
				}
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public HashMap<String, ArrayList<Interval>> getHM()
	{
		return h;
	}
	
	
	public GrammarRuleRecord get(Integer[] t)
	{
		if(t[0]==-1)
			return ar.get(layer-1).get(t[1]);
		return ar.get(t[0]).get(t[1]);
	}
	
	public GrammarRuleRecord get(Integer t)
	{
		return ar.get(layer-1).get(t);
	}
	@Override
	public String toString() {
		return "RulePool [h=" + h + "]";
	}
	public void setSAX(SAXRecords saxData) {
		// TODO Auto-generated method stub
		sax=saxData;
	}
	
	public ArrayList<Interval> get(String s)
	{
		return h.get(s);
	}
	
	public GrammarRules getrules(Integer l)
	{
		return ar.get(l);
	}
	
	public GrammarRules getrules()
	{
		return ar.get(layer-1);
	}
	
}
