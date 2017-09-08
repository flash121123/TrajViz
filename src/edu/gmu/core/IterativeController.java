package edu.gmu.core;

import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.gmu.base.CountSet;
import edu.gmu.core.Word;
import edu.gmu.core.agi.AGrammarRules;
import gmu.edu.core.gi.GrammarRuleRecord;
import gmu.edu.core.gi.GrammarRules;

public class IterativeController {
    public static ArrayList<Word> words = new ArrayList<Word>();
    private static ArrayList<ArrayList<Word>> words_layer = new ArrayList<ArrayList<Word>>();
    private static String str_storage="";
    private static String[] ss;
    
	
	public static Integer[] allrule(String wx)
	{
		Integer[] t=new Integer[2];
		t[0]=-1;
		t[1]=-1;
		//String wx=new String(words);
		if (wx.contains("R")) {
			wx = wx.replace("R", "");
			Integer rt = Integer.parseInt(wx);
			t[1]=rt;
			return t;
		} else {
			if(wx.contains("W"))
			{
				wx = wx.replace("W", "");
			String[] tt=wx.split("_");
			t[0]=Integer.parseInt(tt[0]);
			t[1]=Integer.parseInt(tt[1]);
			return t;
			}
			else
			{
				return t;
			}
		}
	}
	
