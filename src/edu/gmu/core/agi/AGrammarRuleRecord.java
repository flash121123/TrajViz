package edu.gmu.core.agi;

import java.util.ArrayList;

import java.util.Iterator;

import edu.gmu.core.Word;
import edu.gmu.core.agi.RuleInterval;
import gmu.edu.core.Collections.AWordList;
import gmu.edu.core.Collections.WordList;
import gmu.edu.core.frame.Aprox;
import gmu.edu.core.gi.GrammarRuleRecord;


public class AGrammarRuleRecord extends GrammarRuleRecord implements Aprox, Iterable<RuleInterval>,Comparable<AGrammarRuleRecord> {

	protected Integer err=-1;
	protected ArrayList<AWordList> intervals;
	
	protected AWordList Expend2ruleArray;
	protected ArrayList<RuleInterval> ruleintervels;
	protected int pointer=0;
	private boolean basic=false;
	private String rulen;
	protected AWordList ruleArray;
	private Integer TrueRuleYield;
	public boolean skip=false;
	
	public AGrammarRuleRecord() {
		super();
		intervals=new ArrayList<AWordList>();
		ruleintervels=new ArrayList<RuleInterval>();
		// TODO Auto-generated constructor stub
	}
	
	public Integer err() {
		// TODO Auto-generated method stub
		return err;
	}

	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#getRuleString()
	 */
	@Override
	public String getRuleString() {
		// TODO Auto-generated method stub
		return super.getRuleString();
	}

	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#setRuleString(java.lang.String)
	 */
	public void setRuleString(WordList ruleString) {
		// TODO Auto-generated method stub
		super.setRuleString(ruleString.toStringC());
	}

	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#getOccurrences()
	 */
	@Override
	public ArrayList<Integer> getOccurrences() {
		// TODO Auto-generated method stub
		return super.getOccurrences();
	}


	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#setOccurrences(int[])
	 */
	@Override
	public void setOccurrences(int[] indexes) {
		// TODO Auto-generated method stub
		super.setOccurrences(indexes);
	}


	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#getRuleYield()
	 */
	@Override
	public int getRuleYield() {
		// TODO Auto-generated method stub
		return super.getRuleYield();
	}


	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#setRuleYield(int)
	 */
	@Override
	public void setRuleYield(int ruleYield) {
		// TODO Auto-generated method stub
		super.setRuleYield(ruleYield);
	}


	/* (non-Javadoc)
	 * @see core.gi.GrammarRuleRecord#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(this.ruleString.length()>20)
			return this.rulen + " -> " + this.ruleString.substring(0, 20)+"...";
		else
			return this.rulen + " -> " + this.ruleString;
		
	}

	public ArrayList<AWordList> getIntervals() {
		return intervals;
	}

	public void setIntervals(ArrayList<AWordList> intervals) {

		this.intervals = intervals;
	}

	public void addIntervals(AWordList w) {
		this.intervals.add(w);
	}
	

	@Override
	public Integer geterr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void seterr(Integer e) {

		// TODO Auto-generated method stub
		err=e;
	}

	
	public void setRuleNumber(int ruleNum,int layer) {
		// TODO Auto-generated method stub
		rulen=layer+"-"+ruleNum;
	}

	
	
	@Override
	public String getRuleName() {

		// TODO Auto-generated method stub
		return this.rulen;
	}

	public Integer getEnd() {
		// TODO Auto-generated method stub
	
		pointer++;
		if(pointer>(ruleintervels.size()-1))
		{
			pointer=1;
		}
		return ruleintervels.get(pointer-1).getStrEndPos();
	}

	public Integer getEnd(Integer k) {
		// TODO Auto-generated method stub
		Integer h=0;
		for(int i=0;i<ruleintervels.size();i++)
		{
			if(ruleintervels.get(i).getStrStartPos()>=k)
			{
				h=i;
				break;
			}
		}
		return ruleintervels.get(h).getStrEndPos();
	}

	public Integer getRuleLens(Integer k) {
		// TODO Auto-generated method stub
		Integer h=0;
		for(int i=0;i<ruleintervels.size();i++)
		{
			if(ruleintervels.get(i).getStrStartPos()>=k)
			{
				h=i;
				break;
			}
		}
		return intervals.get(h).size();
	}
	
	
	public boolean add(RuleInterval e) {
		return ruleintervels.add(e);
	}

	public void setRuleName(String key) {
		// TODO Auto-generated method stub
		rulen=key;
	}

	public boolean isBasic() {
		return basic;
	}

	public void setBasic(boolean basic) {
		this.basic = basic;
	}

	public AWordList getRuleArray(Word x) {
		for(AWordList aw : intervals)
		{
			if(aw.get(0).equals(x))
				return aw;
		}
		return null;
	}

	public AWordList getRuleArray() {
		
		return new AWordList(this.ruleString);
	}

	
	public void setRuleArray(AWordList ruleArray) {
		this.ruleArray = ruleArray;
		
	}


	@Override
	public Iterator<RuleInterval> iterator() {
		// TODO Auto-generated method stub
		return this.ruleintervels.iterator();
	}

	public AWordList getRuleArray(AGrammarRules ar) {
		// TODO Auto-generated method stub
		return new AWordList(this.ruleString,ar);
		
		
	}

	public AWordList getExpend2ruleArray() {
		return Expend2ruleArray.deepcopy();
	}

	public void setExpend2ruleArray(AWordList expend2ruleArray) {
		Expend2ruleArray = expend2ruleArray;
	}

	public void setStartEndPos(Word s) {
		// TODO Auto-generated method stub
		Integer h=0;
		for(int i=0;i<ruleintervels.size();i++)
		{
			if(ruleintervels.get(i).getStrStartPos()>=s.start)
			{
				h=i;
				break;
			}
		}
		if(ruleintervels.size()==0)
		{
			s.setStart(0);
			s.setEnd(0);
			
			return;
		}
		s.setStart(ruleintervels.get(h).getStrStartPos());
		s.setEnd(ruleintervels.get(h).getStrEndPos());
		
	}

	public int size() {
		// TODO Auto-generated method stub
		return intervals.size();
	}

	public ArrayList<RuleInterval> getRuleintervels() {
		return ruleintervels;
	}

	public void setRuleintervels(ArrayList<RuleInterval> ruleintervels) {
		this.ruleintervels = ruleintervels;
	}

	public Integer getTrueRuleYield() {
		// TODO Auto-generated method stub
		return TrueRuleYield;
	}

	public void setTrueRuleYield(Integer t) {
		// TODO Auto-generated method stub
		this.TrueRuleYield=t;
	}

	public boolean isHead() {
		// TODO Auto-generated method stub
		return Integer.parseInt(this.getRuleName().split("-")[1])==0;
	}

	public AWordList getR0Occurrences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(AGrammarRuleRecord o) {
		// TODO Auto-generated method stub
		Integer tmp=o.getIntervals().get(0).size();
		Integer tmp2=this.getIntervals().get(0).size();
		return tmp.compareTo(tmp2);
	}


	public int getRuleLength()
	{
		return this.getIntervals().get(0).size();
		
	}


	
}
