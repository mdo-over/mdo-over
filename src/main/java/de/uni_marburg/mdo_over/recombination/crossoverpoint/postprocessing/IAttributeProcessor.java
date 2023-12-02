package de.uni_marburg.mdo_over.recombination.crossoverpoint.postprocessing;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public interface IAttributeProcessor {

	public boolean acceptsAttribute(EAttribute attribute);

	/**
	 * For the given attribute calculates and returns a value by considering the attribute values stored in both of the given
	 * objects. The returned value has to conform to the type of the attribute.
	 * 
	 * @param firstParent first object to consider
	 * @param secondParent second object to consider
	 * @param attribute attribute for which a value should be calculated
	 * @return calculated value
	 */
	public Object processAttribute(EObject firstParent, EObject secondParent, EAttribute attribute);

}
