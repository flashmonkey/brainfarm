package org.flashmonkey.util.writers;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.apache.log4j.Logger;
import org.flashmonkey.neat.api.IEvolutionListener;
import org.flashmonkey.neat.core.Population;
import org.flashmonkey.neat.run.IEvolution;

public abstract class TransformingEvolutionListener implements IEvolutionListener {

	private static Logger logger = Logger.getLogger(TransformingEvolutionListener.class);
	
	protected Transformer transformer;
	
	private ISourceGenerator source;
	
	private IResultGenerator result;
	
	public TransformingEvolutionListener() {
		TransformerFactory factory = TransformerFactory.newInstance();
		
		try {
			transformer = factory.newTransformer();
		} catch (TransformerException e) {
			logger.error("Error transform: " + e.getMessage());
		}
	}
	
	@Override
	public void onEpochComplete(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEpochStart(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvolutionComplete(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvolutionStart(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	public void setResult(IResultGenerator result) {
		this.result = result;
	}

	public IResultGenerator getResult() {
		return result;
	}

	public void setSource(ISourceGenerator source) {
		this.source = source;
	}

	public ISourceGenerator getSource() {
		return source;
	}	
}
