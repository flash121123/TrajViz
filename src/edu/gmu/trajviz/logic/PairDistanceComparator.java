package edu.gmu.trajviz.logic;

import java.util.Comparator;

public class PairDistanceComparator implements Comparator<PairDistance>{

	@Override
	public int compare(PairDistance p1, PairDistance p2) {
		// TODO Auto-generated method stub
		if(p1.getDistance()<p2.getDistance())
			return -1;
		if(p1.getDistance()>p2.getDistance())
			return 1;
		return 0;
	}

}
