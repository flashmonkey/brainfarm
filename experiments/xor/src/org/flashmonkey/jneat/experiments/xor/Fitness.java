package org.flashmonkey.jneat.experiments.xor;

import org.flashmonkey.neat.experiments.api.IExperimentFitness;


public class Fitness implements IExperimentFitness {

	public double getMaxFitness() {
		return Math.pow(4.0, 2);
	}

	public double[] computeFitness(int sample, int nodeCount, double output[][], double target[][]) {
		double d[] = new double[3];
		double errorsum = 0.0;
		double fitness;
		
		for (int j = 0; j < sample; j++) {
			errorsum += Math.abs(target[j][0] - output[j][0]);
		}
		
		fitness = Math.pow(4.0 - errorsum, 2);

		d[0] = fitness;
		d[1] = errorsum;
		d[2] = 0.0;

		if (output[0][0] < 0.5 && 
			output[1][0] >= 0.5 && 
			output[2][0] >= 0.5 && 
			output[3][0] < 0.5) {
			
			d[2] = 1.0;
		}		

		return d;
	}
}
