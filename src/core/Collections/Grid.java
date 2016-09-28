package core.Collections;

public class Grid {
	
	public static double[] x;
	public static double[] y;
	 static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
	      'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9','!','"','#','$','%','&','(',')','*','+',',','-','.','/'};

	 
	public static void set(int alphabetSize, double max, double min,double max2, double min2)
	{
		
		int n = alphabetSize-1;
		
	    double[] cuts = new double[n];
	    double[] cuts2 = new double[n];
	    double cut = 1/(double)alphabetSize;
	    for (int i = 0; i<n; i++)
    	{
	    	cuts[i] = cut*(i+1)*(max-min)+min;
	    	cuts2[i] = cut*(i+1)*(max2-min2)+min2;
    	}
	    x=cuts;
	    y=cuts2;
	}
	
	public static double[] get(String s)
	{
		char a=s.charAt(0);
		char b=s.charAt(1);
		double[] res=new double[2];
		int h=0;
		for(int i=0;i<ALPHABET.length;i++)
		{
			if(a==ALPHABET[i])
			{
				h=i;
				break;
			}
		}
		
		res[0]=x[h];
		int h2=0;
		for(int i=0;i<ALPHABET.length;i++)
		{
			if(a==ALPHABET[i])
			{
				h2=i;
				break;
			}
		}
		
		res[1]=y[h2];
		
		return res;
	}
	
	public static void setX(double[] x) {
		Grid.x = x;
	}
	public static void setY(double[] y) {
		Grid.y = y;
	}
	
	
	
	
	
}
