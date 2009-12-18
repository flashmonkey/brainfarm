package org.flashmonkey.neat.context;

import org.flashmonkey.neat.core.Population;

public class ExperimentContext implements IExperimentContext {

	private int numberOfRuns;
	
	private int numberOfEpochs;
	
	private Population population;
	
	public ExperimentContext(int numberOfRuns, int numberOfEpochs, Population population) {
		this.numberOfRuns = numberOfRuns;
		this.numberOfEpochs = numberOfEpochs;
		this.population = population;
	}
	
	@Override
	public int getNumberOfRuns() {
		return numberOfRuns;
	}

	@Override
	public Population getPopulation() {
		return population;
	}

	@Override
	public int getNumberOfEpochs() {
		return numberOfEpochs;
	}

}
