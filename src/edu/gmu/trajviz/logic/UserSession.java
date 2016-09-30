package edu.gmu.trajviz.logic;

public class UserSession {
	  public static final double DEFAULT_MIN_LINK =5;
	  public static final int DEFAULT_ALPHABET_SIZE = 75;
	  public static final int DEFAULT_MINIMUM_BLOCKS =3;
	  public static final int DEFALULT_NOISE_POINT_THRESHOLD =2;
	  private double minLink;
	  private int alphabetSize;
	  private int minBlocks;
	  private int noisePointsThreshold;
  public UserSession(){
	  super();
	  this.minLink = DEFAULT_MIN_LINK;
	  this.alphabetSize = DEFAULT_ALPHABET_SIZE;
	  this.minBlocks = DEFAULT_MINIMUM_BLOCKS;
	  this.noisePointsThreshold = DEFALULT_NOISE_POINT_THRESHOLD;
	  
  }
  public double getMinLink(){
	  return this.minLink;
  }
  public int getAlphabet(){
	  return this.alphabetSize;
  }
  public int getMinBlocks(){
	  return this.minBlocks;
  }
  public int getNoisePointThreshold(){
	  return this.noisePointsThreshold;
  }
  
  public void setNoisePointThreshold(int count){
	  this.noisePointsThreshold = count;
  }
  
  public void setMinLink(double minLink){
	  this.minLink = minLink;
  }
  public void setAlphabet(int alphabet){
	  this.alphabetSize = alphabet;
  }
  public void setMinBlocks(int minBlocks){
	  this.minBlocks = minBlocks;
  }
}
