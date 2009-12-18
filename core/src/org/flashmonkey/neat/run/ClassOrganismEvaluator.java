package org.flashmonkey.neat.run;

import org.flashmonkey.neat.core.NNode;
import org.flashmonkey.neat.core.Neat;
import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.experiments.api.IExperimentFitness;
import org.flashmonkey.neat.experiments.api.IExperimentInput;
import org.flashmonkey.neat.experiments.api.IExperimentOutput;

import util.EnvConstant;

public class ClassOrganismEvaluator extends AbstractOrganismEvaluator {	
	
	private IExperimentInput inputImpl;
	
	private IExperimentOutput outputImpl;
	
	public ClassOrganismEvaluator(Neat neat, IExperimentFitness fitnessImpl, IExperimentInput inputImpl, IExperimentOutput outputImpl) {
		super(neat, fitnessImpl);
		
		this.inputImpl = inputImpl;
		this.outputImpl = outputImpl;
	}
	
	@Override
	protected boolean evaluate() {
		int input[] = new int[2];

		for (int count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) {
			input[0] = count;
			// first activation from sensor to first next level of
			// neurons
			for (int j = 0; j < EnvConstant.NR_UNIT_INPUT; j++) {
				input[1] = j;
				in[j] = inputImpl.getInput(input);
			}

			// load sensor
			net.load_sensors(in);

			if (EnvConstant.ACTIVATION_PERIOD == EnvConstant.MANUAL) {
				for (int relax = 0; relax < EnvConstant.ACTIVATION_TIMES; relax++) {
					success = net.activate();
				}
			} else {
				// first activation from sensor to next layer....
				success = net.activate();

				// next activation while last level is reached !
				// use depth to ensure relaxation
				for (int relax = 0; relax <= net_depth; relax++) {
					success = net.activate();
				}
			}

			// for each sample save each output
			for (int j = 0; j < EnvConstant.NR_UNIT_OUTPUT; j++)
				out[count][j] = ((NNode) net.getOutputs().get(j))
						.getActivation();

			// clear net
			net.flush();
		}
		
		if (success) {
			// prima di passare a calcolare il fitness legge il tgt da
			// ripassare
			// al chiamante;
			int target[] = new int[2];
	
			for (int count = 0; count < EnvConstant.NUMBER_OF_SAMPLES; count++) {	
				target[0] = count;
				for (int j = 0; j < EnvConstant.NR_UNIT_OUTPUT; j++) {
					target[1] = j;
					tgt[count][j] = outputImpl.getTarget(target);
				}
			}
		}
		
		return success;
	}

}
