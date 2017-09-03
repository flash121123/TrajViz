package core.Collections;

import java.util.ArrayList;


import java.util.List;

import core.gi.GrammarRules;
import core.IterativeController;
import core.Word;
import core.agi.AGrammarRuleRecord;
import core.agi.AGrammarRules;
import core.Collections.AWordList;
import core.frame.Aprox;

public class AWordList extends WordList implements Aprox,Iterable<Word> {

	private Integer ind=0;
	
	public AWordList() {
		super();
		// TODO Auto-generated constructor stub
	}


	public AWordList(ArrayList<Word> words) {
		super(words);
		// TODO Auto-generated constructor stub
	}


	public AWordList(GrammarRules r) {
		super(r);
		// TODO Auto-generated constructor stub
	}


	public AWordList(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public AWordList(Integer t,String s) {
		this.words=this.str2word(s,t);
		// TODO Auto-generated constructor stub
	}


	public AWordList(String str,AGrammarRules r) {
		// TODO Auto-generated constructor stub
		ArrayList<Word> w=str2word(str);
		
		Integer t = 0;
		//words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < w.size(); i++) {
			Word s = w.get(i);
			if(s.isrule())
			{
				s.setStart(t);
				t=t+r.get(s.rule()).getRuleYield();
				s.setEnd(t);
				s.str(s.str());
			}
			else
			{
				s.setStart(t);
				t++;
				s.setEnd(t);
			}
			//System.out.println(word_len(r,s));
			//w.add(s);
		}
		words=w;
	}

	/**
	 * @param t  Start Pos of List
	 * @param str	String of list
	 * @param r rules for expand
	 */
	public AWordList(Integer t, String str,AGrammarRules r) {
		// TODO Auto-generated constructor stub
		// TODO Auto-generated constructor stub
		ArrayList<Word> w=str2word(str);
		
		//words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < w.size(); i++) {
			Word s = w.get(i);
			if(s.isrule())
			{
				s.setStart(t);
				t=r.get(s.rule()).getRuleYield();
				s.setEnd(t);
				s.str(s.str());
			}
			else
			{
				s.setStart(t);
				t++;
				s.setEnd(t);
			}
			//System.out.println(word_len(r,s));
			//w.add(s);
		}
		words=w;
	}
	
	public Integer err() {
		// TODO Auto-generated method stub
		Integer[] a=this.toStartArray();
		Integer[] b=this.toEndArray();
		Integer e=0;
		for(int i=0;i<a.length-1;i++)
		{
			e=e+Math.abs(b[i]-a[i+1]);
		}
		return e;
	}


	public Integer err(WordList w) {
		// TODO Auto-generated method stub
		Integer[] a=w.toStartArray();
		Integer[] b=w.toEndArray();
		Integer e=0;
		for(int i=0;i<a.length-1;i++)
		{
			e=e+Math.abs(b[i]-a[i+1]);
		}
		return e;
	}


	@Override
	public Integer geterr() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void seterr(Integer e) {
		
	}

	public AWordList(AGrammarRules r) {
		// TODO Auto-generated constructor stub
		super();
		ArrayList<Word> tmp=str2wordWithRule(0,r);
		this.words = IterativeController.deepcopy(tmp);
	}

	public AWordList(int t, AGrammarRules r) {
		// TODO Auto-generated constructor stub
		super();
		ArrayList<Word> tmp=str2wordWithRule(t,r);
		this.words = IterativeController.deepcopy(tmp);
	}
	
	public AWordList(AWordList ruleArray) {
		// TODO Auto-generated constructor stub
		words=new ArrayList<Word>(ruleArray.getWords());
	}


	public AWordList(List<Word> w) {
		// TODO Auto-generated constructor stub
		ArrayList<Word> wx=new ArrayList<Word>();
		for(Word s : w)
			wx.add(s);
			
		this.words=wx;
	}


	public AWordList(int i, AWordList ruleArray) {
		// TODO Auto-generated constructor stub
		AWordList xw=ruleArray.deepcopy();
		for(Word x : xw)
		{
			x.setStart(x.getStart()+i);
			x.setEnd(x.getEnd()+i);
		}
		this.words=xw.getWords();
	}

	public AWordList(AGrammarRuleRecord x) {
		// TODO Auto-generated constructor stub
		AWordList t=new AWordList();
		String[] q=x.getRuleString().split(" ");
		for(int i=0;i<q.length;i++)
			t.add(q[i], -q.length+i, -q.length+i+1);
	}



	private ArrayList<Word> str2wordWithRule(Integer t,AGrammarRules r) {
		// TODO Auto-generated method stub
		t=0;
		ArrayList<Word> w=str2word(r.get(0).getRuleString());
		//words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < w.size(); i++) {
			Word s = w.get(i);
			if(s.isrule())
			{
				s.setStart(t);
				r.get(s.rule()).setStartEndPos(s);
				t=s.end;
			}
			else
			{
				if(s.isword())
				{
					s.setStart(t);
					r.get(s.ruleU()).setStartEndPos(s);
					t=s.end;
					s.setEnd(t);
				}
				else
				{
					s.setStart(t);
					t++;
					s.setEnd(t);
				}
			}
			//System.out.println(word_len(r,s));
			//w.add(s);
			if(i!=0)
				if(w.get(i-1).getEnd()>s.getStart())
					System.out.println("ERROR! in Converting AWordList at: "+i);
		}
		return w;
	}

	private ArrayList<Word> str2wordWithRuleIn(Integer t,String s2, AGrammarRules r) {
		// TODO Auto-generated method stub
		ArrayList<Word> w=str2word(s2);
		//words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < w.size(); i++) {
			Word s = w.get(i);
			
			if(s.isrule())
			{
				s.setStart(t);
				r.get(s.rule()).setStartEndPos(s);
				t=s.end;
			}
			else
			{
				if(s.isword())
				{
					s.setStart(t);
					r.get(s.ruleU()).setStartEndPos(s);
					t=s.end;
					s.setEnd(t);
				}
				else
				{
					s.setStart(t);
					t++;
					s.setEnd(t);
				}
			}
			//System.out.println(word_len(r,s));
			//w.add(s);
		}
		return w;
	}
	
	@Override
	public String toString() {
		return  words.toString();
	}


	public int size() {
		// TODO Auto-generated method stub
		return words.size();
	}

   @Override
	public AWordList clone() {
		// TODO Auto-generated method stub
		AWordList nw=new AWordList(words);
		return nw;
	}


