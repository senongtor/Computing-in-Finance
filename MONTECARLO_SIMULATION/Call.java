package eran;

public class Call implements PayOut{
 private double strikePrice;
 
 
	
 public Call(double strikePrice){
	 this.strikePrice=strikePrice;
 }
 /**
  * To get the Call type payout of each generated stockpath
  */
 public double getPayout(StockPath path){
	 double payout=Math.max(path.getPrices().get(path.getPrices().size()-1)-strikePrice,0);
	 return payout;
 }
}
