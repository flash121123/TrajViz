package edu.gmu.itr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedSet;

import base.WriteFile;
import core.Word;
import core.Collections.AWordList;
import core.agi.AGrammarRuleRecord;
import core.agi.AGrammarRules;
import edu.gmu.trajviz.model.SequiturModel;
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
	public static ArrayList<Integer> dense=new ArrayList<Integer>();
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
		ArrayList<String> tmp=new ArrayList<String>();
		for(String px : p)
		{
			tmp.add(px);
		}
		Integer px=0;
		ArrayList<Integer> indices = new ArrayList<Integer>(sd.getAllIndices());
		for(int index=0; index<indices.size()-1; index++)
		{
			indexStart.add(indices.get(index));
			indexEnd.add(indices.get(index+1));
			
			if(Integer.parseInt(p[index])<0) //Jump break point in start/end
				{
				dense.add(10000);
				continue;
				}
			
			if(Integer.parseInt(p[index+1])<0)
			{
				dense.add(10000);
				continue;
			}
			
			
			
			Direction<String> direct=new Direction<String>(p[index],p[index+1]); 
			
			if(dlist.containsKey(direct))
				dense.add(dlist.get(direct));
			else
				dense.add(-1);
		}
		dense.add(dense.get(dense.size()-1));
		sorted=new ArrayList<Integer>(dense);
		Collections.sort(sorted);

		boolean flag=false;
		int start=0;
		int end=0;
		 ArrayList<Direction<Integer>> az2=new ArrayList<Direction<Integer>>();
		ArrayList<Integer> az3=new ArrayList<Integer>();
		ArrayList<Integer> az4=new ArrayList<Integer>();
		ArrayList<Integer> tz=new ArrayList<Integer>();
		int Th=SequiturModel.anomalythreshold;
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
				tz.add(start);
				az3.add(start-end);
				az4.add(start-end);
				flag=false;
			}
		}
	
		Collections.sort(az3);
		int Kmax=5;
		for(int i=0;i<Kmax;i++)
		{
			Integer tmp2 = az3.get(i);
			int loc=az4.indexOf(tmp2);
			az4.set(loc, -1);
			if(az2.get(loc).start-az2.get(loc).end>400)
			{
				i--;
				continue;
			}
			for(int k=0;k<-tmp2;k++)
			{
				System.out.println(tmp.get(k+loc));
				
			}
			System.out.println("Next");
			
			az.add(az2.get(loc));
		}
		/*
		WriteFile wr=new WriteFile("anomaly");
		for(Direction<Integer> atmp : az)
		{
			wr.write(SequiturModel.lon, SequiturModel.lat, atmp);
		}
		*/
	}


	public static void clear() {
		// TODO Auto-generated method stub
		dense=new ArrayList<Integer>();
		az=new ArrayList<Direction<Integer>>();
		
		
	}
}