package gmu.edu.core.Collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;

import edu.gmu.core.IterativeController;
import edu.gmu.core.Word;
import edu.gmu.core.agi.AGrammarRules;
import gmu.edu.core.frame.WordsConvert;
import gmu.edu.core.gi.GrammarRules;

public class WordList implements WordsConvert{

	protected ArrayList<Word> words;
	
	public WordList() {
		super();
		this.words = new ArrayList<Word>();
	}

	public WordList(ArrayList<Word> words) {


		super();
		this.words = IterativeController.deepcopy(words);
	}
	
	/**
	 * @param r
	 */
	public WordList(GrammarRules r) {
		super();
		ArrayList<Word> tmp=str2wordWithRule(r);
		this.words = IterativeController.deepcopy(tmp);
	}
	
	/**
	 * @param s
	 */
	public WordList(String s) {
		super();
		this.words = str2word(s);
	}
	
	

	/**
	 * @param x word
	 * @param a starPos
	 * @param b endPos
	 */
	public void add(String x, Integer a,Integer b) {
		Word s=new Word(b,a,x);
		words.add(s);
	}
	
	/**
	 * @param w
	 */
	public void add(Word w) {
		words.add(w);
	}
	
	/**
	 * @param c
	 */
	public void addAll(Collection<? extends Word> c)
	{
		words.addAll(c);
	}
	
	/**
	 * @param index
	 * @return
	 */
	public Word get(int index) {
		return words.get(index);
	}
	
	/**
	 * @param index
	 * @return
	 */
	public String getStr(int index) {
		return words.get(index).str();
	}
	
	/** Get StartPos for index word
	 * @param index
	 * @return
	 */
	public Integer getStartPos(int index) {
		return words.get(index).getStart();
	}
	
	/** get EndPos for index word
	 * @param index
	 * @return
	 */
	public Integer getEndPos(int index) {
		return words.get(index).getEnd();
	}
	
	
	public ArrayList<Word> getWords() {
		return words;
	}
	
	public void Indexing(GrammarRules r, WordList wl) {
		// TODO Auto-generated method stub
		
	}
	
	public Iterator<Word> Iterator()
	{
		return words.iterator();
	}
	
	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	public void setWords(GrammarRules r) {
		this.words = this.str2wordWithRule(r);
	}

	public void setWords(String s) {
		this.words = this.str2word(s);
	}


	/**
	 * Sub function for converting string to word sequence
	 * @param tt
	 * @return set of words with i as index
	 */
	public ArrayList<Word> str2word(String tt)
	{
		String[] t=tt.split(" ");
		
	    ArrayList<Word> s=new ArrayList<Word>();
	    for(int i=0;i<t.length;i++)
	    {
	    	Word w=new Word(i,i+1,t[i]);
	    	s.add(w);
	    }
	    
	return s;
	}
	

	public ArrayList<Word> str2word(String tt,Integer k)
	{
		String[] t=tt.split(" ");
		
	    ArrayList<Word> s=new ArrayList<Word>();
	    for(int i=0;i<t.length;i++)
	    {
	    	Word w=new Word(k+i,k+i+1,t[i]);
	    	s.add(w);
	    }
	    
	return s;
	}
	
	
	/**
	 * @param r
	 * @return
	 */
	public ArrayList<Word> str2wordWithRule(GrammarRules r)
	{
		ArrayList<Word> w=str2word(r.get(0).getRuleString().replace("R", "W"));
		Integer t = 0;
		//words=words_layer.get(words_layer.size()-1);
		for (int i = 0; i < w.size(); i++) {
			Word s = new Word(w.get(i));
			s.setStart(t);
			t=t+IterativeController.word_len(r,s);
			//System.out.println(word_len(r,s));
			s.setEnd(t);
			w.add(s);
		}
		return w;
	}

	
	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public List<Word> subList( int fromIndex, int toIndex) {

		return words.subList(fromIndex, toIndex);	
	}

	public GrammarRules ToGrammar() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * @return Correspond start point
	 */
	public Integer[] toStartArray()
	{
		Integer[] n = new Integer[this.words.size()];
		for(int i=0;i<words.size();i++)
		{
			n[i]=words.get(i).getStart();
		}
		return n;
	}
	
	/**
	 * @return Correspond end point
	 */
	public Integer[] toEndArray()
	{
		Integer[] n = new Integer[this.words.size()];
		for(int i=0;i<words.size();i++)
		{
			n[i]=words.get(i).getEnd();
		}
		return n;
	}
	
	/**
	 * @return Correspond string
	 */
	public ArrayList<String> toStringArray()
	{
		ArrayList<String> s=new ArrayList<String>();
		
		for(Word w : words)
		{
			s.add(w.str());
		}
		return s;
	}
	
	public String toStringC()
	{
		String s=words.toString().replace(",", "");
	
		return s.substring(1,s.length()-1);
	}

	public boolean contains(Object o) {
		return words.contains(o);
	}

	public boolean containsAll(Collection<?> arg0) {
		return words.containsAll(arg0);
	}
	
	
}
