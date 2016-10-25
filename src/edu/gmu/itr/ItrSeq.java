package edu.gmu.itr;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import base.GIHelper;
import base.Interval1D;
import base.IntervalST;
import core.frame.Interval;
import core.Word;
import core.Collections.AWordList;
import core.agi.AGrammarRuleRecord;
import core.agi.AGrammarRules;
import core.agi.ItrSequitur;
import core.agi.RevisingCut;
import edu.gmu.trajviz.gi.GrammarRuleRecord;
import edu.gmu.trajviz.gi.GrammarRules;
import edu.gmu.trajviz.logic.Route;
import edu.gmu.trajviz.model.SequiturModel;
import core.agi.RuleInterval;
import edu.gmu.trajviz.sax.datastructures.SAXRecords;

public class ItrSeq {

	public static ArrayList<String> rn;
	
	public static String str;
	public int err=3;
	public static AGrammarRules arules;
	public static ArrayList<Double> alt;
	public static ArrayList<ArrayList<Route>> motif4density=new ArrayList<ArrayList<Route>>();  
	public RuleDensityEstimator re;
	public static String[] strtoken;
	private String datafileName;
	public static SAXRecords sd_global;
	
	//Main Function:
	public ItrSeq(String str,String name)
	{
		this.str=str;
		datafileName=name;
	}
	
	public ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>> run(SAXRecords sd) throws Exception
	{
		sd_global=sd;
		AWordList w=new AWordList(str);
		ItrSequitur.restart();
		ItrSequitur.setUniversal_word(w);
		arules=ItrSequitur.run(w);
		Integer t=w.get(0).start;

		while (arules.count() != 1) {	 
			   // System.out.println(arules);
				w=new AWordList(t,arules); //Converting Rule to WordList
				w=RevisingCut.run(w,arules,err); //Revising WordList by Rules
				arules=ItrSequitur.run(w);	//Run Sequitur
				t=w.get(0).start;		
				//System.out.println("Finish Layer: "+arules.getLayer());
		}
		//System.out.println(arules.getLayer());
		
		updateRuleIntervals(arules, sd, alt.size());
		strtoken=str.split(" ");
		re=new RuleDensityEstimator(sd,arules);
		re.run();
		ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>> x = toDisplay();
		 
		return x;
	}

	public ArrayList<Double> getAlt() {
		return alt;
	}

	public void setAlt(ArrayList<Double> lat) {
		this.alt = lat;
	}
	
	public static ArrayList<Integer> countFilter=new ArrayList<Integer>();
	
