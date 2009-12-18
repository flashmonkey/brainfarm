package org.flashmonkey.neat.api;

import org.flashmonkey.neat.core.Neat;

public interface INeatParameter {

	public String getKey();
	
	public void setKey(String key);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public abstract void set(Neat neat);
}
