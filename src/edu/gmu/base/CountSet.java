package edu.gmu.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class CountSet {
	public HashMap<String,ArrayList<Integer>> h;
	
	public CountSet()
	{
		h=new HashMap<String,ArrayList<Integer>>();
	}
	
	public void put(String c,Integer r)
	{
		if(h.containsKey(c)){
			h.get(c).add(r);
		}
		else
		{
			ArrayList<Integer> k=new ArrayList<Integer>();
			k.add(r);
		    h.put(c, k);
		}
	}

	public void put(String c, ArrayList<Integer> r) {
		// TODO Auto-generated method stub
		if(h.containsKey(c)){
			h.get(c).addAll(r);
		}
		else
		{
			ArrayList<Integer> k=new ArrayList<Integer>();
			k.addAll(r);
		    h.put(c, k);
		}
	}
	
	
	@Override
	public String toString() {
		return "CountSet [" + h + "]";
	}

	public ArrayList<Integer> get(String rule) {
		// TODO Auto-generated method stub
		return h.get(rule);
	}

	public boolean containsKey(Object key) {
		return h.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return h.containsValue(value);
	}

	public ArrayList<Integer> get(Object key) {
		return h.get(key);
	}

	public Set<String> keySet() {
		return h.keySet();
	}

	public int size() {
		return h.size();
	}

	public Collection<ArrayList<Integer>> values() {
		return h.values();
	}

	
	
	
}
