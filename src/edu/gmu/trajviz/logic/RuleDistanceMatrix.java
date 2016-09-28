package edu.gmu.trajviz.logic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import edu.gmu.trajviz.gi.GrammarRules;
import edu.gmu.trajviz.model.SequiturModel;

public class RuleDistanceMatrix {

public double[][] matrix;
private double minDistance;
private int[] minPair = new int[2];
private GrammarRules rules;
//private int minBlocks;
private double minLink;
public RuleDistanceMatrix(){}
public ArrayList<Integer> filter;
public PriorityQueue<PairDistance> pq; 
private PairDistanceComparator comparator;
@SuppressWarnings("unchecked")
public RuleDistanceMatrix(AdaptiveBlocks blocks, GrammarRules rules, ArrayList<Integer> filter, int minBlocks,double minLink){
	//this.minBlocks = minBlocks;
	this.filter = filter;
	this.minLink = minLink;
	/*
	for (int i=0; i<filter.size();i++)
	System.out.println(i+" : "+filter.get(i)+" ");
	*///System.out.println();
	matrix = new double[filter.size()][filter.size()];
	//matrix[0][0] =100;// Double.MAX_VALUE;
	comparator = new PairDistanceComparator();
	pq= new PriorityQueue<PairDistance>(filter.size()*filter.size()/2, comparator);
	minDistance = 100;//Double.MAX_VALUE;
	minPair[0] = 0;
	minPair[1] = 0;
	//int line = 0;
	//int col = 0;
	for(int i = 0; i<filter.size();i++ )
		for(int j = i+1; j<filter.size();j++){
			if(rules.get(filter.get(i)).frequencyInR0()>2&&rules.get(filter.get(j)).frequencyInR0()>2)
			{
				String rule1 = parseRule(rules.getRuleRecord(filter.get(i)).getExpandedRuleString());
	
				String rule2 = parseRule(rules.getRuleRecord(filter.get(j)).getExpandedRuleString());
				
			//	String rule1 = rules.getRuleRecord(filter.get(i)).getExpandedRuleString();
			//	String rule2 = rules.getRuleRecord(filter.get(j)).getExpandedRuleString();

		//	matrix[0][j] = 100;//Double.MAX_VALUE;
		//	matrix[i][0] = 100;//Double.MAX_VALUE;
			
			/*
			 * DTW Distance	
			*/ 
		//	matrix[i][j] = avgDTWDistance(blocks, toArrayList(rule1), toArrayList(rule2));
			
			matrix[i][j] = lcssDistance(blocks,toArrayList(rule1),toArrayList(rule2));
			matrix[j][i] = matrix[i][j];
			if(matrix[i][j]>0&&matrix[i][j]<minLink){//&&matrix[i][j]<minDistance){
				pq.add(new PairDistance(i,j,matrix[i][j]));
		//		System.out.println("distance = "+matrix[i][j]);
		//		System.out.println("Rule 1: "+rule1);
			//	System.out.println("Rule 2: "+rule2);
				
			/*
				minDistance = matrix[i][j];
				minPair[0] = i;
				minPair[1] = j;
				*/
			}
			}
			//if(rules.get(i).)
		}
	this.rules = rules;
	printMatrix(matrix);
}




public static String parseRule(String string) {
	StringBuffer sb = new StringBuffer();
	//System.out.println("string: "+string);
	ArrayList<String> sa = new ArrayList<String>();
	String[] stringArray = string.split(" ");
	for (String s:stringArray){
		if (s.charAt(0)=='I')
		{
			if(s.contains("r")){
				int rIndex = s.indexOf("r");
				Integer iteration = Integer.valueOf(s.substring(1, rIndex));
				Integer rule = Integer.valueOf(s.substring(rIndex+1));
			//	System.out.println("s: "+s+" iteration: "+iteration+" rule: "+rule);
				String subRule = parseRule(SequiturModel.allRules.get(iteration).get(rule).getExpandedRuleString());
				sa.add(subRule);
		//		System.out.println(s+" = "+subRule );
				
			}
			else if(s.contains("C")){
				int cIndex = s.indexOf("C");
				Integer iteration = Integer.valueOf(s.substring(1, cIndex));
				Integer cluster = Integer.valueOf(s.substring(cIndex+1));
			//	System.out.println("s: "+s+" iteration: "+iteration+" cluster: "+cluster);
				Integer ruleInCluster = (Integer)SequiturModel.allClusters.get(iteration).get(cluster).toArray()[0];
				String subRule = parseRule(SequiturModel.allRules.get(iteration).get(ruleInCluster).getExpandedRuleString());
				sa.add(subRule);
			//	System.out.println(s+" = "+subRule );

			}
		}
		else if (s.charAt(0)=='R'){
			throw new IllegalArgumentException("expect 'I' encounter 'R'");
		}
		
		else	//Base Case
		{
			Integer test = Integer.valueOf(s);
			sa.add(s);
	//		System.out.println("s: "+ s);
		}
	}
	for (int i = 0; i<sa.size()-1;i++){
		sb.append(sa.get(i));
		sb.append(" ");
	}
	if(sa.size()>0)
	   sb.append(sa.get(sa.size()-1));
	//System.out.println("sb: "+sb.toString());
	return sb.toString();
}
private void printMatrix(double[][] matrix) {
	NumberFormat formatter = new DecimalFormat("#0.00");     
/*	
	for(int i=0; i<matrix.length; i++)
	{
		for(int j = 0; j<matrix[0].length;j++)
		{
			System.out.print(formatter.format(matrix[i][j])+"   ");
		}
	System.out.println();	

	}
	*/
	//System.out.println("minDistance"+pq.peek().getDistance());
//	System.out.println("minPair: "+pq.peek().getLine()+", "+filter.get(pq.peek().getLine())+";"+pq.peek().getCol()+","+filter.get(pq.peek().getCol()));
	//System.out.println("rule1 :"+rules.getRuleRecord(filter.get(pq.peek().getLine()))+" Expand String: "+rules.get(filter.get(pq.peek().getLine())).getExpandedRuleString());
	//System.out.println("rule2 :"+rules.getRuleRecord(filter.get(pq.peek().getCol()))+" Expand String: "+rules.get(filter.get(pq.peek().getCol())).getExpandedRuleString());
	System.out.println("Matrix size: "+filter.size());
}
public double[][] getMatrix(){
	return matrix;
}
public double getMinDistance(){
	return minDistance;
}
public int[] getMinPair(){
	return minPair;
}
public static ArrayList<Integer> toArrayList(String rule) {
	String[] strArray = rule.split(" ");
	ArrayList<Integer> al = new ArrayList<>(); 
	for (int i = 0; i<strArray.length;i++){
		al.add(Integer.valueOf(strArray[i]));
	}
	return al;
}
private double lcssDistance(AdaptiveBlocks blocks, ArrayList<Integer> x, ArrayList<Integer> y) {
	int m = x.size();
	int n = y.size();
	if(Math.abs(m-n)>5)
		return 1000;
	double[][] opt = new double[m+1][n+1];
	double ans;
	for(int i = m-1; i>= 0; i--)
		for(int j = n-1; j>=0; j--)
		{
			//if(x.get(i).equals(y.get(j)))
			//if(blocks.latBlockCount(x.get(i),y.get(j))<=Math.max(m,n)/minBlocks&&blocks.lonBlockCount(x.get(i), y.get(j))<=Math.max(m, n)/minBlocks)  //epsilon
//			if(blocks.latBlockCount(x.get(i),y.get(j))<=minBlocks&&blocks.lonBlockCount(x.get(i), y.get(j))<=minBlocks)  //epsilon
			
			
			
			if(blocks.latBlockCount(x.get(i),y.get(j))<=Math.max(SequiturModel.alphabetSize/50.0,1)&&blocks.lonBlockCount(x.get(i), y.get(j))<=Math.max(SequiturModel.alphabetSize/50.0,1))  //epsilon

				opt[i][j] = opt[i+1][j+1]+1;
			else
				opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1])
				-0.3*
				Math.sqrt(blocks.latBlockCount(x.get(i),y.get(j))*blocks.latBlockCount(x.get(i),y.get(j))
						+(blocks.lonBlockCount(x.get(i), y.get(j))*blocks.lonBlockCount(x.get(i), y.get(j)))); //penalty

