package org.flashmonkey.neat.core;

/**
 * Link is a connection from one node to another with an associated weight; It
 * can be marked as recurrent; Its parameters are made public for efficiency.
 */
public class Link extends Neat {
	
	/** 
	 * Weight of connection. 
	 */
	private double weight;
	
	/** 
	 * Reference to the input node.
	 */
	private NNode inputNode;

	/** 
	 * Reference to the output node. 
	 */
	private NNode outputNode;

	/**
	 * Flags whether this Link is recurrent.
	 */
	private boolean recurrent;

	/**
	 * Flags if this Link is tapped (delayed).
	 */
	private boolean timeDelayed;

	/**
	 * Points to a trait of parameters for genetic creation.
	 */
	private Trait trait;
	
	/**
	 * Used during computeDepth(). Flags whether this Link has already been traversed.
	 */
	private boolean traversed = false;

	/** 
	 * The amount of weight adjustment 
	 */
	private double addedWeight;

	/** 
	 * Link-related parameters that change during Hebbian type learning. 
	 */
	double[] params = new double[Neat.num_trait_params];

	/**
	 * Insert the method's description here. Creation date: (12/01/2002
	 * 10.41.28)
	 * 
	 * @param trait
	 *            jneat.Trait
	 * @param weight
	 *            double
	 * @param inputNode
	 *            jneat.NNode
	 * @param outputNode
	 *            jneat.NNode
	 * @param recurrent
	 *            boolean
	 */
	public Link(Trait trait, double weight, NNode inputNode, NNode outputNode, boolean recurrent) {

		setWeight(weight);
		setInputNode(inputNode);
		setOutputNode(outputNode);
		setRecurrent(recurrent);
		setAddedWeight(0.0);
		setTrait(trait);
		setTimeDelayed(false);
	}

	/**
	 * Insert the method's description here. Creation date: (15/01/2002 7.53.27)
	 * 
	 * @param c
	 *            int
	 */
	public Link(double weight, NNode inputNode, NNode outputNode, boolean recurrent) {
		setWeight(weight);
		setInputNode(inputNode);
		setOutputNode(outputNode);
		setRecurrent(recurrent);
		setAddedWeight(0.0);
		setTrait(null);
		setTimeDelayed(false);

	}

	/**
	 * Insert the method's description here. Creation date: (15/01/2002 8.05.44)
	 */
	public void deriveTrait(Trait trait) {
		if (trait != null) {
			for (int count = 0; count < Neat.num_trait_params; count++) {
				params[count] = trait.getParams(count);
			}
		} else {
			for (int count = 0; count < Neat.num_trait_params; count++) {
				params[count] = 0.0;
			}
		}
	}

	public void viewtext() {
		System.out.print("\n +LINK : ");
		System.out.print("weight=" + getWeight());
		System.out.print(", weight-add=" + getAddedWeight());
		System.out.print(", i(" + getInputNode().getId());
		System.out.print(")--<CONNECTION>--o(");
		System.out.print(getOutputNode().getId() + ")");
		System.out.print(", recurrent=" + isRecurrent());
		System.out.print(", tapped=" + isTimeDelayed());

		if (getTrait() != null)
			getTrait().viewtext("\n         (linktrait)-> ");
		else
			System.out
					.print("\n         *warning* linktrait for this gene is null ");
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double[] getParams() {
		return params;
	}

	public void setParams(double[] params) {
		this.params = params;
	}

	public Trait getTrait() {
		return trait;
	}

	public void setTrait(Trait trait) {
		this.trait = trait;
	}

	public void setInputNode(NNode inputNode) {
		this.inputNode = inputNode;
	}

	public NNode getInputNode() {
		return inputNode;
	}

	public void setOutputNode(NNode outputNode) {
		this.outputNode = outputNode;
	}

	public NNode getOutputNode() {
		return outputNode;
	}

	public void setRecurrent(boolean recurrent) {
		this.recurrent = recurrent;
	}

	public boolean isRecurrent() {
		return recurrent;
	}

	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}

	public boolean isTraversed() {
		return traversed;
	}

	public void setAddedWeight(double addedWeight) {
		this.addedWeight = addedWeight;
	}

	public double getAddedWeight() {
		return addedWeight;
	}

	public void setTimeDelayed(boolean timeDelayed) {
		this.timeDelayed = timeDelayed;
	}

	public boolean isTimeDelayed() {
		return timeDelayed;
	}
}