	public ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>> toDisplay()
	{
		ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>> r=new ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>>();
		IntervalST<String> st=new IntervalST<String>();
		String[] tsStr = strtoken;
		ArrayList<Integer> saxWordsIndexes = new ArrayList<Integer>(sd_global.getAllIndices());
		Set<String> ss=new HashSet<String>();
		HashMap<String,Integer> lens=new HashMap<String,Integer>();
		int layer=0;
		for(AGrammarRuleRecord x : arules)
		{
			if(Integer.parseInt(x.getRuleName().split("-")[1])==0)
			{
				layer++;
				ss.add(x.getRuleName());
				continue;
			}
			
			if(layer>1 && x.err()==0) //Potentially repeated motif
				continue;
			
			
			//push2route(x);
			//Potential false postive alarm
			if(x.err()>2)//>5
			{
				ss.add(x.getRuleName());
				continue;
			}
			
			//too short motif for display
			if(x.getRuleYield()<SequiturModel.minYield)
			{
				ss.add(x.getRuleName());
				continue;
			}
			if(x.getRuleintervels().size()<3)
			{
				ss.add(x.getRuleName());
				continue;
			}
			
			if(x.getRuleLevel()>1)
				System.out.println("Pass High Level: "+x);
			
			
			lens.put(x.getRuleName(), x.getMeanLength());
			
			for(Word token : x.getRuleArray())
			{
				if(token.isrule() || token.isword())
				{
					AGrammarRuleRecord tmp = arules.get(token.ruleU(x));
					ss.add(tmp.getRuleName());
				}
			}
		}
		
		ArrayList<AGrammarRuleRecord> agr=new ArrayList<AGrammarRuleRecord>();
		
		for(AGrammarRuleRecord x : arules)
		{
			if(ss.contains(x.getRuleName()))
				continue;
			agr.add(x);
		}
		
		Collections.sort(agr);
		System.out.println("Rule Size: "+agr.size());
			for(AGrammarRuleRecord x : agr)
			{
				boolean flag=true;
				String[] P = x.getExpandedRuleString().split(" ");
				ArrayList<RuleInterval> yyx = new ArrayList<RuleInterval>();
				ArrayList<Integer> nInt=new ArrayList<Integer>();
				for(int i=0;i<tsStr.length-P.length;i++)
				{
					if(match(tsStr,i,P))
						{
							nInt.add(i);
						    yyx.add(new RuleInterval(saxWordsIndexes.get(i),saxWordsIndexes.get(i+x.getRuleYield()-1),i,i+x.getRuleYield()-1));
						}
				}
				for(int i=0;i<yyx.size();i++)
				{
					
					RuleInterval y = yyx.get(i);
					Interval1D yy=new Interval1D(y);
					 
					if(st.search(yy)==null)
					{
						st.put(yy, x.getRuleName());
					}
					else
					{
						flag=true;
						break;
					}
				}
				if(flag)
				{
					st.remove(x);
				}
			}
			
			Set<String> strSet=new HashSet<String>();
			for(Interval1D xsa : st.searchAll(new Interval1D(0,alt.size())))
			{
				strSet.add(st.get(xsa));
			}
			
			ArrayList<AGrammarRuleRecord> arules2=new ArrayList<AGrammarRuleRecord>();
			for(String rname : strSet)
			{	
				AGrammarRuleRecord x = arules.get(rname);
				arules2.add(x);
			}
			Collections.sort(arules2);
			rn=new ArrayList<String>();
			ArrayList<Integer> lens2 = new ArrayList<Integer>();
			for(AGrammarRuleRecord x : arules2)
			{
				rn.add(x.getExpandedRuleString());
				lens2.add(x.getRuleLength());
				ArrayList<edu.gmu.trajviz.logic.RuleInterval> tmp=new ArrayList<edu.gmu.trajviz.logic.RuleInterval>();
				for(int i=0;i<x.getRuleintervels().size();i++)
				{
					RuleInterval y = x.getRuleintervels().get(i);
					tmp.add(new edu.gmu.trajviz.logic.RuleInterval(y.getStart(),y.getEnd()));
					
				}
				r.add(tmp);
			}
			ArrayList<Integer> count=new ArrayList<Integer>();
			boolean[] flags=new boolean[r.size()];
			for(int i=0;i<flags.length;i++)
				flags[i]=false;
			HashMap<String,ArrayList<edu.gmu.trajviz.logic.RuleInterval>> rst=new HashMap<String,ArrayList<edu.gmu.trajviz.logic.RuleInterval>>();
			for(int i=0;i<rn.size();i++)
			{
				HashMap<String,Integer> hs=new HashMap<String,Integer>();
				
				String tmp1=rn.get(i);
				String[] tt1 = tmp1.split(" ");
				for(int k=1; k<tt1.length;k++)
					if(!hs.containsKey(tt1[k]))
						hs.put(tt1[k], 1);
					else
						hs.put(tt1[k],hs.get(tt1[k])+1);
				
				for(int j=i;j<rn.size();j++)
				{
					if(flags[i]==true)
						break;
					
					
					String tmp2=rn.get(j);
					if(i==j)
					{
					   rst.put(tmp1,new ArrayList<edu.gmu.trajviz.logic.RuleInterval>(r.get(i)));
					   continue;
					}
					if(flags[j]==true)
						continue;
					
					String[] tt2 = tmp2.split(" ");
					
					Integer d=lcss(tt1,tt2);
					
					if(d==Math.min(tt1.length, tt2.length))
					{
						flags[j]=true;
					    rst.get(tmp1).addAll(r.get(j)); 
					    for(int k=1; k<tt2.length;k++)
							if(!hs.containsKey(tt2[k]))
								hs.put(tt2[k], 1);
							else
								hs.put(tt2[k],hs.get(tt2[k])+1);
						
					}
					if(Math.min(tt1.length, tt2.length)>18 && d>Math.min(tt1.length, tt2.length)-5)
					{
						flags[j]=true;
					    rst.get(tmp1).addAll(r.get(j)); 
					    for(int k=1; k<tt2.length;k++)
							if(!hs.containsKey(tt2[k]))
								hs.put(tt2[k], 1);
							else
								hs.put(tt2[k],hs.get(tt2[k])+1);
						
					}
					
				}
				int a=Collections.max(hs.values());
				count.add(a);
			}
		r=new ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>>();
		ArrayList<String> rn2 = new ArrayList<String>();
		
		
		Integer L=SequiturModel.mfthreshold;
		while(rn2.size()>100 || rn2.size()==0)
		{
			L++;
			r=new ArrayList<ArrayList<edu.gmu.trajviz.logic.RuleInterval>>();
			
			rn2 = new ArrayList<String>();
		for(int i=0;i<rn.size();i++)
		{
			String tsp=rn.get(i);
			
			if(!rst.containsKey(tsp))
				continue;
			
			if(rst.get(tsp).size()<=L) //<=2
				continue;
			
			r.add(rst.get(tsp));
			
			//countFilter.add(count.get(i));
			rn2.add(tsp);
		}
		}
		
		rn=new ArrayList<String>();
		rn.addAll(rn2);
		
		return r;
	}
	
	
	private static void push2route(AGrammarRuleRecord x) {
		// TODO Auto-generated method stub
		ArrayList<Route> tmp=new ArrayList<Route>();
		for(RuleInterval xx : x)
		{
			ArrayList<Double> tmp1=new ArrayList<Double>(SequiturModel.lat.subList(xx.getStart(), xx.getEnd()));
			ArrayList<Double> tmp2=new ArrayList<Double>(SequiturModel.lon.subList(xx.getStart(), xx.getEnd()));
			tmp.add(new Route(tmp1,tmp2));
		}
		motif4density.add(tmp);
	}

