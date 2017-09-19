package edu.gmu.base;

import edu.gmu.core.agi.RuleInterval;
import intervaltree.Interval;

public class IntervalG extends Interval {
	private Integer startPos=-1;
	private Integer endPos=-1;
	private Integer lens=0;
	
	public IntervalG()
	{
		return;
	}

	public IntervalG(Integer s,Integer e)
	{
		startPos=s;
		endPos=e;
	}
	
	public IntervalG(Double s, Double e) {
		// TODO Auto-generated constructor stub
		startPos=s.intValue();
		endPos=e.intValue();
	}

	public IntervalG(RuleInterval y) {
		// TODO Auto-generated constructor stub
		this.startPos=y.getStart();
  	this.endPos=y.getEnd();
	}

	@Override
	public String toString() {
		return "[" + startPos + ", " + endPos + "]";
	}

}