@Override
public java.util.Iterator<Word> iterator() {
	// TODO Auto-generated method stub
	return words.iterator();
}


public void expand(Word x, AWordList ruleArray,AGrammarRules ar) {
	// TODO Auto-generated method stub
	AWordList w=new AWordList(x.getStart(),ruleArray);
	int i=this.words.indexOf(x);
	this.words.remove(i);
	this.words.addAll(i, w.getWords());

}


public AWordList subLists(int fromIndex, int toIndex) {
	// TODO Auto-generated method stub
	List<Word> w=super.subList(fromIndex, toIndex);
	AWordList wx=new AWordList(w);
	return wx;
	
}

public AWordList deepcopy()
{
	ArrayList<Word> w=IterativeController.deepcopy(this.words);
	AWordList nw=new AWordList(w);
	return nw;
}

public void addAll(AWordList nw) {
	// TODO Auto-generated method stub
	this.words.addAll(nw.getWords());
}

public void inc(int layer)
{
	for(Word s : this.words)
	{
		if(s.isrule())
			s.str(s.str().replace("R","W_"+layer+"_"));
	}
}



public void updateAll(AGrammarRules ar) {
	// TODO Auto-generated method stub
	words=str2wordWithRuleIn(words.get(0).getStart(), this.toStringC(),ar);

}

public AWordList replace(AWordList wp)
{
	AWordList x=new AWordList();
	boolean flag=true;
	for(Word wo: this.words)
	{
		if(!wp.contains(wo))
		{
			flag=true;
			x.add(wo);
		}
		else
		{
			if(flag)
			{
				x.add(new Word(wp.getStartPos(0),wp.getEndPos(wp.size()-1),"A"+ind));
				ind++;
				flag=false;
			}
		}
	}
	return x;
}


public void add(String str) {
	// TODO Auto-generated method stub
	if(words.size()==0)
	{
		this.words.add(new Word(0,1,str));
		return;
	}
	
	Integer t=words.get(words.size()-1).getEnd();
	this.words.add(new Word(t,t+1,str));
}


public int indexOf(Word k) {
	// TODO Auto-generated method stub
	int i=0;
	for(Word x : words)
	{
		if(x.str().equals(k.str()))
			break;
		else
			i++;
	}
	return i;
}

Integer l=0;

public void addUnique() {
	// TODO Auto-generated method stub
	this.add("X"+l);
	l++;
}

}
