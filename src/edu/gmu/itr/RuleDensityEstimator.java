package edu.gmu.itr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import core.Word;
import core.Collections.AWordList;
import core.agi.AGrammarRuleRecord;
import core.agi.AGrammarRules;
import edu.gmu.trajviz.sax.datastructures.SAXRecords;

public class RuleDensityEstimator {

	public RuleDensityEstimator(SAXRecords sd, AGrammarRules ag) {
		
		this.sd = sd;
		this.ag=ag;
	}

	public SAXRecords sd;
	
	public HashMap<Direction<String>,Integer> dlist;
	public AGrammarRules ag;
	
	public static ArrayList<Integer> indexStart;
	public static ArrayList<Integer> dense;
	public static ArrayList<Integer> indexEnd;
	public static ArrayList<Integer> sorted;
	public static ArrayList<Direction<Integer>> az;
	
	
	public void run()
	{
		int layer=0;
		az=new ArrayList<Direction<Integer>>();
		dlist=new HashMap<Direction<String>,Integer>();
		dense=new ArrayList<Integer>();
		for(AGrammarRuleRecord x : ag)
		{
			if(Integer.parseInt(x.getRuleName().split("-")[1])==0)
			{
				layer++;
				continue;
			}
			
			if(layer>1 && x.err()==0) //Potentially repeated motif
				continue;
			
			String[] w=x.getExpandedRuleString().split(" ");
			
			for(int i=1; i<w.length;i++)
			{
				Direction<String> tmp_direct=new Direction<String>(w[i-1],w[i]);
				if(dlist.containsKey(tmp_direct))
				{
					Integer count=dlist.get(tmp_direct);
					count++;
					dlist.put(tmp_direct, count);
				}
				else
				{
					dlist.put(tmp_direct, 1);
				}
			}
		}
		createdenseMap();
	}


	private void createdenseMap() {
		// TODO Auto-generated method stub
		String[] p=ItrSeq.strtoken;
		Integer K=sd.getAllIndices().size();
		indexStart=new ArrayList<Integer>(K);
		dense=new ArrayList<Integer>(K);
		indexEnd=new ArrayList<Integer>(K);
		
		Integer px=0;
		Integer index=-1;
		for(Integer x : sd.getAllIndices())
		{
			index++; //index for call string
			if(x==0) //Jump the first one
				continue;
			
			if(Integer.parseInt(p[index])<0) //Jump break point in start/end
				continue;
			
			if(Integer.parseInt(p[index-1])<0)
				continue;
			
			indexStart.add(px);
			indexEnd.add(x);
			
			Direction<String> direct=new Direction<String>(p[index-1],p[index]); 
			
			if(dlist.containsKey(direct))
				dense.add(dlist.get(direct));
			else
				dense.add(-1);
			px=x;
		}
		
		sorted=new ArrayList<Integer>(dense);
		Collections.sort(sorted);

		boolean flag=false;
		int start=0;
		int end=0;
		 ArrayList<Direction<Integer>> az2=new ArrayList<Direction<Integer>>();
		ArrayList<Integer> az3=new ArrayList<Integer>();
		ArrayList<Integer> az4=new ArrayList<Integer>();
		int Th=2;
		for(int i=0;i<dense.size();i++)
		{
			
			if(dense.get(i)<Th && flag==false)
			{
				start=i;
				flag=true;
				continue;
			}
			
			if(dense.get(i)>=Th && flag==true)
			{
				end=i-1;
				Direction<Integer> d=new Direction<Integer>(indexStart.get(start),indexEnd.get(end));
				az2.add(d);
				az3.add(start-end);
				az4.add(start-end);
				flag=false;
			}
		}
	
		Collections.sort(az3);
		int Kmax=5;
		
		for(int i=0;i<Kmax;i++)
		{
			Integer tmp = az3.get(i);
			int loc=az4.indexOf(tmp);
			az4.set(loc, -1);
			if(az2.get(loc).start-az2.get(loc).end>400)
			{
				i--;
				continue;
			}
			az.add(az2.get(loc));
		}
		
	}


	public static void clear() {
		// TODO Auto-generated method stub
		dense=new ArrayList<Integer>();
		az=new ArrayList<Direction<Integer>>();
		
		
	}
}
