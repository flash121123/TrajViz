package base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class AppendSet<T> implements Iterable<ArrayList<T>> {
	public HashMap<String,ArrayList<T>> h;
	
	public AppendSet()
	{
		h=new HashMap<String,ArrayList<T>>();
	}
	
	public void put(String c,T r)
	{
		if(h.containsKey(c)){
			h.get(c).add(r);
		}
		else
		{
			ArrayList<T> k=new ArrayList<T>();
			k.add(r);
		    h.put(c, k);
		}
	}

	public void put(String c, ArrayList<T> r) {
		// TODO Auto-generated method stub
		if(h.containsKey(c)){
			h.get(c).addAll(r);
		}
		else
		{
			ArrayList<T> k=new ArrayList<T>();
			k.addAll(r);
		    h.put(c, k);
		}
	}
	
	
	@Override
	public String toString() {
		return "CountSet [" + h + "]";
	}

	public ArrayList<T> get(String rule) {
		// TODO Auto-generated method stub
		return h.get(rule);
	}

	public boolean containsKey(Object key) {
		return h.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return h.containsValue(value);
	}

	public ArrayList<T> get(Object key) {
		return h.get(key);
	}

	public Set<String> keySet() {
		return h.keySet();
	}

	public int size() {
		return h.size();
	}

	public Collection<ArrayList<T>> values() {
		return h.values();
	}

	@Override
	public Iterator<ArrayList<T>> iterator() {
		// TODO Auto-generated method stub
		return h.values().iterator();
	}

	
	
	
}
