package eran;

import java.util.Random;

public class NormalRandomVector implements RandomVectorGenerator{
	private double[] array;
	private int days;
  Random rand = new Random();
  public NormalRandomVector(int days){
	  this.array=new double[days];
	  this.days=days;
  }
  /**Generate random normally distributed variable for the calculation of the stockpath with days number of prices.*/
	@Override
  public double[] getVector(){
		for (int i =0;i<days;i++){
			array[i]=rand.nextGaussian();
		}
		return array;
	}
}
