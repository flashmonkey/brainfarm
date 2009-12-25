package org.flashmonkey.neat.api;

import org.flashmonkey.neat.run.IEvolution;

public interface IEvolutionListener {

	void onEvolutionStart(IEvolution evolution);
	
	void onEpochStart(IEvolution evolution);
	
	void onEpochComplete(IEvolution evolution);
	
	void onEvolutionComplete(IEvolution evolution);
}
