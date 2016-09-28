package base;

public class Interval {
	private Integer startPos=-1;
	private Integer endPos=-1;
	private Integer lens=0;
	
	public Interval()
	{
		return;
	}

	public Interval(Integer s,Integer e)
	{
		startPos=s;
		endPos=e;
	}
	
	public void set_start(Integer s)
	{
		startPos=s;
	}
	public  void set_end(Integer e)
	{
		endPos=e;
		setlen();
	}
	
	private void setlen() {
		lens=endPos-startPos;
	}

	public Integer get_len()
	{
		return lens;
	}
	
	public Integer get_start()
	{
		return startPos;
	}
	
	public Integer get_end()
	{
		return endPos;
	}
	
	@Override
	public String toString() {
		return "[" + startPos + ", " + endPos + "]";
	}
}
