package core;

import java.util.ArrayList;

import core.gi.RuleInterval;

public class motifFactory {

public static  boolean istrival(RuleInterval ruleInterval,RuleInterval ruleInterval2)
{
	int p1=Math.abs(ruleInterval.startPos-ruleInterval2.startPos);
	if(p1>(ruleInterval.getLength()+ruleInterval2.getLength())/8)
		return false;
	int p2=Math.abs(ruleInterval.endPos-ruleInterval2.endPos);
	if(p2>(ruleInterval.getLength()+ruleInterval2.getLength())/8)
		return false;
	return true;
}

public static  boolean istrival(ArrayList<RuleInterval> arrayList,ArrayList<RuleInterval> arrayList2,int s)
{
	int t=0;
	for(int i=0;i<arrayList.size();i++)
		for(int j=0;j<arrayList2.size();j++)
		{
			if(istrival(arrayList.get(i),arrayList2.get(j)))
				t++;
		}
	if(t<arrayList.size())
		return true;
	else
		return false;
}


}
