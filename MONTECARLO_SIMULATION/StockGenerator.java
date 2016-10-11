package eran;

import java.util.ArrayList;
import java.util.List;

public class StockGenerator implements StockPath{
	private RandomVectorGenerator generator;
	private Option option;
	private ArrayList<Double> Path;
	int i = 0;
	
	public StockGenerator(Option option, RandomVectorGenerator generator){
		RandomVectorGenerator normgenerator=new NormalRandomVector(option.getperiod());
		RandomVectorGenerator antitheticgenerator=new AntitheticRandomVector(normgenerator);
		this.option=option;
		this.generator = antitheticgenerator;
	}
	/**
	 * To calculate a stockpath which contains the number of prices we input as options duration
	 * At each time, use a random number from our generator to generate next day's price.
	 */
	@Override
	public List<Double> getPrices(){
		
		this.Path = new ArrayList<Double>();
		this.Path.add(option.getInitialPrice());   //add the initial price to the list.get(0)
		double[] duration = generator.getVector();
		double St = option.getInitialPrice();
		for(i=1; i<duration.length;i++) {
			St = St * Math.exp(option.getInterestRate() - option.getvolatility()*option.getvolatility()/2+option.getvolatility()*duration[i-1]);
			this.Path.add(St);
		}
			return Path;
		
 }
	 
}
