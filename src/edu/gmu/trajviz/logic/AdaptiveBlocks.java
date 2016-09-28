package edu.gmu.trajviz.logic;

import java.util.*;

public class AdaptiveBlocks {
	public int alphabetSize;   //means a area with n*n blocks

	public double[] latCutPoints;
	public double[] lonCutPoints;
	public double latMin;
	public double lonMin;
	public double latMax;
	public double lonMax;
	

	public static double clatu=60.799487;
	public static double clatl=20.727834;
	public static double clonu=-180.522464;
	public static double clonl=-100.370029;
	
	public AdaptiveBlocks(int n, ArrayList<Double> latOri1, ArrayList<Double> lonOri1){
		
		this.alphabetSize = n;
		ArrayList<Double> latOri = new ArrayList<Double> ();
		ArrayList<Double> lonOri = new ArrayList<Double> ();
		int K=20;
		for (int i=0; i<latOri1.size();i++) {
			
			
			Double lat=latOri1.get(i);
			Double lon=lonOri1.get(i);
			
			if(Double.valueOf(lat)<-90) 
				continue;
			if(Double.valueOf(lon)<-180) 
				continue;
			latOri.add(lat);
		
			lonOri.add(lon);
		}
		
		Collections.sort(latOri);
		Collections.sort(lonOri);
		double sumOfCount = latOri.size();
		latMin = latOri.get(0);
		lonMin = lonOri.get(0);
		latMax = latOri.get(latOri.size()-1);
		lonMax = lonOri.get(lonOri.size()-1);
		
	    double freq = sumOfCount * 1.0 / n;
	    latCutPoints = new double[n - 1+K];
	    lonCutPoints = new double[n - 1+K];
	    double counter = 0;
	    int cpindex = K/2;
	    
	    for (int i = 0; i < latOri.size() - 1; i++) {	    	
	    	counter += 1;

			if (counter >= freq) {
				latCutPoints[cpindex] = (latOri.get(i) + latOri.get(i+1)) / 2;
			  cpindex++;
			  counter = counter - freq;
			}
	    }
	    double step=(latCutPoints[K/2]-latMin)/(K/2+1);
	    
	    for(int k=0;k<K/2;k++)
	    {
	    latCutPoints[k]=latMin+step*(k+1);
	    }
	    
	    step=(latMax-latCutPoints[latCutPoints.length-1-K/2])/(K/2+1);
	    for(int k=0;k<K/2;k++)
	    {
	    	int l=latCutPoints.length-1;
	    latCutPoints[l-k]=latMax-step*(k+1);
	    }
	    
	    
	    counter = 0;
	    cpindex = K/2;
	    
	    for (int i = 0; i < lonOri.size() - 1; i++) {	    	
	    	counter += 1;

			if (counter >= freq) {
				lonCutPoints[cpindex] = (lonOri.get(i) + lonOri.get(i+1)) / 2;
			  cpindex++;
			  counter = counter - freq;
			}
	    }
	    
	    step=(lonCutPoints[K/2]-lonMin)/(K/2+1);
	    
	    for(int k=0;k<K/2;k++)
	    {
	    lonCutPoints[k]=lonMin+step*(k+1);
	    }
	    
	    step=(lonMax-lonCutPoints[lonCutPoints.length-1-K/2])/(K/2+1);
	    for(int k=0;k<K/2;k++)
	    {
	    	int l=lonCutPoints.length-1;
	    	lonCutPoints[l-k]=lonMax-step*(k+1);
	    }
	    
	    System.out.println(lonCutPoints);
	    this.alphabetSize=lonCutPoints.length+1;
	}
	
	 

