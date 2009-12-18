package org.flashmonkey.util;

import java.util.Random;

public class RandomUtils {

	private static Random random = new Random();
	
	/**
	 * Returns a random double-precision floating point number.
	 * 
	 * @return
	 */
	public static double randomDouble() {
		return random.nextDouble();
	}
	
	/**
	 * Returns a random unsigned integer.
	 * 
	 * @return
	 */
	public static int randomInt() {
		return random.nextInt();
	}
	
	/**
	 * Returns either 1 or -1 with 50% chance of either.
	 * 
	 * @return
	 */
	public static int randomBinomial() {
		int n = random.nextInt();
		if ((n % 2) == 0)
			return -1;
		else
			return 1;
	}
	
	/**
	 * Returns a random unsigned integer between the lower and upper limits supplied.
	 * 
	 * @param lower
	 * @param upper
	 * @return
	 */
	public static int randomInt(int lower, int upper) {
		return random.nextInt(upper - lower + 1) + lower;
	}
	
	public static int randomInt(int x) {
		return random.nextInt(x - 1);
	}
	
	public static double randomGaussian() {
		return random.nextGaussian();
	}
}
