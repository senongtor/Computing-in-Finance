package MonteCarlo;

public class StockGenerator extends StockPath {
	protected Option option;
	protected double lastprice;

	public StockGenerator(Option option) {

		this.option = option;
	}

	/**
	 * For an European option, we only need to know the price of the closing
	 * date. We need only one random variable to get that price using the
	 * Geometric Brownian Formula.
	 */
	public double getPrices(float normalvar) {

		lastprice = option.initialPrice * Math
				.exp((option.getInterestRate() - option.getvolatility() * option.getvolatility() / 2) * option.period
						+ option.getvolatility() * Math.sqrt(option.period) * normalvar);
		return lastprice;

	}

}
