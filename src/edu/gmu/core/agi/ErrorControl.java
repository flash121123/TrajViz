package edu.gmu.core.agi;

public class ErrorControl {
	
	public static int LengthErr(AGrammarRuleRecord a)
	{
		int b=a.size();
		int amax=-1;
		int amin=10000;
		for(int i=0;i<b;i++)
		{
			int tmp=a.getIntervals().get(i).size();
  			if(tmp>amax)
			{
				amax=tmp;
			}
			if(tmp<amin)
			{
				amin=tmp;
			}
		}
		return amax-amin;
	}
	
	
}
