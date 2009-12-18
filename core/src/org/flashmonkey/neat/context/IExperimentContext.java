package org.flashmonkey.neat.context;

import org.flashmonkey.neat.core.Population;

public interface IExperimentContext {

	int getNumberOfRuns();
	
	int getNumberOfEpochs();
	
	Population getPopulation();
}
