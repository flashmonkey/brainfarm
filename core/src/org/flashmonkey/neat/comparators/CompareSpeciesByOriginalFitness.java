   package org.flashmonkey.neat.comparators;

import org.flashmonkey.neat.core.Organism;
import org.flashmonkey.neat.core.Species;

   /**
 * 
 * 
 * 
 */
	public class CompareSpeciesByOriginalFitness implements java.util.Comparator {
   	  	  /**
   * order_species constructor comment.
   */
	   public CompareSpeciesByOriginalFitness() {
	  //super();
	  }
   /**
   */
	   public int compare(Object o1, Object o2) {
	  
		 Species _sx = (Species) o1;
		 Species _sy = (Species) o2;
	  
		 Organism _ox = (Organism) _sx.organisms.get(0);
		 Organism _oy = (Organism) _sy.organisms.get(0);
	  
		 if (_ox.orig_fitness < _oy.orig_fitness)
			return +1;
		 if (_ox.orig_fitness > _oy.orig_fitness)
			return -1;
		 return 0;
	  
	  }
}