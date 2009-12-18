package org.flashmonkey.neat.run;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.flashmonkey.neat.core.Genome;
import org.flashmonkey.neat.core.Neat;
import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.core.Population;
import org.flashmonkey.neat.core.Species;
import org.flashmonkey.util.AbstractThreadedClass;

import util.EnvConstant;

public class ExperimentRun extends AbstractThreadedClass {
	
	private static Logger logger = Logger.getLogger(ExperimentRun.class);
	
	private int numberOfRuns;
	
	private int numberOfEpochs;
	
	private Population population;
	
	private IOrganismEvaluator evaluator;
	
	public ExperimentRun(int numberOfRuns, int numberOfEpochs, Population population, IOrganismEvaluator evaluator) {
		this.numberOfRuns = numberOfRuns;
		this.numberOfEpochs = numberOfEpochs;
		this.population = population;
		this.evaluator = evaluator;
	}
	
	@Override
	protected void execute() {
		
		logger.info("Starting an experiment run using\n" + numberOfRuns + ", " + numberOfEpochs + ", " + population + ", " + evaluator);
		
		for (int i = 0; i < numberOfRuns; i++) {
			
			for (int j = 0; j < numberOfEpochs; j++) {
				logger.info("Begging Epoch - " + j);
				epoch(population, j);
			}
		}
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
				/*if (esito) {
					win = true;

					if (_organism.getFitness() > max_fitness_of_winner) {
						max_fitness_of_winner = _organism.getFitness();
						EnvConstant.MAX_WINNER_FITNESS = max_fitness_of_winner;
					}
				
					// store only first organism
					if (EnvConstant.FIRST_ORGANISM_WINNER == null) {
						EnvConstant.FIRST_ORGANISM_WINNER = _organism;
					}

				}*/
			}
			
			System.out.println("2 " + pop.organisms.get(0) + " " + ((Organism)pop.organisms.get(0)).fitness);

			// compute average and max fitness for each species
			/*Iterator itr_specie;
			itr_specie = pop.species.iterator();
			while (itr_specie.hasNext()) {
				Species _specie = ((Species) itr_specie.next());
				_specie.compute_average_fitness();
				_specie.compute_max_fitness();
			}*/
			
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
			/*if (win) {
				logger.debug(" generation:      in this generation "
						+ generation + " i have found at leat one WINNER  ");
				int conta = 0;
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

			}*/

			// wait an epoch and make a reproduction of the best species
			pop.epoch(generation);
			System.out.println("3 " + pop.organisms.get(0));
			if (win) {
				return true;
			} else
				return false;

	}
}
