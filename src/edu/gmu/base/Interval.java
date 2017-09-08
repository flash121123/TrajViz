package edu.gmu.base;

import edu.gmu.core.agi.RuleInterval;

public class Interval {
	private Integer startPos=-1;
	private Integer endPos=-1;
	private Integer lens=0;
	
	public Interval()
	{
		return;
	}

	public Interval(Integer s,Integer e)
	{
		startPos=s;
		endPos=e;
	}
	
	public Interval(Double s, Double e) {
		// TODO Auto-generated constructor stub
		startPos=s.intValue();
		endPos=e.intValue();
	}

	public Interval(RuleInterval y) {
		// TODO Auto-generated constructor stub
		this.startPos=y.getStart();
  	this.endPos=y.getEnd();
	}

	public void set_start(Integer s)
	{
		startPos=s;
	}
	public  void set_end(Integer e)
	{
		endPos=e;
		setlen();
	}
	
	private void setlen() {
		lens=(int)(endPos-startPos);
	}

	public Integer get_len()
	{
		return lens;
	}
	
	public Integer get_start()
	{
		return startPos.intValue();
	}
	
	public Integer get_end()
	{
		return endPos.intValue();
	}
	
	public int compareTo(Interval interval) {
    if      (this.get_start() < interval.get_start()) 
    	return -1;
    else if (this.get_start() > interval.get_start()) 
    	return +1;
    else if (this.get_end() < interval.get_end()) 
    	return -1;
    else if (this.get_end() > interval.get_end()) 
    	return +1;
    else                          
    	return  0;
}
	
  public boolean intersects(Interval interval) {
    if ( interval.get_end()< this.get_start()) return false;
    if (this.get_end() < interval.get_start()) return false;
    return true;
}
  
	@Override
	public String toString() {
		return "[" + startPos + ", " + endPos + "]";
	}

}
