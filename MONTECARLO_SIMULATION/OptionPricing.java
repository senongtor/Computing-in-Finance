package eran;


import eran.SimulationManager;
import eran.StockGenerator;

public class OptionPricing {
	static String optionName = "IBM";
	static int optionPeriod = 252;
	static double initialPrice = 152.35;
	static double interestRate = 0.0001;
	static double volatility = 0.01;
	static double strikePrice = 165;
	static String optionType = "Call";
	static double strikePrice2 = 164;
	static String optionType2 = "AsianCall";

	public static void main(String[] args) {
//Simulate the price for the Call option for IBM
		Option option = new Option.OptionBuilder().setInitialPrice(initialPrice).setInterest(interestRate)
				.setStrikePrice(strikePrice).setName(optionName).setVolatility(volatility).setDuration(optionPeriod)
				.setOptionType(optionType).build();

		RandomVectorGenerator normGenerator = new NormalRandomVector(option.period);
		RandomVectorGenerator generator = new AntitheticRandomVector(normGenerator);
		StockGenerator path = new StockGenerator(option, generator);
		
		StatsCollector stat = new StatsCollector();
		PayOut payOut = new Call(strikePrice);

		double value = SimulationManager.simulate(path, payOut, stat);
		double price = value * Math.exp(-option.interestRate * option.period);
		System.out.println("The value of "+optionType +" option " + optionName + " with strikeprice " + strikePrice
				+ " that will expire in " + optionPeriod + " days is " + value);
		System.out.println("The " + optionType + " option should be priced as :" + price);
       
		
//Then we are gonna price the Asian call option for IBM
		Option option2 = new Option.OptionBuilder().setInitialPrice(initialPrice).setInterest(interestRate)
				.setStrikePrice(strikePrice2).setName(optionName).setVolatility(volatility).setDuration(optionPeriod)
				.setOptionType(optionType2).build();
		RandomVectorGenerator normGenerator2 = new NormalRandomVector(option2.period);
		RandomVectorGenerator generator2 = new AntitheticRandomVector(normGenerator2);
		StockGenerator path2 = new StockGenerator(option2, generator2);
		StatsCollector stat2 = new StatsCollector();
		PayOut payOut2 = new AsianCall(strikePrice2);
		double value2 = SimulationManager.simulate(path2, payOut2, stat2);
		double price2 = value2 * Math.exp(-option2.interestRate * option2.period);
		System.out.println("The value of "+optionType2+" option " + optionName + " with strikeprice " + strikePrice2
				+ " that will expire in " + optionPeriod + " days is " + value2);
		System.out.println("The " + optionType2 + " option should be priced as :" + price2);
	}
}
