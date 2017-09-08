package edu.gmu.base;

public class Point {
	
	public float x;
	public float y;
	public Point(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Point(double x,double y)
	{
		this.x=(float)x;
		this.y=(float)y;
	}

	@Override
	public String toString() {
		return "["+ x + ", " + y + "]";
	}
	
	
}