	/** function lcs **/
    public static int lcss(String[] str1, String[] str2)
    {
        int l1 = str1.length;
        int l2 = str2.length;
 
        int[][] arr = new int[l1 + 1][l2 + 1];
 
        for (int i = l1 - 1; i >= 0; i--)
        {
            for (int j = l2 - 1; j >= 0; j--)
            {
                if (str1[i].equals(str2[j]))
                    arr[i][j] = arr[i + 1][j + 1] + 1;
                else 
                    arr[i][j] = Math.max(arr[i + 1][j], arr[i][j + 1]);
            }
        }
 
        int i = 0, j = 0;
        ArrayList<String> sb = new ArrayList<String>();
        while (i < l1 && j < l2) 
        {
            if (str1[i].equals(str2[j])) 
            {
                sb.add(str1[i]);
                i++;
                j++;
            }
            else if (arr[i + 1][j] >= arr[i][j + 1]) 
                i++;
            else
                j++;
        }
        return sb.size();
    }
    
	
private static boolean match(String[] tsStr, int i, String[] p) {
		// TODO Auto-generated method stub
		for(int j=0;j<p.length;j++)
		{
			if(!tsStr[i+j].equals(p[j]))
				return false;
		}
		return true;
	}


public static int minDistance(String[] word1, String[] word2) {
	int len1 = word1.length;
	int len2 = word2.length;
 
	if(Math.abs(len1-len2)>5)
		return 1000;
	// len1+1, len2+1, because finally return dp[len1][len2]
	int[][] dp = new int[len1 + 1][len2 + 1];
 
	for (int i = 0; i <= len1; i++) {
		dp[i][0] = i;
	}
 
	for (int j = 0; j <= len2; j++) {
		dp[0][j] = j;
	}
 
	//iterate though, and check last char
	for (int i = 0; i < len1; i++) {
		String c1 = word1[i];
		for (int j = 0; j < len2; j++) {
			String c2 = word2[j];
 
			//if last two chars equal
			if (c1.equals(c2)) {
				//update dp value for +1 length
				dp[i + 1][j + 1] = dp[i][j];
			} else {
				int replace = dp[i][j] + 1;
				int insert = dp[i][j + 1] + 1;
				int delete = dp[i + 1][j] + 1;
 
				int min = replace > insert ? insert : replace;
				min = delete > min ? min : delete;
				dp[i + 1][j + 1] = min;
			}
		}
	}
 
	return dp[len1][len2];
}

public static void updateRuleIntervals(AGrammarRules rules,
		SAXRecords saxFrequencyData, int originalLength ) {
	ArrayList<Integer> saxWordsIndexes = new ArrayList<Integer>(saxFrequencyData.getAllIndices());
	//ArrayList<Integer> indexesInR0 = new ArrayList<Integer>(saxFrequencyData.getAllIndices());
//	System.out.println("saxwordsIndexes: "+saxWordsIndexes);
	for (AGrammarRuleRecord ruleContainer : rules) {
	//	System.out.println("minmaxLenth: "+ruleContainer.minMaxLengthAsString());
	//	System.out.println("ruleIntervals: "+ruleContainer.getRuleIntervals());
	      // here we construct the array of rule intervals
	      ArrayList<RuleInterval> resultIntervals = new ArrayList<RuleInterval>();
	      ArrayList<RuleInterval> r0Intervals = new ArrayList<RuleInterval>();
	      // array of all words of this rule expanded form
	      // String[] expandedRuleSplit = ruleContainer.getExpandedRuleString().trim().split(" ");
	     
	      int expandedRuleLength =ruleContainer.getRuleYield();
	//      System.out.println("getExStr: "+ruleContainer.getExpandedRuleString());
     
	      // the auxiliary array that keeps lengths of all rule occurrences
	      int[] lengths = new int[ruleContainer.getOccurrences().size()];
	      int lengthCounter = 0;
	      //int r0LengthCounter = 0 ;
	      // iterate over all occurrences of this rule
	      // the currentIndex here is the position of the rule in the input string
	      //
	      for (Integer currentIndex : ruleContainer.getOccurrences()) {

	        // System.out.println("Index: " + currentIndex);
	        // String extractedStr = "";

	        // what we do here is to extract the positions of sax words in the real time-series
	        // by using their positions at the input string
	        //
	        // int[] extractedPositions = new int[expandedRuleSplit.length];
	        // for (int i = 0; i < expandedRuleSplit.length; i++) {
	        // extractedStr = extractedStr.concat(" ").concat(
	        // saxWordsToIndexesMap.get(saxWordsIndexes.get(currentIndex + i)));
	        // extractedPositions[i] = saxWordsIndexes.get(currentIndex + i);
	        // }

	        int startPos = saxWordsIndexes.get(currentIndex);
	        int endPos;
	        
	        
	        
	        /* modified here to adapt multiple runs  -qz on 11022015
	         * */
	        if (currentIndex+expandedRuleLength>=saxWordsIndexes.size())
	        	endPos = originalLength-1;
	        else
	        	endPos = saxWordsIndexes.get(currentIndex+expandedRuleLength)-1;
	        	
	        	
	        
	        
	       // System.out.println("expandedRuleLength: "+expandedRuleLength);
	        /*
	        if ((currentIndex + expandedRuleLength) >= saxWordsIndexes.size()) {
	          endPos = originalLength - 1;
	        }
	        else {
	        	endPos = Long.valueOf(Math.round(startPos + expandedRuleLength)).intValue();
	        	/*
	        	double step = (double) originalLength;
	           // double step = (double) originalLength / (double) saxPAASize;
	            endPos = Long.valueOf(Math.round(startPos + expandedRuleLength * step)).intValue();
	          
	        }
*/
	        resultIntervals.add(new RuleInterval(startPos, endPos,-1,-1));

	        lengths[lengthCounter] = endPos - startPos;
	        lengthCounter++;
	      }
	      
	      /*
	       * add by qingzhe to set rule intervals in R0
	       * 
	       */
	      for (Integer r0Index : ruleContainer.getOccurrences()) {

		        // System.out.println("Index: " + currentIndex);
		        // String extractedStr = "";

		        // what we do here is to extract the positions of sax words in the real time-series
		        // by using their positions at the input string
		        //
		        // int[] extractedPositions = new int[expandedRuleSplit.length];
		        // for (int i = 0; i < expandedRuleSplit.length; i++) {
		        // extractedStr = extractedStr.concat(" ").concat(
		        // saxWordsToIndexesMap.get(saxWordsIndexes.get(currentIndex + i)));
		        // extractedPositions[i] = saxWordsIndexes.get(currentIndex + i);
		        // }

		        int startPos = saxWordsIndexes.get(r0Index);
		        int endPos;
		        
		        
		        
		        /* modified here to adapt multiple runs  -qz on 11022015
		         * */
		        if (r0Index+expandedRuleLength>=saxWordsIndexes.size())
		        	endPos = originalLength-1;
		        else
		        	endPos = saxWordsIndexes.get(r0Index+expandedRuleLength)-1;
		  
		      }
	      
	      
	      if (0 == ruleContainer.getRuleNumber()) {
	        resultIntervals.add(new RuleInterval(0, originalLength - 1));
	        r0Intervals.add(new RuleInterval(0, originalLength -1));
	        lengths = new int[1];
	        lengths[0] = originalLength;
	      }
	      ruleContainer.setRuleintervels(resultIntervals);
	      //ruleContainer.setR0Intervals(r0Intervals);
	      ruleContainer.setMeanLength((int) GIHelper.mean(lengths));
	      ruleContainer.setMinMaxLength(lengths);
	  //    ruleContainer.setMinMaxLength(lengths);
	  //    System.out.println("minmaxLenth: "+ruleContainer.minMaxLengthAsString());
	  //    System.out.println("ruleIntervals: "+ruleContainer.getRuleIntervals());
         
          }

}

public static ArrayList<Integer> getCount() {
	return countFilter;
}
}
