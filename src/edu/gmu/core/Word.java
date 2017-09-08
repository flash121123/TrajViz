package edu.gmu.core;

import edu.gmu.core.agi.AGrammarRuleRecord;

public class Word implements gmu.edu.core.frame.Word {
	/**
	 * start point of the word
	 */
	public Integer start=-1;
	/**
	 * end point of the word
	 */
	public Integer end=-1;
	String  words="";
	
	/**
	 * 
	 * @param in start point in the string
	 * @param s value of word
	 */
	public Word(Integer in, String s)
	{
		start=in;
		words=s;
	}
	
	/**
	 * @param in  start point
	 * @param in2	end point
	 * @param s	value of string
	 */
	public Word(Integer in, Integer in2, String s)
	{
		start=in;
		end=in2;
		words=s;
	}
	
	/**
	 *  null initalization
	 */
	public Word(){}
	
	/**
	 * @param word Copy initalization
	 */
	public Word(Word word) {
		start=word.start;
		words=word.words;
	}

	public void ind(Integer in)
	{
		start=in;
	}

	public void str(String in)
	{
		words=in;
	}

	
	public Integer rule()
	{
		String wx=new String(words);
		if (wx.contains("R")) {
			wx = wx.replace("R", "");
			Integer rt = Integer.parseInt(wx);
			return rt;
		} else {
			return -1;
		}
	}
	
	
	public boolean isword()
	{
		String wx=new String(words);		
			if(wx.contains("W_"))
			{
			return true;
			}
			else
			{
			return false;
			}
	}
	
	public Integer[] ruleU()
	{
		Integer[] t=new Integer[2];
		t[0]=-1;
		t[1]=-1;
		String wx=new String(words);
		if (wx.contains("R")) {
			wx = wx.replace("R", "");
			Integer rt = Integer.parseInt(wx);
			t[1]=rt;
			return t;
		} else {
			if(wx.contains("W_"))
			{
				wx = wx.replace("W_", "");
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
	
	public boolean isrule()
	{
		if(words.contains("R"))
			return true;
		else
			return false;
	}
	
	public boolean isbreak()
	{
		if(words.contains("X"))
			return true;
		else
			return false;
	}
	
	public boolean isanomaly()
	{
		if(words.contains("A"))
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		
		//return "(" + words + ", (" + start +", "+ end + "))";
		return words;
	}

	public boolean contains(String string) {
		// TODO Auto-generated method stub
		
		return words.contains(string);
	}

	public Word replace(String string, String string2) {
		// TODO Auto-generated method stub
		Word w =new Word(start,words.replace(string, string2));
		return w;
	}
	
	public String str()
	{return words;}
	

	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}

	public static boolean isword(String wx) {
		// TODO Auto-generated method stub
		if(wx.contains("W_"))
		{
		return true;
		}
		else
		{
		return false;
		}
	}
	
	public static Integer[] ruleU(String wx)
	{
		Integer[] t=new Integer[2];
		t[0]=-1;
		t[1]=-1;
		if (wx.contains("R")) {
			wx = wx.replace("R", "");
			Integer rt = Integer.parseInt(wx);
			t[1]=rt;
			return t;
		} else {
			if(wx.contains("W_"))
			{
				wx = wx.replace("W_", "");
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

	public static int k=0;
	public static String uniqueWord() {
		// TODO Auto-generated method stub
		String s="R"+k;
		k++;
		return s;
	}

	public Integer[] ruleU(AGrammarRuleRecord x) {
		// TODO Auto-generated method stub
		Integer[] t=new Integer[2];
		t[0]=-1;
		t[1]=-1;
		if (words.contains("R")) {
			String tmp = words.replace("R", "");
			Integer rt = Integer.parseInt(tmp);
			t[1]=rt;
			t[0]=Integer.parseInt(x.getRuleName().replace("R","").split("-")[0])+1;
			return t;
		} else {
			if(words.contains("W_"))
			{
				String tmp = words.replace("W_", "");
			String[] tt=tmp.split("_");
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
	
}
