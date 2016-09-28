package edu.gmu.trajviz.logic;

import java.util.ArrayList;

public class Route{
	
	/* Input:
	 * lat & lon
	 * 
	 * ArrayList<Double>,ArrayList<Double>
	 */
	private ArrayList<Double> lat;
	private ArrayList<Double> lon;
	
	
	public Route(){
		lat = new ArrayList<Double>();
		lon = new ArrayList<Double>();
		}
	public ArrayList<Double> getLats(){
		return lat;
	}
	public ArrayList<Double> getLons(){
		return lon;
	}
	public Route(ArrayList<Double> latitudes, ArrayList<Double> longitudes)
	{
		this();
		lat.addAll(latitudes);
		lon.addAll(longitudes);
	}
	public void addLocation(double latitude, double longitude){
		lat.add(latitude);
		lon.add(longitude);
	}
	public void print(){
		for (int i = 0; i<lat.size();i++){
			System.out.println(lat.get(i)+", "+lon.get(i));
		}
	}
	@Override
	public String toString() {
		return "Route [lat=" + lat + ", lon=" + lon + "]";
	}
}