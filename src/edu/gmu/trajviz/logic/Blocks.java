package edu.gmu.trajviz.logic;

import java.util.ArrayList;

public class Blocks {
//	public int n;   //means a area with n*n blocks
	public int nLat;
	public int nLon;
	public double latMin;
	public double lonMin;
	public double latMax;
	public double lonMax;
	public double latRangeKm;
	public double lonRangeKm;
	public double latCut;
	public double latCutKm;
	public double lonCut;
	public double lonCutKm;
	private int size;
	private double coef;
	public ArrayList<Block> blocks;
	public Blocks(){
	//	n = 0;
		size = 0;
		blocks = new ArrayList<Block>();
	}
	public Blocks(int n, double laMin, double laMax, double loMin, double loMax){
		
	//	this.n = n;
		
		latMin = laMin;
		//deal with the max boundary problem, e.g. 8/1=8, however, the latId are from 0 to 7.
		latMax = laMax+0.000000001;
		lonMin = loMin;
		lonMax = loMax+0.000000001;
		latRangeKm = distFrom(latMax,lonMax,latMin,lonMax);
		lonRangeKm = distFrom((latMax+latMin)/2, lonMax,(latMax+latMin)/2, lonMin);
		coef = latRangeKm/lonRangeKm;
		nLat = (int)(n*coef);
		nLon = n;
		latCut = (latMax-latMin)/nLat;
		lonCut = (lonMax-lonMin)/nLon;
		latCutKm = latRangeKm/nLat;
		lonCutKm = lonRangeKm/nLon;
		System.out.println("LatCut:latCutKm : nLat = "+ latCut+" : "+latCutKm+" : "+nLat);
		System.out.println("LonCut:latCutKm : nLon = "+":"+lonCut +" : "+lonCutKm+" : "+nLon);

		size = nLat*nLon;

		blocks = new ArrayList<Block>();
		for (int i=0;i<size;i++)
		{
			blocks.add(new Block(i,nLon,latCut,lonCut,latMin,lonMin));
		}
	//	printBlockMap();

	}
	public void addPoint2Block(Location point){
		blocks.get(findBlockIdForPoint(point)).addPoint(point);
		
	}
	public int findBlockIdForPoint(Location point){
		 
		//System.out.println("latID: "+(Math.floor((point.latitude-latMin)/latCut))+" lonID: "+(Math.floor((point.longitude-lonMin)/lonCut)));
		if(point.latitude<-90||point.longitude<-180)
			return (int)(point.latitude);
		return (int)(Math.floor((point.latitude-latMin)/latCut))*nLon+(int)(Math.floor((point.longitude-lonMin)/lonCut));
	}
	public Block findBlockById(int id){
		return blocks.get(id);
	}
	public Block findBlockByLocation(Location point){
		return blocks.get(findBlockIdForPoint(point));
	}
	public void printBlockMap(){
		int i = 0;
		for (int k = 0; k<nLat; k++)
		{
			for (int j = 0; j<nLon; j++)
				{
					System.out.print(blocks.get(i).id+"\t");
					i++;
				}
		System.out.println();
		}
	}
	
	/*
	 *  compute distance in Kilometers between cells
	 */
	public double distance(Integer block1, Integer block2) {
		int latBlock1 = block1/nLon;
		double lat1 = latMin+latCut*latBlock1+0.5*latCut;
		int lonBlock1 = block1%nLon;
		double lon1 = lonMin+lonCut*lonBlock1+0.5*lonCut;
		int latBlock2 = block2/nLon;
		double lat2 = latMin+latCut*latBlock2+0.5*latCut; 
		int lonBlock2 = block2%nLon;
		double lon2 = lonMin+lonCut*lonBlock2+0.5*lonCut;
		double distance = distFrom(lat1,lon1,lat2,lon2);
		//double distance1 = Math.sqrt((latBlock1-latBlock2)*(latBlock1-latBlock2)*latCutKm*latCutKm+(lonBlock1-lonBlock2)*(lonBlock1-lonBlock2)*lonCutKm*lonCutKm);
	//	System.out.println("block1:block2:dis1:dis2"+"   "+block1+"("+latBlock1+","+lonBlock2+")"+": "+block2+"   "+distance+" : "+distance1);
		return distance;
	}
	public int latBlockCount(Integer b1,Integer b2){
		int latBlock1 = b1/nLon;
	//	int lonBlock1 = b1%nLon;
		
		int latBlock2 = b2/nLon;
	//	int lonBlock2 = b2%nLon;
		
		return (Math.abs(latBlock2-latBlock1));
	}
	public int lonBlockCount(Integer b1,Integer b2){
//		int latBlock1 = b1/nLon;
		int lonBlock1 = b1%nLon;
		
	//	int latBlock2 = b2/nLon;
		int lonBlock2 = b2%nLon;
		
		return (Math.abs(lonBlock2-lonBlock1));
	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371.0; //3958.75 miles or 6371.0 kilometers
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	    }
}
