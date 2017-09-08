package gmu.edu.core.frame;

public interface Word {
	
	/*
	 * rule(): return the index of rule in current level
	*/
	public Integer rule();

	/*
	 * isword(): check whether it is a word (a rule in all level)
	*/
	public boolean isword();
	
	/*
	 * ruleU(): check whether it is a universal rule return the index used by RulePool
	*/
	public Integer[] ruleU();
	
	/*
	 * isrule(): check whether it is a rule
	 */
	public boolean isrule();
	
	public void str(String in);
	public Integer getStart();
	public void setStart(Integer a);
	public Integer getEnd();
	public void setEnd(Integer a);
	
	
}
