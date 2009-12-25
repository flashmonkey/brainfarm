package org.flashmonkey.util.writers;

import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.flashmonkey.neat.core.Gene;
import org.flashmonkey.neat.core.Genome;
import org.flashmonkey.neat.core.NNode;
import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.core.Population;
import org.flashmonkey.neat.core.Species;
import org.flashmonkey.neat.core.Trait;
import org.flashmonkey.neat.run.IEvolution;
import org.flashmonkey.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generates a Spring Bean that encapsulates the current state of the Population being 
 * evolved by the IEvolution instances passed to getSource(IEvolution);
 * 
 * @author Trevor
 *
 */
public class SpringXMLSourceGenerator {

	public static final String BEAN_NODE_NAME = "bean";
	public static final String PROPERTY_NODE_NAME = "property";
	public static final String REF_NODE_NAME = "ref";
	
	public static final String CLASS_ATTRIBUTE_NAME = "class";
	public static final String ID_ATTRIBUTE_NAME = "id";
	public static final String NAME_ATTRIBUTE_NAME = "name";
	public static final String VALUE_ATTRIBUTE_NAME = "value";
	
	public Source getSource(IEvolution evolution) {
		
		Document xml = XMLUtils.createNewDocument("beans");
		
		generatePopulationXML(evolution.getPopulation(), "__population_" + evolution.getRun() + "_" + evolution.getEpoch(), xml);
		
		return new DOMSource(xml);
	}
	
	private void generatePopulationXML(Population population, String id, Document xml) {
		
		// Create the Population-specific xml
		Element classNode = xml.createElement(BEAN_NODE_NAME);
		classNode.setAttribute(CLASS_ATTRIBUTE_NAME, Population.class.getName());
		classNode.setAttribute(ID_ATTRIBUTE_NAME, id);
		
		Element propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "species");
		
		Element listNode = xml.createElement("list");
		propertyNode.appendChild(listNode);
		
		classNode.appendChild(propertyNode);
		
		// Now loop through each Species.
		for (int i = 0; i < population.getSpecies().size(); i++) {
			Species species = population.getSpecies().get(i);
			String beanId = "__species_" + i;
			
			generateSpeciesXML(species, beanId, xml);
			
			Element listEntry = xml.createElement(REF_NODE_NAME);
			listEntry.setAttribute(BEAN_NODE_NAME, beanId);
			
			listNode.appendChild(listEntry);
		}
	}
	
	private void generateSpeciesXML(Species species, String id, Document xml) {
		
		// Create the species-specific xml.
		Element classNode = xml.createElement(BEAN_NODE_NAME);
		classNode.setAttribute(CLASS_ATTRIBUTE_NAME, Species.class.getName());
		classNode.setAttribute(ID_ATTRIBUTE_NAME, id);
		
		// Add the species id.
		Element propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "id");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(species.getId()));
		
		classNode.appendChild(propertyNode);
		
		// Add the Average fitness of the species.
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "averageFitness");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(species.getAve_fitness()));
		
		classNode.appendChild(propertyNode);
		
		// Add the size of the species (the size of the organisms list).
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "size");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(species.getOrganisms().size()));
		
		classNode.appendChild(propertyNode);
		
		// Add the age of the species.
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "age");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(species.getAge()));
		
		classNode.appendChild(propertyNode);
		
		// Add the organisms to the list. 
		// Add a property node with a child 'list' node.
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "organisms");
		
		Element listNode = xml.createElement("list");
		propertyNode.appendChild(listNode);		
		
		// Now loop through each organism.
		// Add each organism to the Document as a seperate bean.
		// Add a reference to the organism bean to the list.
		for (int i = 0; i < species.getOrganisms().size(); i++) {
			Organism organism = species.getOrganisms().get(i);
			String beanId = "__organism_" + i;
			
			generateOrganismXML(organism, beanId, xml);
			
			Element listEntry = xml.createElement(REF_NODE_NAME);
			listEntry.setAttribute(BEAN_NODE_NAME, beanId);
			
			listNode.appendChild(listEntry);
		}
	}
	
	private void generateOrganismXML(Organism organism, String id, Document xml) {
		
		// Create the organism-specific xml.
		Element classNode = xml.createElement(BEAN_NODE_NAME);
		classNode.setAttribute(CLASS_ATTRIBUTE_NAME, Organism.class.getName());
		classNode.setAttribute(ID_ATTRIBUTE_NAME, id);
		
		// Add the organism id.
		Element propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "id");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(organism.getGenome().getId()));
		
		classNode.appendChild(propertyNode);
		
		// Add the organism fitness.
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "fitness");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(organism.getFitness()));
		
		classNode.appendChild(propertyNode);
		
		// Add the organism's error.
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "error");
		propertyNode.setAttribute(VALUE_ATTRIBUTE_NAME, String.valueOf(organism.getError()));
		
		// Add the organism's genome.
		propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "genome");
		
		// Create an individual id for the genome bean.
		String beanId = id + "_genome";
		
		// Add a reference to the genome bean.
		propertyNode.setAttribute(REF_NODE_NAME, beanId);
		
		// Add the genome bean to the document.
		generateGenomeXML(organism.getGenome(), id, xml);
	}
	
	private void generateGenomeXML(Genome genome, String id, Document xml) {
		
		Element classNode = xml.createElement(BEAN_NODE_NAME);
		classNode.setAttribute(CLASS_ATTRIBUTE_NAME, Genome.class.getName());
		classNode.setAttribute(ID_ATTRIBUTE_NAME, id);
			
		// Now loop through the genes, nodes and traits.
		generateGenesXML(genome.getGenes(), id, classNode, xml);
		generateNodeXML(genome.getNodes(), id, xml);
		generateTraitXML(genome.getTraits(), id, xml);
	}
	
	private void generateGenesXML(List<Gene> genes, String id, Element parent, Document xml) {
		
		Element propertyNode = xml.createElement(PROPERTY_NODE_NAME);
		propertyNode.setAttribute(NAME_ATTRIBUTE_NAME, "genes");
		
		Element listNode = xml.createElement("list");
		
		for (int i = 0; i < genes.size(); i++) {
			Gene gene = genes.get(i);
			String beanId = id + "__gene_" + i;
			
			generateGeneXML(gene, id, xml);
			
			Element listEntry = xml.createElement(REF_NODE_NAME);
			listEntry.setAttribute(BEAN_NODE_NAME, beanId);
			
			listNode.appendChild(listEntry);
		}
		
		parent.appendChild(propertyNode);
	}
	
	private void generateGeneXML(Gene gene, String id, Document xml) {
		
		Element classNode = xml.createElement(BEAN_NODE_NAME);
		classNode.setAttribute(CLASS_ATTRIBUTE_NAME, gene.getClass().getName());
		classNode.setAttribute(ID_ATTRIBUTE_NAME, id);
		
		
	}
	
	private void generateNodeXML(List<NNode> nodes, String id, Document xml) {
		
	}
	
	private void generateTraitXML(List<Trait> traits, String id, Document xml) {
		
	}
}
