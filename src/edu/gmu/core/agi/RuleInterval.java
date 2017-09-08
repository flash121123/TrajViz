package edu.gmu.core.agi;

import gmu.edu.core.Collections.AWordList;
import gmu.edu.core.frame.Interval;

public class RuleInterval implements Interval {

	int startPos=-1;
	int endPos=-1;
	Integer id=-1;
	int StrStartPos=-1;
	int StrEndPos=-1;
	Integer coverage=-1;
	
	public RuleInterval(int a, int b)
	{
		StrStartPos=a;
		StrEndPos=b;
	}
	
	public RuleInterval(int a, int b, int c,int d)
	{
		startPos=a;
		endPos=b;
		StrStartPos=c;
		StrEndPos=d;
	}
	
	public RuleInterval(RuleInterval r) {
		// TODO Auto-generated constructor stub
		this.startPos=r.startPos;
		this.endPos=r.endPos;
		this.StrEndPos=r.StrEndPos;
		this.StrStartPos=r.StrStartPos;
		this.id=r.id;
	}	

	public RuleInterval(AWordList x) {
		// TODO Auto-generated constructor stub
		this.StrEndPos=x.get(x.size()-1).getEnd();
		this.StrStartPos=x.get(0).getStart();
	}

	@Override
	public void setStart(int startPos) {
		// TODO Auto-generated method stub
		this.startPos=startPos;
	}

	@Override
	public int getStart() {
		// TODO Auto-generated method stub
		return startPos;
	}

	@Override
	public void setEnd(int endPos) {
		// TODO Auto-generated method stub
		this.endPos=endPos;
	}

	@Override
	public int getEnd() {
		// TODO Auto-generated method stub
		return this.endPos;
	}

	public int getStrStartPos() {
		return StrStartPos;
	}

	public void setStrStartPos(int strStartPos) {
		StrStartPos = strStartPos;
	}

	public int getStrEndPos() {
		return StrEndPos;
	}

	public void setStrEndPos(int strEndPos) {
		StrEndPos = strEndPos;
	}

	public int getLength() {
	    return this.endPos - this.startPos;
	  }

	public int getSLength() {
	    return this.StrEndPos - this.StrEndPos;
	  }
/*
	public String toString() {
	    return "["+ StrStartPos+"(" + startPos + ") - "+StrEndPos+"(" + endPos + ")]";
	  }
*/
	public String toString() {
	    //return "["+ StrStartPos+"(" + startPos + ") - "+StrEndPos+"(" + endPos + ")]";
	    return "["+ startPos + " - "+ endPos + "]";

	    //return "<label class=\"btn btn-primary\"> <input type=\"checkbox\" value=<INDEXVALUE> autocomplete=\"off\"> "+StrStartPos + " - "+ StrEndPos +"  <span class=\"badge\">"+this.id+"</span>"+"</label>";
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
	    if (StrEndPos != other.StrEndPos)
	      return false;
	    if ( StrStartPos != other.StrStartPos)
		      return false;
	    return true;
	  }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCoverage() {
		return coverage;
	}

	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}
}
