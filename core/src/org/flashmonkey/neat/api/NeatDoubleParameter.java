package org.flashmonkey.neat.api;

import java.lang.reflect.Field;

import org.flashmonkey.neat.core.Neat;

public class NeatDoubleParameter extends AbstractNeatParameter {

	private double value;
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public void set(Neat neat) {
		try
		{
			Field field = neat.getClass().getField(name);
			
			try
			{
				field.setDouble(neat, value);
			}
			catch (IllegalAccessException e) {
				// Not allowed to access field.
			}
		}
		catch (NoSuchFieldException e) {
			// Field doesn't exist.
		}
	}

}
