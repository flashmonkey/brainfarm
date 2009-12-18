package org.flashmonkey.neat.core;

import java.text.DecimalFormat;

import util.IOseq;

/** Is a genetic codification of gene; */
public class Gene extends Neat {

	/** if a reference to object for identify input/output node and features */
	private Link link;

	/** is historical marking of node */
	private double innovationNumber;

	/** how much mutation has changed the link */
	private double mutationNumber;

	/** is a flag: is TRUE the gene is enabled FALSE otherwise. */
	private boolean enabled;

	public Gene(Gene g, Trait tp, NNode inode, NNode onode) {
		link = new Link(tp, g.link.getWeight(), inode, onode, g.link.isRecurrent());
		setInnovationNumber(g.getInnovationNumber());
		setMutationNumber(g.getMutationNumber());
		setEnabled(g.isEnabled());
	}

	public void op_view() {

		String mask03 = " 0.000;-0.000";
		DecimalFormat fmt03 = new DecimalFormat(mask03);

		String mask5 = " 0000";
		DecimalFormat fmt5 = new DecimalFormat(mask5);

		System.out.print("\n [Link (" + fmt5.format(link.getInputNode().getId()));
		System.out.print("," + fmt5.format(link.getOutputNode().getId()));
		System.out.print("]  innov (" + fmt5.format(getInnovationNumber()));

		System.out.print(", mut=" + fmt03.format(getMutationNumber()) + ")");
		System.out.print(" Weight " + fmt03.format(link.getWeight()));

		if (link.getTrait() != null)
			System.out.print(" Link's trait_id " + link.getTrait().getId());

		if (isEnabled() == false)
			System.out.print(" -DISABLED-");

		if (link.isRecurrent())
			System.out.print(" -RECUR-");

	}

	public Gene() {
	}

	public Gene(Trait trait, double weight, NNode inputNode, NNode outputNode, boolean recurrent, double innovationNumber, double mutationNumber) {
		link = new Link(trait, weight, inputNode, outputNode, recurrent);
		setInnovationNumber(innovationNumber);
		setMutationNumber(mutationNumber);
		setEnabled(true);
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