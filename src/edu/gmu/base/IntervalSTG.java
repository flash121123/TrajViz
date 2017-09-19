package edu.gmu.base;

import java.util.LinkedList;



import intervaltree.IntervalST;
import intervaltree.Interval;
import edu.gmu.core.agi.AGrammarRuleRecord;
import edu.gmu.core.agi.RuleInterval;

/**
 * 
 * Using Random Binary Search Tree for storing Intervals
 * 
 * The code is wrote on the top of 
 * http://algs4.cs.princeton.edu/93intersection/IntervalST.java.html
 * 
 * Interval Tree is used during the post-processing to merge overlapped patterns
 */

public class IntervalSTG<Value>  extends IntervalST<Value>{

    

	public void remove(AGrammarRuleRecord xx) {
		for(RuleInterval xs : xx)
		{
			this.remove(xs);
		}
	}

	private void remove(RuleInterval xs) {
		Interval x=new Interval(xs.getStart(),xs.getEnd());
		if(this.contains(x))
			this.remove(x);
	}
	
	public void remove(IntervalG xs) {
		Interval x=new Interval(xs.get_start(),xs.get_end());
		if(this.contains(x))
			this.remove(x);
	}

	@Override
	public String toString() {
		return this.searchAll(new Interval(0,5000000)).toString();
	}


}
