package core.frame;

import java.util.ArrayList;
import java.util.Collection;

import core.gi.GrammarRules;

public interface WordListI {
	public void add(String x, Integer a,Integer b);
	public void add(Word w);
	public void addAll(Collection<? extends Word> c);
	public Word get(int index);
	public String getStr(int index);
	public Integer getStartPos(int index);
	public ArrayList<Word> getWords();
	public void setWords(ArrayList<Word> words);
	public void setWords(GrammarRules r);
	public void setWords(String s);
}
