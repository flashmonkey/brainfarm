package org.flashmonkey.neat.experiments.api;

public class Experiment {
	
	public Experiment() {
		
	}
	
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private String dataInput;
	
	public void setDataInput(String dataInput) {
		this.dataInput = dataInput;
	}

	public String getDataInput() {
		return dataInput;
	}
	
	private String dataOutput;

	public void setDataOutput(String dataOutput) {
		this.dataOutput = dataOutput;
	}

	public String getDataOutput() {
		return dataOutput;
	}
	
	private String fitnessClass;

	public void setFitnessClass(String fitnessClass) {
		this.fitnessClass = fitnessClass;
	}

	public String getFitnessClass() {
		return fitnessClass;
	}
	
	private StartFrom startFrom;

	public void setStartFrom(StartFrom startFrom) {
				this.startFrom = startFrom;
	}

	public StartFrom getStartFrom() {
		return startFrom;
	}
	
	private String genomeFile;

	public void setGenomeFile(String genomeFile) {
		this.genomeFile = genomeFile;
	}

	public String getGenomeFile() {
		return genomeFile;
	}
	
	private int epoch;

	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}

	public int getEpoch() {
		return epoch;
	}
	
	private int activation;

	public void setActivation(int activation) {
		this.activation = activation;
	}

	public int getActivation() {
		return activation;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder("Experiment Details:\n");
		
		s.append("Data Source: " + getDataSource().toString());
		
		return s.toString();
	}
}
