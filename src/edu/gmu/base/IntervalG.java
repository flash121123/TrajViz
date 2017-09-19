package edu.gmu.base;

import edu.gmu.core.agi.RuleInterval;

import intervaltree.Interval;

public class IntervalG extends Interval {

	
	public IntervalG()
	{
		return;
	}

	public IntervalG(Integer s,Integer e)
	{
		this.set_start(s);
		this.set_end(e);
	}
	
	public IntervalG(Double s, Double e) {
		// TODO Auto-generated constructor stub
		this.set_start(s.intValue());
		this.set_end(e.intValue());
	}

	public IntervalG(RuleInterval y) {
		// TODO Auto-generated constructor stub
		this.set_start(y.getStart());
		this.set_end(y.getEnd());
	}

	@Override
	public String toString() {
		return "[" + this.get_start() + ", " + this.get_end() + "]";
	}

}
