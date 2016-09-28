package edu.gmu.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import base.Interval1D;
import base.IntervalST;
import edu.gmu.trajviz.logic.Location;
import edu.gmu.trajviz.logic.Route;

public class DensityGrid {

	
	/**
	 * Initalization with min-max
	 * 
	 * @param lat_min
	 * @param lat_max
	 * @param lon_min
	 * @param lon_max
	 * @param precision
	 */
	public DensityGrid(double lat_min, double lat_max, double lon_min, double lon_max,double N) {
		super();
		//initialziation
		this.lat_min = lat_min;
		this.lat_max = lat_max;
		this.lon_min = lon_min;
		this.lon_max = lon_max;
		
		//percision for create array
		double p_lat=(lat_max-lat_min)/N;
		double p_lon=(lat_max-lat_min)/N;
		
		this.lat=new ArrayList<Double>();
		this.lon=new ArrayList<Double>();
		
		//Create arraylist latitdue
		
		for(double i=this.lat_min;i<this.lat_max;i=i+p_lat)
		{
			this.lat.add(i);
			
		}
		this.lat.add(this.lat_max);
		
		//Update Lat Grid
		for(int i=0;i<lat.size()-1;i++)
		{
			//Convert to int:
			Interval1D value=new Interval1D(lat.get(i),lat.get(i+1));
			latGrid.put(value, i);
		}
		
		//Create arraylist longtitude
		for(double i=this.lon_min;i<this.lon_max;i=i+p_lon)
		{
			this.lon.add(i);
		}
		this.lon.add(this.lon_max);
		
		//Update Lon Grid
		for(int i=0;i<lon.size()-1;i++)
		{
			//Convert to int:
			Interval1D value=new Interval1D(lon.get(i),lon.get(i+1));
			lonGrid.put(value, i);
		}
		
	}
	
	
	/**
	 * Intialization with array
	 * @param lat
	 * @param lon
	 */
	public DensityGrid(ArrayList<Double> lat, ArrayList<Double> lon) {
		super();
		this.lat = lat;
		this.lon = lon;
		
		this.lat_max=Collections.max(this.lat);
		this.lat_min=Collections.min(this.lat);

		this.lon_max=Collections.max(this.lon);
		this.lon_min=Collections.min(this.lon);
		
	}
	
	
	
	/*
	 * Start of Functional Part
	 * 
	 */
	
	
	/**
	 * @param x: Location Class for a point
	 */
	public void put(Location x)
	{
		Location s=check(x);
		if(density.containsKey(s))
		{
			Integer value=density.get(s);
			value++;
			density.put(s, value);
		}
		else
		{
			density.put(s, 1);
		}
		return;
	}

	

	/**
	 * @param x,y: Coordinate of a point
	 */
	public void put(double x,double y)
	{
		Location loc=new Location(x,y);
		Location s=check(loc);
		if(density.containsKey(s))
		{
			Integer value=density.get(s);
			value++;
			density.put(s, value);
		}
		else
		{
			density.put(s, 1);
		}
		return;
	}
	


	public int getMaxdensity() {
		
		if(maxdensity<0)
		{
			if(density.isEmpty())
				return 0;
			
			ArrayList<Integer> tmp=new ArrayList<Integer>();
			for(Integer x : density.values())
			{
				tmp.add(x);
			}
			maxdensity=Collections.max(tmp);
		}
		
		return maxdensity;
	}


	public HashMap<Location, Integer> getDensity() {
		return density;
	}

	/**
	 * Counting density for each route
	 * @param route
	 */
	public void put(Route route) {
		// TODO Auto-generated method stub
		ArrayList<Double> tmp1 = route.getLats();
		ArrayList<Double> tmp2 = route.getLons();
		Set<Location> setlocs=new HashSet<Location>(); //set for remove deplication
		
		//Get the grid location
		for(int i=0;i<tmp1.size();i++)
		{
			 Location x = check(new Location(tmp1.get(i),tmp2.get(i)));
			 setlocs.add(x);
		}
		
		/*
		if(sl.size()<30)
			return;
			*/
		//Counting
		
		for(Location x : setlocs){put(x);}
		
	}
	
	/*
	 * End of Functional Part
	 * 
	 */
	
	
	/*
	 * Sub-function Part
	 * 
	 */
	
	private Location check(Location x) {
		// TODO Auto-generated method stub
		Interval1D tmp=new Interval1D(x.getLatitude());
		Interval1D res = latGrid.search(tmp);
		tmp=new Interval1D(x.getLongitude());
		Interval1D res2 = lonGrid.search(tmp);
		return new Location(lat.get(latGrid.get(res)),lon.get(lonGrid.get(res2)));
		
	}


	

	
	/*
	 * End Sub-function Part
	 * 
	 */
	

	
	/**
	 * All Parameters used in Grid
	 * 
	 */
	
	ArrayList<Double> lat; //latitude grid important point
	ArrayList<Double> lon; //longtitude grid important point
	
	double lat_min; //latitude minmize value
	double lat_max; //latitude maximze value
	
	double lon_min; //longtitude minmize value
	double lon_max; //longtitude maximze value
	
	/**
	 * For Counting Density
	 */
	HashMap<Location,Integer> density=new HashMap<Location,Integer>();
	int maxdensity=-1;
	IntervalST<Integer> latGrid=new IntervalST<Integer>();
	IntervalST<Integer> lonGrid=new IntervalST<Integer>();
	public int get(Location loc) {
		// TODO Auto-generated method stub
		Location loc2=check(loc);
		if(!density.containsKey(loc2))
			return 0;
		return density.get(loc2);
	}


	/*
	 * End of Parameters Declaration
	 * 
	 */
	
	@Override
	public String toString() {
		return "(" + maxdensity + ")"+" Table=" + density;
	}
	
	
}