			//	opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1])-0.3*Math.max(blocks.latBlockCount(x.get(i),y.get(j)),blocks.lonBlockCount(x.get(i), y.get(j))); //penalty

				//	opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1])-0.5*Math.max(blocks.latBlockCount(x.get(i),y.get(j))-minBlocks,blocks.lonBlockCount(x.get(i), y.get(j))-minBlocks); //penalty
		//		opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1])-(blocks.latBlockCount(x.get(i),y.get(j))+blocks.lonBlockCount(x.get(i), y.get(j)))/2; //penalty

		}
	ans = 1- Math.max(0.0,(double)opt[0][0])/(Math.max(m, n));
	/*
	if(ans<=minLink){
	System.out.println("x: "+x);
	System.out.println("y: "+y);
	System.out.println("minLink = "+minLink);
	System.out.println("ans: "+ans+" opt00 = "+opt[0][0]);
	if(y.get(0)==3040&&y.get(1)==3118&&x.get(0)==3040)
		for(int f =0; f<x.size(); f++){
			System.out.println();
			for(int g = 0; g<y.size(); g++)
				System.out.print(opt[f][g]+"\t\t");
		}
	}
	*/
	return ans;
}

private double avgDTWDistance(Blocks blocks,ArrayList<Integer> s,
		ArrayList<Integer> t) {
	double totalDistance = 0;
/*	System.out.print("s::::::::::size:"+s.size());
	
	for(int i=0; i<s.size();i++)
		System.out.print(" "+s.get(i));
		
	System.out.println();
	System.out.print("t::::::::::size:"+t.size()+"   ");
	
	for(int i=0; i<t.size();i++)
		System.out.print(" "+t.get(i));
		
	System.out.println();
	*/

	int n = s.size();
	int m = t.size();
	double[][] DTW = new double[n+1][m+1];
	double cost = 0;
	for(int i=0;i<n;i++)
		DTW[i+1][0]=Double.MAX_VALUE;
	for(int i=0;i<m;i++)
		DTW[0][i+1] = Double.MAX_VALUE;
	DTW[0][0] = 0;
	for (int i=0;i<n;i++){
		if (i>0)
			totalDistance = totalDistance + blocks.distance(s.get(i), s.get(i-1));
		for(int j=0;j<m;j++){
			if(i==0&&j>0)
				totalDistance = totalDistance + blocks.distance(t.get(j), t.get(j-1));
			cost = blocks.distance(s.get(i),t.get(j));
		//	System.out.println("cost_"+i+","+j+": "+cost);
			DTW[i+1][j+1]=cost+minimum(DTW[i][j+1],		// insertion
									   DTW[i+1][j], 	// deletion
									   DTW[i][j]);	// match
		}
	}
//	System.out.println("DTW:::::"+DTW[n][m]);
	int step = 1;
	int x = n;
	int y = m;
	while(!((x==1)&&(y==1))){
		step = step + 1;
		switch(min(DTW[x-1][y-1],DTW[x-1][y],DTW[x][y-1])){
		case 1: x--; y--; break;
		case 2: x--; break;
		case 3: y--; break;
		default: System.out.println("Error!!!!");
		}
		
	}
//	System.out.println("step: "+step);
	double avg = DTW[n][m]/step;
/*
	if (m<n)
		avg = avg*((double)minBlocks/m)*((double)minBlocks/m);
	else
		avg = avg*((double)minBlocks/n)*((double)minBlocks/n);
//	System.out.println("avgDTW:::::"+avg);
*/
	//System.out.println("avg : totalDistance"+avg+" : "+totalDistance);
	
	avg = avg* (avg/totalDistance);
	return avg;
}

private int min(double d, double e, double f) {
	if(d<=e&&d<=f)
		return 1;
	if(e<=d&&e<=f)
		return 2;
	if(f<=e&&f<=d)
		return 3;
	return 0;
}

private double minimum(double a, double b, double c) {
	return Math.min(Math.min(a, b), Math.min(b, c));
}
}
