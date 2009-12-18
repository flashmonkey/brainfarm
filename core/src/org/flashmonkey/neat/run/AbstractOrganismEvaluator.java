package org.flashmonkey.neat.run;

import org.flashmonkey.neat.core.Neat;
import org.flashmonkey.neat.core.Network;
import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.experiments.api.IExperimentFitness;

import util.EnvConstant;

public abstract class AbstractOrganismEvaluator implements IOrganismEvaluator {

	protected Neat neat;
	
	private IExperimentFitness fitnessImpl;
	
	protected Network net;
	
	protected double[] in;
	
	protected double[][] out;
	
	protected double tgt[][];
	
	protected int net_depth = 0;
	
	protected boolean success = false;
	
	public AbstractOrganismEvaluator(Neat neat, IExperimentFitness fitnessImpl) {
		this.neat = neat;
		this.fitnessImpl = fitnessImpl;
	}
	
	@Override
	public boolean evaluate(Organism organism) {
		
		
		
		double fit_dyn = 0.0;
		double err_dyn = 0.0;
		double win_dyn = 0.0;

		// per evitare errori il numero di ingressi e uscite viene calcolato in
		// base
		// ai dati ;
		// per le unit di input a tale numero viene aggiunto una unit bias
		// di tipo neuron
		// le classi di copdifica input e output quindi dovranno fornire due
		// metodi : uno per restituire l'input j-esimo e uno per restituire
		// il numero di ingressi/uscite
		// se I/O è da file allora è il metodo di acesso ai files che avrà lo
		// stesso nome e che farà la stessa cosa.

		double errorsum = 0.0;
		//int net_depth = 0; // The max depth of the network to be activated
		int count = 0;

		// System.out.print("\n evaluate.step 1 ");

		in = new double[EnvConstant.NR_UNIT_INPUT + 1];

		// setting bias

		in[EnvConstant.NR_UNIT_INPUT] = 1.0;

		out = new double[neat.getNumberOfSamples()][neat.getNumberOfOutputUnits()];

		tgt = new double[neat.getNumberOfSamples()][neat.getNumberOfOutputUnits()];

		Integer ns = new Integer(neat.getNumberOfSamples());

		net = organism.net;
		net_depth = net.max_depth();

		// pass the number of node in genome for add a new
		// parameter of evaluate the fitness
		//
		int xnn = net.getAllNodes().size();
		Integer nn = new Integer(xnn);

		
		
		if (evaluate()) {
			double[] fitness = fitnessImpl.computeFitness(ns, nn, out, tgt);
			//System.out.println("SETTING FITNESS FROM FITNESS CLASS");
			fit_dyn = fitness[0];
			err_dyn = fitness[1];
			win_dyn = fitness[2];
			
			organism.setFitness(fit_dyn);
			organism.setError(err_dyn);
		} else {
			//System.out.println("SETTING FITNESS FROM DEFAULT MINIMUM");
			errorsum = 999.0;
			organism.setFitness(0.001);
			organism.setError(errorsum);
		}
		
		//System.out.println("FITNESS AFTER EVALUATION == " + organism.getFitness());
		
		if (win_dyn == 1.0) {
			organism.setWinner(true);
			return true;
		}

		if (win_dyn == 2.0) {
			organism.setWinner(true);
			EnvConstant.SUPER_WINNER_ = true;
			return true;
		}

		organism.setWinner(false);
		
		return false;
	}
	
	protected abstract boolean evaluate();
}
