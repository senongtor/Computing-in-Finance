package eran;

import java.util.ArrayList;

public class StatsCollector {
	private double mean = 0;
	/** The standard deviation of arraylist with all the values to be calculated */
	private double stdDv = 0;
	/** E[x^2] for the calculationg of standard deviation*/
	private double meanSq = 0; 
	private ArrayList<Double> list;
	
	public StatsCollector(){
		this.list=new ArrayList<Double>();
	}
	/***
	 * Add the payout result each time and calculate the moving average, 
	 * standard deviation for further need.
	 * @param Double x
	 */
 public void add(double x){
	 list.add(new Double(x));
	 mean=(list.size()-1)*mean/list.size()+x/list.size();
	 meanSq=(list.size()-1)*meanSq/list.size()+x*x/list.size();
	 stdDv=Math.sqrt(meanSq-mean*mean);
 }
 public double getmean(){
	 return mean;
 }
 public double getStdDv() {
		return stdDv;
	}

}


