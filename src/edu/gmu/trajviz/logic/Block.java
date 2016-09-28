package edu.gmu.trajviz.logic;

import java.util.ArrayList;

public class Block {
	public int id;
	public int latId;
	public int lonId;
	public double latBlockMin;
	public double lonBlockMin;
	public double latBlockMax;
	public double lonBlockMax;
	public ArrayList<Location> points;
	public int size;  // a huge area divided into size*size blocks
	public Block(){
		// just an empty block;
		id = 0;
		latId = 0;
		lonId = 0;
		size = 0;
		points = new ArrayList<Location>();
	}
	/* doesn't use for now
	public Block(int latId, int lonId, int size){
		this.latId = latId;
		this.lonId = lonId;
		this.size = size;
		id = latId*size+lonId;
		points = new ArrayList<Location>();
	}
	*/
	public Block(int id, int size,double latCut, double lonCut,double latMin, double lonMin){
		this.id = id;
		this.size = size;
		this.latId = id/size;
		this.lonId = id%size;
		latBlockMin = latMin + this.latId*latCut;
		latBlockMax = latBlockMin + latCut;
		lonBlockMin = lonMin + this.lonId*lonCut;
		lonBlockMax = lonBlockMin + lonCut;
		points = new ArrayList<Location>();
	}
	public void addPoint(Location point){
		points.add(point);
	}
}
