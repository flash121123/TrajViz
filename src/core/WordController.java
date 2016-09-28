/**
This class is used for querying the data in a time series string,
The string is encoded in the word sequences (class: Word)

We want to get the location of Word
We want to update the word location

 * 
 */
package core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yfeng
 *
 */
public class WordController {

	private ArrayList<Word> w = new ArrayList<Word>();
	
	public WordController(ArrayList<Word> w)
	{
		this.w=IterativeController.deepcopy(w);
	}
	
	//Finding sub sequence
	public List<Word> query(Integer a, Integer b)
	{
		return w.subList(a, Math.min(b,w.size()-1));
	}
	
	//get a word at index a
	public Word get(Integer a)
	{
		return w.get(a);
	}
	
	//find the index of a word
	public int find(Word wx)
	{
		return w.indexOf(wx);
	}
	
	//find subset from word w1 to w2
	public List<Word> findsubset(Word w1, Word w2)
	{
		return w.subList(w.indexOf(w1), w.indexOf(w2));
	}
	
	//find rule correspond sequence
	
	public List<Word> find(ArrayList<Word> w1, Integer n)
	{
		return w.subList(w1.get(n).start, w1.get(n+1).start);
	}
	
	
}
