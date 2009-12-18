package org.flashmonkey.neat.experiments.api;

import org.flashmonkey.neat.core.Population;

public interface INeatContext {
	void loadExperiment(String location);
	IExperimentFitness getFitnessImpl();
	IExperimentInput getInputImpl();
	IExperimentOutput getOutputImpl();
	Population getPopulation();
	Runnable getExperimentRun();
}
