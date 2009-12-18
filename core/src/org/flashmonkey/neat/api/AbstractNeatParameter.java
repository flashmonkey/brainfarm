package org.flashmonkey.neat.api;

import org.flashmonkey.neat.core.Neat;

public abstract class AbstractNeatParameter {

	protected String name;
	
	protected String description;
	
	public AbstractNeatParameter() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String key) {
		this.name = key;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public abstract void set(Neat neat);
}
