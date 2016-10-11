package eran;

public class AntitheticRandomVector implements RandomVectorGenerator {
	/** To store the given <code>RandomVectorGenerator</code>*/
	private RandomVectorGenerator generator;
	/** To indicate which step the generator in during the antithetic process */
	private Boolean flag = false;
	/** To store the vector in the last step*/
	private double[] antiVector;
	
	/**
	 * Build <code>AntitheticRandomVector</code> as a decorator of <code>RandomVectorGenerator</code>
	 * @param<code>RandomVectorGenerator</code> generator to generate random vectors
	 */
	public AntitheticRandomVector(RandomVectorGenerator generator) {
		this.generator = generator;
	}
	
	@Override
	/**
	 * Generate a new random vector or return the negation of <code>antiVector</code>
	 * @return The generated new antithetic random vector
	 */
	public double[] getVector() { 
		if (flag) {
			flag = false;
			for (int i=0; i<antiVector.length; i++) {
				antiVector[i] *= -1;
			}
		} else {
			flag = true;
			antiVector = generator.getVector();	
		}
		return this.antiVector;	
	}

}