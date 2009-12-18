package org.flashmonkey.neat.experiments.api;

public interface IExperimentFitness {
	double getMaxFitness();
	double[] computeFitness(int sample, int numNodes, double out[][], double tgt[][]);
}
