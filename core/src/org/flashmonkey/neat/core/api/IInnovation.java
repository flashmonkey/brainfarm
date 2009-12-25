package org.flashmonkey.neat.core.api;

public interface IInnovation {

	int getInnovationType();
	
	int getInputNodeId();
	
	int getOutputNodeId();
	
	int getNewTraitId();
	
	double getNewWeight();
	
	double getInnovationNumber1();
	
	boolean isRecurrent();
}
