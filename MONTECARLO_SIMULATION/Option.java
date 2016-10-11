package eran;


public class Option {
	/** Option Name */
	String name;
	/** Initial Price */
	double initialPrice;
	/** Duration of the option*/
	int period;
	/** The interest rate r*/
	double interestRate;
	/** The volatility sigma */
	double volatility;
	/** The strike price of the option */
	double strikePrice;
	/** option type */
	String optionType;
	
	public static class OptionBuilder{
		private double interestRate;
		private double initialPrice;
		private int period;
		private double volatility;
		private double strikePrice;
		private String name;
		private String optionType;
		
		public OptionBuilder(){}
		
		public OptionBuilder setInitialPrice(double initialPrice){
			this.initialPrice=initialPrice;
			return this;
		}
		
		public OptionBuilder setInterest(double interestRate){
			this.interestRate=interestRate;
			return this;
		}
		
		public OptionBuilder setStrikePrice(double strikePrice){
			this.strikePrice=strikePrice;
			return this;
		}
		
		public OptionBuilder setName(String name){
			this.name=name;
			return this;
		}
		
		public OptionBuilder setVolatility(double volatility){
			this.volatility=volatility;
			return this;
		}
		
	    public OptionBuilder setDuration(int period){
	    	this.period=period;
	    	return this;
	    }
	    
	    public OptionBuilder setOptionType(String optionType){
	    	this.optionType=optionType;
	    	return this;
	    }
	    
	    public Option build(){
	    	return new Option(this);
	    }
	    
	}
	public Option(OptionBuilder builder){
    	this.initialPrice=builder.initialPrice;
    	this.interestRate=builder.interestRate;
    	this.strikePrice=builder.strikePrice;
    	this.volatility=builder.volatility;
    	this.name=builder.name;
    	this.optionType=builder.optionType;
    	this.period=builder.period;
    }
	public double getInitialPrice(){
		return initialPrice;
	}
	public double getInterestRate(){
		return interestRate;
	}
	public double getstrikePrice(){
		return strikePrice;
	}
	public double getvolatility(){
		return volatility;
	}
	public int getperiod(){
		return period;
	}
	

}
