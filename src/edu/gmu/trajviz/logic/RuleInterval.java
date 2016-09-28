package edu.gmu.trajviz.logic;

/**
 * 
 * Helper class implementing an interval used when plotting.
 * 
 * @author Manfred Lerner, seninp
 * 
 */
public class RuleInterval implements Comparable<RuleInterval> {
  public int startPos;
  public int endPos;
  public double coverage;
  public int id;
  private String ruleName;

  public RuleInterval() {
    this.startPos = -1;
    this.endPos = -1;
  }

  public RuleInterval(int startPos, int endPos) {
    this.startPos = startPos;
    this.endPos = endPos;
  }
  public RuleInterval(String name, int startPos, int endPos) {
	    this.ruleName = name;
	    this.startPos = startPos;
	    this.endPos = endPos;
	    
	  }
  public RuleInterval(int id, int startPos, int endPos, double coverage) {
    this.id = id;
    this.startPos = startPos;
    this.endPos = endPos;
    this.coverage = coverage;
  }
  public String getRuleName(){
	  return this.ruleName;
  }
  /**
   * @param startPos starting position within the original time series
   */
  public void setStartPos(int startPos) {
    this.startPos = startPos;
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
  }

  /**
   * @return ending position within the original time series
   */
  public int getEndPos() {
    return endPos;
  }

  /**
   * @return the coverage
   */
  public double getCoverage() {
    return this.coverage;
  }

  /**
   * @param coverage the coverage to set
   */
  public void setCoverage(double coverage) {
    this.coverage = coverage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return "[" + startPos + "-" + endPos + "]";
  }

  public int getLength() {
    return this.endPos - this.startPos;
  }

  @Override
  public int compareTo(RuleInterval arg0) {
    return Integer.valueOf(this.getLength()).compareTo(Integer.valueOf(arg0.getLength()));
  }
// do not allow reset ID
  private void setId(int ruleIndex) {
    this.id = ruleIndex;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(coverage);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + endPos;
    result = prime * result + id;
    result = prime * result + startPos;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RuleInterval other = (RuleInterval) obj;
    if (Double.doubleToLongBits(coverage) != Double.doubleToLongBits(other.coverage))
      return false;
    if (endPos != other.endPos)
      return false;
    if (id != other.id)
      return false;
    if (startPos != other.startPos)
      return false;
    return true;
  }
  //-Qingzhe
  public static boolean isMergable(RuleInterval i1,RuleInterval i2){
	  if((i1.getStartPos()>=i2.getStartPos()&&i1.getStartPos()<=i2.getEndPos())||
			  (i1.getEndPos()>=i2.getStartPos()&&i1.getEndPos()<=i2.getEndPos())||
			  (i2.getStartPos()>=i1.getStartPos()&&i2.getStartPos()<=i1.getEndPos())||
			  (i2.getEndPos()>=i1.getStartPos()&&i2.getEndPos()<=i1.getEndPos()))
		  return true;
	  else  
		  return false;
  }
  public static RuleInterval merge(RuleInterval i1, RuleInterval i2){
	  if(!isMergable(i1,i2))
		  throw new IllegalArgumentException("Given Rule Intervals are not mergable.");
	  int start;
	  int end;
	  if(i1.startPos<i2.startPos)
		  start = i1.startPos;
	  else
		  start = i2.startPos;
	  if(i1.endPos>i2.endPos)
		  end = i1.endPos;
	  else
		  end = i2.endPos;
	  return new RuleInterval(start,end);
  }

}
