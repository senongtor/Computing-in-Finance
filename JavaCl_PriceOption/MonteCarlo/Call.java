package MonteCarlo;

/**
 * European Call
 * 
 * @author Hongtao Cheng
 *
 */
public class Call extends PayOut {
	private double strikePrice;

	public Call(double strikePrice) {
		this.strikePrice = strikePrice;
	}

	/**
	 * To get the Call type payout of each last day price for each simulation
	 */
	public double getPayout(double lastprice) {
		double payout = Math.max(lastprice - strikePrice, 0);
		return payout;
	}
}