	public static void generateCsvFile(String sFileName, GrammarRules rules) {
		// TODO Auto-generated method stub
		try {
			FileWriter writer = new FileWriter(sFileName);
			
			for (GrammarRuleRecord r : rules)
			{
				ArrayList<Integer> a=r.getOccurrences();
				for(int i=0;i<a.size();i++)
				{
					Integer ends=a.get(i)+r.getRuleYield();
					writer.append(r.getRuleName()+","+a.get(i)+","+ends+"\n");
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String arr2str(ArrayList<String> s)
	{
		String ss="";
		for(String sx : s)
			ss=ss+" "+sx;
		return ss.substring(1);
	}
	
	public static ArrayList<Word> deepcopy(ArrayList<Word> w)
	{
		ArrayList<Word> wx=new ArrayList<Word>();
		for(Word t : w)
		{
			Word s=new Word(t.getStart(),t.getEnd(),t.str());
			wx.add(s);
		}
		return wx;
	}
	
	
	private static ArrayList<String> ExpandRule(
			GrammarRuleRecord sg, GrammarRules r, int level,ArrayList<String> q) {
		// TODO Auto-generated method stub
		String[] w=sg.getRuleString().split(" ");

		for(String s : w)
		{
			Integer x=getRule(s);
			if(x==-1)
				q.add(s);
			else
			{
				GrammarRuleRecord sg2=r.get(x);
				Integer x2=sg2.getRuleLevel();
				if(x2<=level)
					q.add(s);
				else
				{
					q=ExpandRule(sg2,r,level,q);
				}
			}	
		}
		return q;
	}
	
	
	private static String Filter(String str, GrammarRules rule) {
		// TODO Auto-generated method stub
		
		ArrayList<String> s=new ArrayList<String>();
		Random randomGenerator = new Random();
		for(String sx : str.split(" "))
		{
			Integer x=getRule(sx);
			if(x==-1)
				s.add(sx);
			else
			{
				GrammarRuleRecord sg=rule.get(x);
				Integer x2=sg.getRuleLevel();
				Integer x3=iscontainRule(sg);
				
				if(x2==1)
				{
					if(x3==1)
					{
						ArrayList<String> q=new ArrayList<String>();
						s.addAll(ExpandRule(sg,rule,1,q));
						continue;
					}
					s.add(sx);
				}
				else
				{
					int randomInt = randomGenerator.nextInt(x2-1)+1;
					ArrayList<String> q=new ArrayList<String>();
					s.addAll(ExpandRule(sg,rule,randomInt,q));
				}
			}
		}
		
		return arr2str(s);
	}
	
	
	public static Integer getRule(GrammarRules r, String s)
	{
		if (s.contains("R")) {
			s = s.replace("R", "");
			Integer rt = Integer.parseInt(s);
			return rt;
		} else {
			return 0;
		}
	}

	public static Integer getRule(GrammarRules r, Word s)
	{
		if (s.contains("R")) {
			s = s.replace("R", "");
			Integer rt = Integer.parseInt(s.str());
			return rt;
		} else {
			return 0;
		}
	}

	public static Integer getRule(String s) {
		// TODO Auto-generated method stub
		if (s.contains("R")) {
			s = s.replace("R", "");
			Integer rt = Integer.parseInt(s);
			return rt;
		} else {
			return -1;
		}
	}
	

	private static Integer iscontainRule(GrammarRuleRecord sg) {
		// TODO Auto-generated method stub
		for(String x : sg.getRuleString().split(" "))
		{
			if(isrule(x))
				return 1;
		}
		return 0;
	}
	
	public static boolean isrule(String x) {
		// TODO Auto-generated method stub
		
			if(x.contains("R"))
				return true;
			else
				return false;
		}

	public static boolean isrule(Word s)
	{
		if(s.words.contains("R"))
			return true;
		else
			return false;
	}
	
	
	
	public static boolean isword(String wx)
	{
			
			if(wx.contains("W"))
			{
			return true;
			}
			else
			{
			return false;
			}
	}
	
	public static String rule2str(GrammarRules r)
	{
		return r.get(0).toString();
	}
	
	
	public static ArrayList<String> rule2strlist(GrammarRules rule)
	{
		String[] t=rule.get(0).toString().split(" ");
	    ArrayList<String> s=new ArrayList<String>();
	    for(int i=0;i<t.length;i++)
	    {
	    	if(i==0 || i==1)
	    		continue;
	    	s.add(t[i]);
	    }
	    
	return s;
	}

	public static ArrayList<Word> rule2words(GrammarRules rule)
	{
		String[] t=Filter(rule.get(0).getRuleString(),rule).split(" ");
		
	    ArrayList<Word> s=new ArrayList<Word>();
	    for(int i=0;i<t.length;i++)
	    {
	    	Word w=new Word(-1,t[i]);
	    	s.add(w);
	    }
	    
	return s;
	}
	
	public static ArrayList<Word> rule2wordsWitoutFilter(GrammarRules rule)
	{
		String[] t=rule.get(0).getRuleString().split(" ");
		
	    ArrayList<Word> s=new ArrayList<Word>();
	    for(int i=0;i<t.length;i++)
	    {
	    	Word w=new Word(-1,t[i]);
	    	s.add(w);
	    }
	    
	return s;
	}
	
	public static ArrayList<Word> str2word(String tt)
	{
		String[] t=tt.split(" ");
		
	    ArrayList<Word> s=new ArrayList<Word>();
	    for(int i=0;i<t.length;i++)
	    {
	    	Word w=new Word(-1,t[i]);
	    	s.add(w);
	    }
	    
	return s;
	}
	public static String tostr(ArrayList<Word> s)
	{
		String ss="";
		for(Word sx : s)
			ss=ss+" "+sx;
		return ss.substring(1);
	}
	
	
	public static  ArrayList<Word> update(ArrayList<Word> words, ArrayList<Word> w, GrammarRules r)
	{
		//words : For previous layer words string
		//w : For current layer words string
		//r : for current grammar rule
		
		ArrayList<Word> ws=new ArrayList<Word>(update(words,r));
		ArrayList<Word> wx=new ArrayList<Word>();
		
		for(int i=0;i<ws.size();i++)
		{
			Word s =new Word(ws.get(i));
			s.start=w.get(s.start).start;
			wx.add(s);
		}
		return wx;
	}
	
	public static ArrayList<Word> update(ArrayList<Word> words, GrammarRules rules)
	{
		ArrayList<Word> w=new ArrayList<Word>();
		Integer t = 0;
		//words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < words.size(); i++) {
			Word s = new Word(words.get(i));
			s.start=t;
			t=t+word_len(rules,s);
			//System.out.println(word_len(r,s));
			w.add(s);
		}
		return w;
	}
	
	public static ArrayList<Word> update(GrammarRules r)
	{
		ArrayList<Word> w=new ArrayList<Word>();
		Integer t = 0;
		words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < words.size(); i++) {
			Word s = new Word(words.get(i));
			s.start=t;
			t=t+word_len(r,s);
			w.add(s);
		}
		return w;
	}
	
	public static String UpdateRules(ArrayList<Word> w,Integer layer) {
		// update rules for correspond words list
		String sx="";
		for(Word s : w)
			sx=sx+" "+s.words;
		return sx.substring(1, sx.length()).replace("R", "W"+layer.toString()+"_");
	}

	
	public static void updateRuleYield(GrammarRules rules,RulePool r) {
		// update rule yield
		for(int i=0;i< rules.size();i++)
		{
			if(i==0)
			{ continue;}
			
			String[] k=rules.get(i).getExpandedRuleString().split(" ");
			Integer Y=0;
			for(String ch : k)
			{
				if(!isword(ch))
				{
					Y++;
				}
				else
				{
					Y=Y+r.get(allrule(ch)).getRuleYield();
				}
			}
			rules.get(i).setRuleYield(Y);
			
		}
	}
	
	public static String w2str(ArrayList<Word> w) {
		// update rules for correspond words list
		String sx="";
		for(Word s : w)
			sx=sx+" "+s.words;
		return sx.substring(1, sx.length());
	}

	public static String w2str(List<Word> w) {
		// update rules for correspond words list
		String sx="";
		for(Word s : w)
			sx=sx+" "+s.words;
		return sx.substring(1, sx.length());
	}

	public static Integer word_len(GrammarRules r, Word s)
	{
		if (s.contains("R")) {
			s = s.replace("R", "");
			Integer rt = Integer.parseInt(s.str());
			Integer t = r.get(rt).getRuleYield();
			return t;
		} else {
			return 1;
		}
	}
	
	public IterativeController()
    {
    	return;
    }
	
	public IterativeController(String s)
	{
		str_storage=s;
	}
	
	public IterativeController(String[] s)
	{
		ss=s;
	}
	
	public boolean convert(String s)
	{
		return true;
	}

	public boolean convert(String[] s)
	{
		return true;
	}
	
	public String[] get_aStr()
	{
		return ss;
	}
	
	public String get_storage()
	{
		return str_storage;
	}

	public static void generateCsvFile(String sFileName, CountSet c2, GrammarRules rules) {
		// TODO Auto-generated method stub
		try {
			FileWriter writer = new FileWriter(sFileName);
			
			for (String r : c2.h.keySet())
			{
				ArrayList<Integer> a=c2.get(r);
				for(int i=0;i<a.size();i++)
				{
					Integer ends=a.get(i)+rules.get(IterativeController.getRule(r)).getRuleYield();
					writer.append(r+","+a.get(i)+","+ends+"\n");
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Integer word_len(AGrammarRules r, Word s) {
		// TODO Auto-generated method stub
		if (s.contains("R")) {
			s = s.replace("R", "");
			String dc="R"+r.getLayer().toString()+"-"+s;
			Integer t = r.get(dc).getRuleYield();
			return t;
		} else {
			return 1;
		}
	}
	
	
	
}
