package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import base.Interval;

public class StatInterval {
	// Put all analysis for intervals here
	
	HashMap<String, ArrayList<Interval>> h = new HashMap<String, ArrayList<Interval>>();
	
	public StatInterval(HashMap<String, ArrayList<Interval>> hs)
	{
		h=hs;
		return;
	}
	
	public HashMap<String, ArrayList<Interval>> ReturnLayerRules(Integer layer)
	{
		//Caution: Swallow Copy
		HashMap<String, ArrayList<Interval>> ht = new HashMap<String, ArrayList<Interval>>();
		Set<String> s=h.keySet();
	    for(String str : s)
	    {
	    	if(str.substring(0,1).equals(layer.toString()))
	    		ht.put(str, h.get(str));
	    }
		return ht;
		
	}
}
