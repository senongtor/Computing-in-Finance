package eran;


public class SimulationManager {
		private final static double PROB=0.98;  //1-(1/2-0.96/2)
		private final static double ERROR=0.1;
		private final static double T=Math.sqrt(Math.log(1/Math.pow(PROB, 2)));
		private final static double Y=-T+(2.515517+0.802853*T+0.010328*Math.pow(T, 2))/(1+1.432788*T+0.189269*Math.pow(T, 2)+0.001308*Math.pow(T, 3));
		/**
		 * Generate stockpath, get the payout from it, and add it to the 
		 * <code>StatsCollector</code> for standard deviation and mean value 
		 * calculation at iteration until the stopping criteria is met.
		 * @param <code>StockPath</code> path
		 * @param <code>PayOut</code> payOut
		 * @param <code>StatsCollector</code> stat
		 * @return The mean value of all the payOut we generated
		 */
		public static double simulate(StockPath path,PayOut payOut,StatsCollector stat){
      			for (int i =0;i<50000;i++){
      				stat.add(payOut.getPayout(path));
      				if (i % 10000== 0 && i!=0)
      					System.out.print("10000 times simulated\n");
    				if(Y*stat.getStdDv()/Math.sqrt(i)<ERROR && i>100){
     					System.out.println(i+" simulations to converge");
      					break;
      				}
      			}
      			return stat.getmean();
	
	}
	
	
}
	
