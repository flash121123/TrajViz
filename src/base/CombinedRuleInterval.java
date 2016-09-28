package base;

public class CombinedRuleInterval{
	
	  public int startPos;
	  public int endPos;
	  public int rule;
	  public int rule1;
	  private int length;
	  
	  public CombinedRuleInterval() {
	    this.startPos = -1;
	    this.endPos = -1;
	    this.length = -1;
	    this.rule = -1;
	    this.rule1 = -1;	    
	  }

	  public CombinedRuleInterval(int rule, int start, int end, int rule1, int start1, int end1) {
	    if(start>=end1||start1>=end)
	    {
	    	this.startPos = -1;
		    this.endPos = -1;
		    this.length = -1;
		    this.rule = rule;
		    this.rule1 = rule1;	
	    	return;
	    }
	    else
	    {
	    	this.startPos = Math.max(start, start1);
	    	this.endPos = Math.min(end, end1);
	    	this.length = endPos-startPos;
	    	this.rule = rule;
		    this.rule1 = rule1;
	    }
	    
	  }

	 
	  /**
	   * @param startPos starting position within the original time series
	   */
	  public void setStartPos(int startPos) {
	    this.startPos = startPos;
	    length = endPos -startPos;
	  }

	  /**
	   * @return starting position within the original time series
	   */
	  public int getStartPos() {
	    return startPos;
	    
	  }

	  /**
	   * @param endPos ending position within the original time series
	   */
	  public void setEndPos(int endPos) {
	    this.endPos = endPos;
	    this.length = endPos -startPos;
	  }

	  /**
	   * @return ending position within the original time series
	   */
	  public int getEndPos() {
	    return endPos;
	  }
	  public int getRule1(){
		  return rule;
	  }
	  public int getRule2(){
		  return rule1;
	  }

	  /**
	   * @param coverage the coverage to set
	   */

	  /*
	   * (non-Javadoc)
	   * 
	   * @see java.lang.Object#toString()
	   */
	  public String toString() {
	    return "[" + startPos + "-" + endPos + "]";
	  }

	  public int length() {
	    return this.length;
	  }

	
	 

	 
	  
}