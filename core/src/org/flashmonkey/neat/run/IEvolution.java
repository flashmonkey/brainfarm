package org.flashmonkey.neat.run;

import org.flashmonkey.neat.core.Population;

public interface IEvolution {

	int getRun();
	
	int getEpoch();
	
	Population getPopulation();
}
