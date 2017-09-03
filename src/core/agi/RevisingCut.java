package core.agi;

import core.Word;
import core.Collections.AWordList;

public class RevisingCut {

	private static Integer lens_str=5;
	
	public static AWordList run(AWordList w, AGrammarRules r) {
		// TODO Auto-generated method stub
		AWordList aw= ExpandRule(w, r).clone();
		Integer t=0;
		for(int i=0;i<aw.size();i++)
		{
			Word wx=new Word(aw.get(i)); 
		
			if(wx.isrule())
			{
				if(t<=lens_str)
				{
					boolean flag=false;
					for(int j = 1; j <= t; j++)
					{
						if(Integer.parseInt(aw.get(i-j).str())<-999)
						{	
							flag=true;
							break;
						
						}
					}
					if(flag)
					{
						t=0;
						continue;
					}
					for(int j = 1; j <= t; j++)
					{
						System.out.println(aw.get(i-j).str());
							aw.get(i-j).str("x");
					}
				}
				t=0;
			}
			else
			{
				if(Integer.parseInt(wx.str())<-999)
					t=0;
				else
					t++;
			}
		}
		
		AWordList wt=new AWordList();
		
		for(Word s : aw)
		{
			if(!s.str().equals("x"))
				wt.add(s);
		}
		
		return wt;
	}
	
	
	public static AWordList run(AWordList w, AGrammarRules r,Integer lens) {
		// TODO Auto-generated method stub
		AWordList aw= ExpandRule(w, r).clone();
		Integer t=0;
		for(int i=0;i<aw.size();i++)
		{
			Word wx=new Word(aw.get(i)); 
		
			if(wx.isrule() || wx.contains("-"))
			{
				if(t<=lens)
				{
					for(int j = 1; j <= t; j++)
					{
						if(aw.get(i-j).str().contains("-"))
							continue;
						else
							aw.get(i-j).str("x");
					}
				}
				t=0;
			}
			else
			{
				t++;
			}
		}
		
		AWordList wt=new AWordList();
		
		for(Word s : aw)
		{
			if(!s.str().equals("x"))
				wt.add(s);
		}
		
		return wt;
	}
	
	public static AWordList ExpandRule(AWordList w, AGrammarRules ar)
	{
		AWordList nw=new AWordList();
		
		for(Word wx : w)
		{
			
			if(wx.isrule() && !ar.get(wx.rule()).isBasic())
			{
				AGrammarRuleRecord rx=ar.get(wx.rule());
				AWordList a=new AWordList(wx.getStart(),rx.getExpend2ruleArray());
				a.updateAll(ar);
				nw.addAll(a);
			
			}
			else
			{
				nw.add(wx);
			}
					
		}
		return nw;
	}


	
}