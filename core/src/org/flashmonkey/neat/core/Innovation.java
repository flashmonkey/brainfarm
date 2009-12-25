package org.flashmonkey.neat.core;

import org.flashmonkey.neat.core.api.IInnovation;

import util.NeatConstant;

/**
 * This class serves as a way to record innovations specifically, so that an
 * innovation in one genome can be compared with other innovations in the same
 * epoch, and if they Are the same innovation, they can both be assigned the
 * same innnovation number. This class can encode innovations that represent a
 * new link forming, or a new node being added. In each case, two nodes fully
 * specify the innovation and where it must have occured. (Between them)
 * 
 */
public class Innovation implements IInnovation {
	/**
	 * Either NEWNODE or NEWLINK
	 */
	int innovationType;

	/**
	 * Two nodes specify where the innovation took place : this is the node
	 * input
	 */
	int inputNodeId;

	/**
	 * Two nodes specify where the innovation took place : this is the node
	 * output
	 */
	int outputNodeId;

	/**
	 * The number assigned to the innovation
	 */
	double innovationNumber1;

	/**
	 * If this is a new node innovation,then there are 2 innovations (links)
	 * added for the new node
	 */
	double innovationNumber2;

	/**
	 * If a link is added, this is its weight
	 */
	double newWeight;

	/**
	 * If a link is added, this is its connected trait
	 */
	int newTraitId;

	/**
	 * If a new node was created, this is its node_id
	 */
	int newNodeId;

	/**
	 * If a new node was created, this is the innovnum of the gene's link it is
	 * being stuck inside
	 */
	double oldInnovationNumber;

	/**
	 * is recurrent ?
	 */
	boolean recurrent;

	/**
	 * Insert the method's description here. Creation date: (24/01/2002 8.09.28)
	 */
	public Innovation() {
	}

	/**
	 * Insert the method's description here. Creation date: (23/01/2002 9.04.02)
	 */
	public Innovation(int nin, int nout, double num1, double w, int t) {
		innovationType = NeatConstant.NEWLINK;
		inputNodeId = nin;
		outputNodeId = nout;
		innovationNumber1 = num1;
		newWeight = w;
		newTraitId = t;

		// Unused parameters set to zero
		innovationNumber2 = 0;
		newNodeId = 0;
		recurrent = false;
	}

	/**
	 * Insert the method's description here. Creation date: (24/01/2002 8.09.28)
	 */
	public Innovation(int nin, int nout, double num1, double num2, int newid,
			double oldinnov) {
		innovationType = NeatConstant.NEWNODE;
		inputNodeId = nin;
		outputNodeId = nout;
		innovationNumber1 = num1;
		innovationNumber2 = num2;
		newNodeId = newid;
		oldInnovationNumber = oldinnov;

		// Unused parameters set to zero
		newWeight = 0;
		newTraitId = 0;
		recurrent = false;
	}

	public int getInnovationType() {
		return innovationType;
	}

	public void setInnovationType(int innovationType) {
		this.innovationType = innovationType;
	}

	public int getInputNodeId() {
		return inputNodeId;
	}

	public void setInputNodeId(int inputNodeId) {
		this.inputNodeId = inputNodeId;
	}

	public int getOutputNodeId() {
		return outputNodeId;
	}

	public void setOutputNodeId(int outputNodeId) {
		this.outputNodeId = outputNodeId;
	}

	public double getInnovationNumber1() {
		return innovationNumber1;
	}

	public void setInnovationNumber1(double innovationNumber1) {
		this.innovationNumber1 = innovationNumber1;
	}

	public double getInnovationNumber2() {
		return innovationNumber2;
	}

	public void setInnovationNumber2(double innovationNumber2) {
		this.innovationNumber2 = innovationNumber2;
	}

	public double getNewWeight() {
		return newWeight;
	}

	public void setNewWeight(double newWeight) {
		this.newWeight = newWeight;
	}

	public int getNewTraitId() {
		return newTraitId;
	}

	public void setNewTraitId(int newTraitId) {
		this.newTraitId = newTraitId;
	}

	public int getNewNodeId() {
		return newNodeId;
	}

	public void setNewNodeId(int newNodeId) {
		this.newNodeId = newNodeId;
	}

	public double getOldInnovationNumber() {
		return oldInnovationNumber;
	}

	public void setOldInnovationNumber(double oldInnovationNumber) {
		this.oldInnovationNumber = oldInnovationNumber;
	}

	public boolean isRecurrent() {
		return recurrent;
	}

	public void setRecurrent(boolean recurrent) {
		this.recurrent = recurrent;
	}
}