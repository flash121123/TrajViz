package base;

import core.agi.RuleInterval;

/******************************************************************************
 *  Compilation:  javac Interval1D.java
 *  Execution:    java Interval1D
 *  
 *  Interval data type with integer coordinates.
 *
 ******************************************************************************/


public class Interval1D implements Comparable<Interval1D> {
    public final int min;  // min endpoint
    public final int max;  // max endpoint

    // precondition: min <= max
    public Interval1D(int min, int max) {
        if (min <= max) {
            this.min = min;
            this.max = max;
        }
        else throw new RuntimeException("Illegal interval");
    }

    public Interval1D(RuleInterval y) {
		// TODO Auto-generated constructor stub
    	this.min=y.getStart();
    	this.max=y.getEnd();
	}

    
	/**
	 * Notes: the percision is in 10^7
	 * @param min
	 * @param max
	 */
	public Interval1D(Double min, Double max) {
		// TODO Auto-generated constructor stub
		if (min <= max) {
            this.min = (int)(10000000*min);
            this.max = (int)(10000000*max);
        }
        else throw new RuntimeException("Illegal interval");
	}

	public Interval1D(double min) {
		this.min = (int)(10000000*min);
        this.max = (int)(10000000*min)+1;
		// TODO Auto-generated constructor stub
	}

	// does this interval intersect that one?
    public boolean intersects(Interval1D that) {
        if (that.max < this.min) return false;
        if (this.max < that.min) return false;
        return true;
    }

    // does this interval a intersect b?
    public boolean contains(int x) {
        return (min <= x) && (x <= max);
    }

    public int compareTo(Interval1D that) {
        if      (this.min < that.min) return -1;
        else if (this.min > that.min) return +1;
        else if (this.max < that.max) return -1;
        else if (this.max > that.max) return +1;
        else                          return  0;
    }

    public String toString() {
        return "[" + min + ", " + max + "]";
    }

}
