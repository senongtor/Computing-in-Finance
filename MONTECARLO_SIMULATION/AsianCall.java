package eran;

import java.util.List;


public class AsianCall implements PayOut{
	private double strikePrice;
	
	public AsianCall(double strikePrice){
		this.strikePrice = strikePrice;
	}
	/**
	 * To get the AsianCall type payout of each generated stockpath
	 */
	@Override
	public double getPayout(StockPath path){
		
		List<Double> price =  path.getPrices();

		double ave = price.get(0);  // sum of all the elements
	    for (int i=1; i<price.size(); i++) {
	         ave = i/(i+1.0)*ave + price.get(i)/(i+1.0);
	    }
		return Math.max(ave- strikePrice,0);

}
}
