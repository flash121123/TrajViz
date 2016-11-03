package com.roots.map;

import java.awt.Color;
import java.text.DecimalFormat;

public class ColorBar {

	private int largest;
	private int smallest;
	private int range;
	private Color[] colors;
	private int height;
	 
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ColorBar(int smallestCount, int largestCount) {
		
		//colors = createGradient(Color.BLUE, Color.RED, 500);
		 colors = createMultiGradient(new Color[]{Color.blue, Color.cyan, Color.green, Color.yellow, Color.red}, 500);
		//transparencies = createTransparencyGradient(0f,0f,1f,500);
				
		largest = largestCount;
    smallest = smallestCount;
		range = largest - smallest;
	}
	
public ColorBar(int largestCount) {
		
		//colors = createGradient(Color.BLUE, Color.RED, 500);
		 colors = createMultiGradient(new Color[]{Color.blue, Color.cyan, Color.green, Color.yellow, Color.red}, 500);
		//transparencies = createTransparencyGradient(0f,0f,1f,500);
				
		largest = largestCount;
    smallest = 1;
		range = largest - smallest;
	}
	
	private Color[] createMultiGradient(Color[] colors, int numSteps)
  {

      int numSections = colors.length - 1;
      int gradientIndex = 0; 
      Color[] gradient = new Color[numSteps];
      Color[] temp;

      for (int section = 0; section < numSections; section++)
      {
          temp = createGradient(colors[section], colors[section+1], numSteps / numSections);
          for (int i = 0; i < temp.length; i++)
          {
              gradient[gradientIndex++] = temp[i];
          }
      }

      if (gradientIndex < numSteps)
      {
          for (/* nothing to initialize */; gradientIndex < numSteps; gradientIndex++)
          {
              gradient[gradientIndex] = colors[colors.length - 1];
          }
      }

      return gradient;
  }
	

	private Color[] createGradient(final Color one, final Color two, final int numSteps)
    {
        int r1 = one.getRed();
        int g1 = one.getGreen();
        int b1 = one.getBlue();
        int a1 = one.getAlpha();

        int r2 = two.getRed();
        int g2 = two.getGreen();
        int b2 = two.getBlue();
        int a2 = two.getAlpha();

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;

        Color[] gradient = new Color[numSteps];
        double iNorm;
        for (int i = 0; i < numSteps; i++)
        {
            iNorm = i / (double)numSteps; //a normalized [0:1] variable
            newR = (int) (r1 + iNorm * (r2 - r1));
            newG = (int) (g1 + iNorm * (g2 - g1));
            newB = (int) (b1 + iNorm * (b2 - b1));
            newA = (int) (a1 + iNorm * (a2 - a1));
            gradient[i] = new Color(newR, newG, newB, newA);
        }

        return gradient;
    }
	
	public Color getColor(int use) {
		double norm = (use - smallest) * 1.0 / range; // 0 < norm < 1
        int colorIndex = (int) Math.floor(norm * (colors.length - 1));
		return colors[colorIndex];
	}

	public String[] getlabels(int numYTicks) {
		// TODO Auto-generated method stub
		 DecimalFormat df = new DecimalFormat("####");
		 String label="";
		 String[] labels=new String[numYTicks+1];
		 for (int y = 0; y <= numYTicks; y++)
     {
         //g2d.drawLine(26, height - 30 - y * yDist, 30, height - 30 - y * yDist);
         label = df.format(((y / (double) numYTicks) * (this.largest - 1)) + 1);
         
         labels[y]=label;
     }
		return labels;
	}
	
}
