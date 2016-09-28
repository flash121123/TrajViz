package core.frame;

public interface Interval {
	
	  public void setStart(int startPos);
	  /**
	   * @return starting position within the original time series
	   */
	  public int getStart();

	  /**
	   * @param endPos ending position within the original time series
	   */
	  public void setEnd(int endPos);

	  /**
	   * @return ending position within the original time series
	   */
	  public int getEnd();
	  
	  
	  
}