	public int findBlockIdForPoint(Location point){
		double latitude = point.latitude;
		double longitude = point.longitude;
		if(point.latitude<-90||point.longitude<-180)
			return (int)(point.latitude);
		int latId = latCutPoints.length;
		int lonId = lonCutPoints.length;
		for (int i = 0; i < latCutPoints.length; i++) {	

			if (latitude <= latCutPoints[i]) {
				latId = i;
				break;
			}
	    }
		for (int i = 0; i < lonCutPoints.length; i++) {	

			if (longitude <= lonCutPoints[i]) {
				lonId = i;
				break;
			}
	    }
		
		return latId*alphabetSize + lonId + 1;
	}

	
	/*
	 *  compute distance in Kilometers between cells
	 */
	public double distance(Integer block1, Integer block2) {
		int latBlock1 = (block1-1)/alphabetSize;
		int lonBlock1 = (block1-1)%alphabetSize;
		int latBlock2 = (block2-1)/alphabetSize;
		int lonBlock2 = (block2-1)%alphabetSize;
		double lat1,lon1,lat2,lon2;
		if (latBlock1 == 0) {
			lat1 = (latMin+latCutPoints[0])/2;
		} else if (latBlock1 == (alphabetSize-1)) {
			lat1 = (latMax+latCutPoints[alphabetSize-2])/2;
		} else {
			lat1 = (latCutPoints[latBlock1-1]+latCutPoints[latBlock1])/2;
		}
		if (latBlock2 == 0) {
			lat2 = (latMin+latCutPoints[0])/2;
		} else if (latBlock2 == (alphabetSize-1)) {
			lat2 = (latMax+latCutPoints[alphabetSize-2])/2;
		} else {
			//if(latBlock2==74) System.out.println(latBlock2);
			lat2 = (latCutPoints[latBlock2-1]+latCutPoints[latBlock2])/2;
		}
		if (lonBlock1 == 0) {
			lon1 = (lonMin+lonCutPoints[0])/2;
		} else if (lonBlock1 == (alphabetSize-1)) {
			lon1 = (lonMax+lonCutPoints[alphabetSize-2])/2;
		} else {
			lon1 = (lonCutPoints[lonBlock1-1]+lonCutPoints[lonBlock1])/2;
		}
		if (lonBlock2 == 0) {
			lon2 = (lonMin+lonCutPoints[0])/2;
		} else if (lonBlock2 == (alphabetSize-1)) {
			lon2 = (lonMax+lonCutPoints[alphabetSize-2])/2;
		} else {
			lon2 = (lonCutPoints[lonBlock2-1]+lonCutPoints[lonBlock2])/2;
		}
		
		double distance = distFrom(lat1,lon1,lat2,lon2);
		return distance;
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
	
	public int latSpan(double lat1, double lat2) {
		int latId1 = latCutPoints.length;
		int latId2 = latCutPoints.length;
		for (int i = 0; i < latCutPoints.length; i++) {	

			if (lat1 <= latCutPoints[i]) {
				latId1 = i;
				break;
			}
	    }
		for (int i = 0; i < latCutPoints.length; i++) {	

			if (lat2 <= latCutPoints[i]) {
				latId2 = i;
				break;
			}
	    }
		return Math.abs(latId1-latId2);
	}
	
	public int lonSpan(double lon1, double lon2) {
		int lonId1 = lonCutPoints.length;
		int lonId2 = lonCutPoints.length;
		for (int i = 0; i < lonCutPoints.length; i++) {	

			if (lon1 <= lonCutPoints[i]) {
				lonId1 = i;
				break;
			}
	    }
		for (int i = 0; i < lonCutPoints.length; i++) {	

			if (lon2 <= lonCutPoints[i]) {
				lonId2 = i;
				break;
			}
	    }
		return Math.abs(lonId1-lonId2);
	}
	
	public int latBlockCount(Integer b1,Integer b2){
		int latBlock1 = (b1-1)/alphabetSize;
	//	int lonBlock1 = b1%nLon;
		
		int latBlock2 = (b2-1)/alphabetSize;
	//	int lonBlock2 = b2%nLon;
		
		return (Math.abs(latBlock2-latBlock1));
	}
	
	public int lonBlockCount(Integer b1,Integer b2){
//		int latBlock1 = b1/nLon;
		int lonBlock1 = (b1-1)%alphabetSize;
		
	//	int latBlock2 = b2/nLon;
		int lonBlock2 = (b2-1)%alphabetSize;
		
		return (Math.abs(lonBlock2-lonBlock1));
	}
}


