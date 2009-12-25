package org.flashmonkey.neat.run;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.flashmonkey.neat.api.IEvolutionListener;
import org.flashmonkey.neat.core.Neat;
import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.core.Population;
import org.flashmonkey.neat.core.Species;
import org.flashmonkey.neat.experiments.api.Experiment;
import org.flashmonkey.util.AbstractThreadedClass;

public class Evolution extends AbstractThreadedClass implements IEvolution {
	
	private static Logger logger = Logger.getLogger(Evolution.class);
	
	private int currentRun;
	
	private int currentEpoch;
	
	private Neat neat;
	
	private Experiment experiment;
	
	private Population population;
	
	private IOrganismEvaluator evaluator;
	
	protected List<IEvolutionListener> listeners = Collections.synchronizedList(new CopyOnWriteArrayList<IEvolutionListener>());
	
	public Evolution(Neat neat, Experiment experiment, Population population, IOrganismEvaluator evaluator) {
		this.neat = neat;
		this.experiment = experiment;
		this.population = population;
		this.evaluator = evaluator;
	}
	
	@Override
	protected void execute() {
		
		onEvolutionStart(population);
		
		logger.info("Starting an experiment run using\n" + neat.num_runs + ", " + experiment.getEpoch() + ", " + population + ", " + evaluator);
		
		for (int i = 0; i < neat.num_runs; i++) {
			currentRun = i;
			for (int j = 0; j < experiment.getEpoch(); j++) {
				currentEpoch = j;
				logger.info("Begging Epoch - " + j);
				onEpochStart(i, j, population);
				epoch(population, j);
				onEpochComplete(i, j, population);
			}
		}
		
		onEvolutionComplete(population);
	}
	
	public boolean epoch(Population pop, int generation) {

		
		
		boolean esito = false;
		boolean win = false;

		System.out.println("1 " + pop.organisms.get(0) + " " + ((Organism)pop.organisms.get(0)).fitness);
			// Evaluate each organism if exist the winner.........
			// flag and store only the first winner

			Iterator itr_organism;
			itr_organism = pop.organisms.iterator();
			double max_fitness_of_winner = 0.0;

			while (itr_organism.hasNext()) {
				// point to organism
				Organism _organism = ((Organism) itr_organism.next());
				
				// evaluate
				esito = evaluator.evaluate(_organism);

				// if is a winner , store a flag
				if (esito) {
					win = true;

					if (_organism.getFitness() > max_fitness_of_winner) {
						max_fitness_of_winner = _organism.getFitness();
						//EnvConstant.MAX_WINNER_FITNESS = max_fitness_of_winner;
					}
				
					// store only first organism
					/*if (EnvConstant.FIRST_ORGANISM_WINNER == null) {
						EnvConstant.FIRST_ORGANISM_WINNER = _organism;
					}*/

				}
			}
			
			// compute average and max fitness for each species
			Iterator itr_specie;
			itr_specie = pop.species.iterator();
			while (itr_specie.hasNext()) {
				Species _specie = ((Species) itr_specie.next());
				_specie.compute_average_fitness();
				_specie.compute_max_fitness();
				
				//System.out.println("species " + _specie.getId() + " Average Finess == " + _specie.getAve_fitness());
			}
			
			
			
			// Only print to file every print_every generations
			/*String cause1 = " ";
			String cause2 = " ";*/
			/*if (((generation % _neat.print_every) == 0) || (win)) {

				if ((generation % _neat.print_every) == 0)
					cause1 = " request";
				if (win)
					cause2 = " winner";
			}*/

			// if exist a winner write to file
			if (win) {
				logger.debug("in generation " + generation + " i have found at leat one WINNER");
				/*int conta = 0;
				itr_organism = pop.getOrganisms().iterator();
				while (itr_organism.hasNext()) {
					Organism _organism = ((Organism) itr_organism.next());
					if (_organism.winner) {
						conta++;
					}
					if (EnvConstant.SUPER_WINNER_) {
						logger
								.debug(" generation:      in this generation "
										+ generation
										+ " i have found a SUPER WINNER ");
						EnvConstant.SUPER_WINNER_ = false;

					}

				}

				logger.debug(" generation:      number of winner's is "
						+ conta);
*/
			}

			// wait an epoch and make a reproduction of the best species
			pop.epoch(generation);

			
			
			if (win) {
				return true;
			} else
				return false;

	}
	
	protected void onEvolutionStart(Population population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEvolutionStart(this);
		}
	}
	
	protected void onEvolutionComplete(Population population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEvolutionComplete(this);
		}
	}
	
	protected void onEpochStart(int run, int epoch, Population population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEpochStart(this);
		}
	}
	
	protected void onEpochComplete(int run, int epoch, Population population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEpochComplete(this);
		}
	}
	
	public void addListener(IEvolutionListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IEvolutionListener listener) {
		listeners.remove(listener);
	}
	
	public int getRun() {
		return currentRun;
	}
	
	public int getEpoch() {
		return currentEpoch;
	}
	
	public Population getPopulation() {
		return population;
	}
}
