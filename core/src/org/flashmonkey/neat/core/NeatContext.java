package org.flashmonkey.neat.core;

import java.io.File;

import org.apache.log4j.Logger;
import org.flashmonkey.neat.experiments.api.DataSource;
import org.flashmonkey.neat.experiments.api.Experiment;
import org.flashmonkey.neat.experiments.api.IExperimentFitness;
import org.flashmonkey.neat.experiments.api.IExperimentInput;
import org.flashmonkey.neat.experiments.api.IExperimentOutput;
import org.flashmonkey.neat.experiments.api.INeatContext;
import org.flashmonkey.neat.experiments.api.StartFrom;
import org.flashmonkey.neat.run.ClassOrganismEvaluator;
import org.flashmonkey.neat.run.Evolution;
import org.flashmonkey.neat.run.IOrganismEvaluator;
import org.flashmonkey.util.FileUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

import util.EnvConstant;

public class NeatContext implements INeatContext {
	
	private static Logger logger = Logger.getLogger(NeatContext.class);

	private JarClassLoader jarClassLoader;
	
	private JclObjectFactory factory;
	
	protected Neat neat;

	protected Experiment experiment;

	private ApplicationContext context;
	
	private IExperimentFitness fitnessImpl;
	
	private IExperimentInput inputImpl;
	
	private IExperimentOutput outputImpl;
	
	private Genome genome;

	public NeatContext(ApplicationContext context) {
		this.context = context;
		
		neat = (Neat)context.getBean("neat");
	}

	public Neat getNeat() {
		return neat;
	}

	public void setNeat(Neat neat) {
		this.neat = neat;
	}

	/**
	 * This is where we load the experiment the user wants to execute.
	 * This method loads the .jar file the user has selected.
	 * The .jar file is then extracted to a new 'experiments' directory.
	 * The .class files in the jar are loaded onto the classpath.
	 * The Spring context file is loaded and the 'experiment' bean is instantiated
	 * and stored in this NeatContext so it can be referenced.
	 * 
	 * @param location (String) The location of the .jar file that contains the experiment's resources.
	 */
	public void loadExperiment(String location) {
		
		// Create a temporary directory for the experiment.		
		File experimentDirectory = new File("experiment");
		
		if (experimentDirectory.exists()) {
			experimentDirectory.delete();
		}
		
		boolean success = experimentDirectory.mkdir();
		
		if (success) {
			
			// Extract the JAR file that contains the experiment's 
			// contents into the experiments directory.
			FileUtils.extractZip(location, experimentDirectory);
			
			// Load the experiments classes onto the classpath so they're available to neat.
			jarClassLoader = new JarClassLoader();
			jarClassLoader.add(location);
			
			factory = JclObjectFactory.getInstance();
		  
			// Grab the experiments directory's path.
			String path = experimentDirectory.getAbsolutePath();
			experimentDirectory = null;
		  
			// Load the experiment's context into a bean factory.
			XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource( path + "/experiment-context.xml"));
			PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		  	cfg.setLocation(new FileSystemResource(path + "/experiment-parameters.properties"));
		  	cfg.postProcessBeanFactory(factory);
		  	
		  	// Get the 'experiment' bean - this contains the settings for the loaded experiment.
		  	experiment = (Experiment)factory.getBean("experiment");
		  	
		  	refresh();
		  	
		} else {
			logger.error("There was an error creating the experiment directory");
		}
	}
	
	public IExperimentFitness getFitnessImpl() {
		if (fitnessImpl == null) {
			fitnessImpl = JclUtils.cast(factory.create(jarClassLoader, experiment.getFitnessClass()), IExperimentFitness.class);
		}
		
		return fitnessImpl;
	}
	
	public IExperimentInput getInputImpl() {
		if (inputImpl == null) {
			inputImpl = JclUtils.cast(factory.create(jarClassLoader, "org.flashmonkey.jneat.experiments.xor.Input"), IExperimentInput.class);
		}
		
		return inputImpl;
	}
	
	public IExperimentOutput getOutputImpl() {
		if (outputImpl == null) {
			return JclUtils.cast(factory.create(jarClassLoader, "org.flashmonkey.jneat.experiments.xor.Output"), IExperimentOutput.class);
	
		}
		
		return outputImpl;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(String location) {
		// experiment = new Experiment(location);
	}

	@Override
	public Population getPopulation() {
		if (experiment.getStartFrom() == StartFrom.GENOME) {
			return new Population(genome, neat.pop_size);
		}

		/*if ((EnvConstant.TYPE_OF_START == EnvConstant.START_FROM_NEW_RANDOM_POPULATION)
				&& (!EnvConstant.FORCE_RESTART)) {
			return new Population(neat.pop_size, neat.getNumberOfInputs() + 1,
					neat.getNumberOfOutputUnits(), EnvConstant.NR_UNIT_MAX, EnvConstant.RECURSION, EnvConstant.PROBABILITY_OF_CONNECTION);
		}*/
		
		/*if ((EnvConstant.TYPE_OF_START == EnvConstant.START_FROM_OLD_POPULATION)
				|| (EnvConstant.FORCE_RESTART)) {
			u_pop = new Population(EnvRoutine
					.getJneatFileData(EnvConstant.NAME_CURR_POPULATION));
		}*/
		
		return null;
	}
	
	private void refresh() {
		// Unload the experiment-specific class implmentations.
		// So they'll be recreated next time they're called.
		fitnessImpl = null;
		inputImpl = null;
		outputImpl = null;
		
		// Initialise the experiment-specific NEAT parameters.
		neat.setMaxFitness(getFitnessImpl().getMaxFitness());
		
		if (experiment.getDataSource() == DataSource.CLASS) {
			neat.setNumberOfInputs(getInputImpl().getNumUnit());
			neat.setNumberOfSamples(getInputImpl().getNumSamples());
			neat.setNumberOfOutputUnits(getOutputImpl().getNumUnit());
		} else if (experiment.getDataSource() == DataSource.FILE) {
			
		}
		
		logger.debug("Experiment starting from: " + experiment.getStartFrom());
		
		if (experiment.getStartFrom() == StartFrom.GENOME) {
			XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource( "experiment/" + experiment.getGenomeFile()));
			genome = (Genome)factory.getBean("genome");
			logger.info("Created Genome from file " + genome);
		}
	}
	
	public Runnable getExperimentRun() {
		return new Evolution(getNeat(), getExperiment(), getPopulation(), getOrganismEvaluator());
	}
	
	private IOrganismEvaluator getOrganismEvaluator() {
		if (experiment.getDataSource() == DataSource.CLASS) {
			return new ClassOrganismEvaluator(neat, getFitnessImpl(), getInputImpl(), getOutputImpl());
		}
		return null;
	}
}
