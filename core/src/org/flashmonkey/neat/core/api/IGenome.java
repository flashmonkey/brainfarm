package org.flashmonkey.neat.core.api;

import org.flashmonkey.neat.core.Genome;
import org.flashmonkey.neat.core.Population;

public interface IGenome {

	Genome mateMultipoint(Genome g, int id, double fitness1, double fitness2);
	
	Genome mateMultipointAverage(Genome g, int id, double fitness1, double fitness2);
	
	Genome mateSinglepoint(Genome spouse, int id);
	
	boolean mutateAddLink(Population population, int attempts);
	
	boolean mutateAddNode(Population population);
	
	void mutateGeneReenable();
	
	void mutateLinkTrait(int repeats);
	
	void mutateLinkWeight(double power, double rate, int mutationType);
	
	void mutateNodeTrait(int repeats);
	
	void mutateRandomTrait();
	
	void mutateToggleEnable(int repeats);
}
