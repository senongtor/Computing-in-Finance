package MonteCarlo;

public class InverseNormal {
	/***
	 * Calculating the "Y" value we use to know if the desired accuracy level is reached
	 * @param The probability we input.
	 * @return
	 */
	public static double getY(double prob){
		double p=1-(0.5-prob/2);
		double t=Math.sqrt(Math.log(1/Math.pow(p, 2)));
	double y=-t+(2.515517+0.802853*t+0.010328*Math.pow(t, 2))/(1+1.432788*t+0.189269*Math.pow(t, 2)+0.001308*Math.pow(t, 3));
		return y;
	}
	}
