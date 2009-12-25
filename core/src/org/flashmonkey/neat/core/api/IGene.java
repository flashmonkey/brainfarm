package org.flashmonkey.neat.core.api;

import org.flashmonkey.neat.core.Link;

public interface IGene {
	
	Link getLink();
	void setLink(Link link);
	
	double getInnovationNumber();
	void setInnovationNumber(double innovationNumber);	
	
	double getMutationNumber();
	void setMutationNumber(double mutationNumber);
	
	boolean isEnabled();
	void setEnabled(boolean enabled);
}
