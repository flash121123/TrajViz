package edu.gmu.trajviz.logic;

public class PairDistance{
private int line;
private int column;
private double distance;
public PairDistance(int l, int c, double d){
	if(l<c)
		{
		line = l;
		column = c;
		}
	else
	{
		line = c;
		column = l;
	}
	distance = d;
}
public int getLine(){
	return line;
}
public int getCol(){
	return column;
}
public double getDistance(){
	return distance;
}
}
