package de.uni_marburg.mdo_over.crossover;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

public interface ICrossoverStrategy {

	/**
	 * Evolves two EMF models and returns a list of offspring models. By contract
	 * the parent models must not be modified by this call.
	 * 
	 * @param firstParent  first parent model of the crossover
	 * @param secondParent second parent model of the crossover
	 * @return list of offspring models
	 */
	public List<EObject> evolve(EObject firstParent, EObject secondParent);

}
