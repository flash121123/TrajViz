package edu.gmu.core;

import java.util.ArrayList;

public class Path {
   ArrayList<Double> p1=new ArrayList<Double>();
   ArrayList<Double> p2=new ArrayList<Double>();
   ArrayList<Double> msg=new ArrayList<Double>();
   public Path(ArrayList<Double> px, ArrayList<Double> py)
   {
	   p1=deepcopy(px);
	   p2=deepcopy(py);
	   msg=GenerateMsgArr();
	   
	   return;
   }
   private ArrayList<Double> deepcopy(ArrayList<Double> x)
   {
	   ArrayList<Double> temp=new ArrayList<Double>();
	   for(Double s : x)
	   {
		   temp.add(s);
	   }
	   return temp;
   }
   
   public ArrayList<Double> GenerateMsgArr() {
		ArrayList<Double> d = new ArrayList<Double>();
		for (int i = 0; i < p1.size(); i++) {
			d.add(p1.get(i));
			d.add(p2.get(i));
		}
		return d;

	}
   
   
}
