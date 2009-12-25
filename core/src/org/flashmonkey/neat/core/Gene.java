package org.flashmonkey.neat.core;

import java.text.DecimalFormat;

import org.flashmonkey.neat.core.api.IGene;

import util.IOseq;

/** 
 * A genetic codification of gene. 
 */
public class Gene implements IGene
{
	/** 
	 * Reference to object for identify input/output node and features. 
	 */
	private Link link;

	/** 
	 * Historical marking of node.
	 */
	private double innovationNumber;

	/** 
	 * How much mutation has changed the link.
	 */
	private double mutationNumber;

	/** 
	 * TRUE the gene is enabled. 
	 */
	private boolean enabled;

	/**
	 * Empty Constructor.
	 */
	public Gene() {
	}
	
	/**
	 * Creates a new Gene from references to the Trait and Input/Output Nodes.
	 * 
	 * @param gene
	 * @param trait
	 * @param inputNode
	 * @param outputNode
	 */
	public Gene(Gene gene, Trait trait, NNode inputNode, NNode outputNode) {
		
		// Create a new Link.
		link = new Link(trait, gene.link.getWeight(), inputNode, outputNode, gene.link.isRecurrent());
		
		// Copy the supplied gene's properties.
		setInnovationNumber(gene.getInnovationNumber());
		setMutationNumber(gene.getMutationNumber());
		setEnabled(gene.isEnabled());
	}
	
	/**
	 * Creates a new Gene from the supplied Input/Output nodes and gene properties.
	 * 
	 * @param trait
	 * @param weight
	 * @param inputNode
	 * @param outputNode
	 * @param recurrent
	 * @param innovationNumber
	 * @param mutationNumber
	 */
	public Gene(Trait trait, double weight, NNode inputNode, NNode outputNode, boolean recurrent, double innovationNumber, double mutationNumber) {
		
		// Create the Link.
		link = new Link(trait, weight, inputNode, outputNode, recurrent);
		
		// Set the gene properties.
		setInnovationNumber(innovationNumber);
		setMutationNumber(mutationNumber);
		setEnabled(true);
	}

	public String toString() {

		StringBuilder s = new StringBuilder();
		
		String mask03 = " 0.000;-0.000";
		DecimalFormat fmt03 = new DecimalFormat(mask03);

		String mask5 = " 0000";
		DecimalFormat fmt5 = new DecimalFormat(mask5);

		s.append("\n [Link (" + fmt5.format(link.getInputNode().getId()));
		s.append("," + fmt5.format(link.getOutputNode().getId()));
		s.append("]  innov (" + fmt5.format(getInnovationNumber()));

		s.append(", mut=" + fmt03.format(getMutationNumber()) + ")");
		s.append(" Weight " + fmt03.format(link.getWeight()));

		if (link.getTrait() != null)
			s.append(" Link's trait_id " + link.getTrait().getId());

		if (isEnabled() == false)
			s.append(" -DISABLED-");

		if (link.isRecurrent())
			s.append(" -RECUR-");
		
		return s.toString();
	}

	public void print_to_file(IOseq xFile) {

		StringBuffer s2 = new StringBuffer("");

		s2.append("gene ");

		if (link.getTrait() != null)
			s2.append(" " + link.getTrait().getId());
		else
			s2.append(" 0");

		s2.append(" " + link.getInputNode().getId());
		s2.append(" " + link.getOutputNode().getId());
		s2.append(" " + link.getWeight());

		if (link.isRecurrent())
			s2.append(" 1");
		else
			s2.append(" 0");

		s2.append(" " + getInnovationNumber());
		s2.append(" " + getMutationNumber());

		if (isEnabled())
			s2.append(" 1");
		else
			s2.append(" 0");

		xFile.IOseqWrite(s2.toString());

	}
	
	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public void setInnovationNumber(double innovationNumber) {
		this.innovationNumber = innovationNumber;
	}

	public double getInnovationNumber() {
		return innovationNumber;
	}

	public void setMutationNumber(double mutationNumber) {
		this.mutationNumber = mutationNumber;
	}

	public double getMutationNumber() {
		return mutationNumber;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}