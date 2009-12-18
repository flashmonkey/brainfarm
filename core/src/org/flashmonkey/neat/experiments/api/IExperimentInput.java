package org.flashmonkey.neat.experiments.api;

public interface IExperimentInput {

	int getNumSamples();

	int getNumUnit();

	double getInput(int _plist[]);
}
