package de.uni_marburg.mdo_over.model.modelgraph;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public abstract class ModelGraphElement {

	private String name;
	private EObject referencedObject;
	private final Set<EAttribute> attributes = new LinkedHashSet<EAttribute>();

	public ModelGraphElement() {
	}

	/**
	 * Creates a copy of the given element.
	 * 
	 * @param toCopy model graph to create a copy from
	 */
	public ModelGraphElement(ModelGraphElement toCopy) {
		setName(toCopy.getName());
		setReferencedObject(toCopy.getReferencedObject());
		attributes.addAll(toCopy.getAttributes());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EObject getReferencedObject() {
		return referencedObject;
	}

	/**
	 * Sets the object referenced by this element. The reference may be null to allow {@link ModelGraph model graphs} only
	 * partially mapping to concrete EMF models.
	 * 
	 * @param referencedObject object referenced by the element
	 */
	public void setReferencedObject(EObject referencedObject) {
		this.referencedObject = referencedObject;
	}
	
	//TODO: In the long run attributes might need to be represented more explicitly.
	/**
	 * Attributes of the referenced object which should also be considered attributes of this graph element.
	 * @return attributes of this element
	 */
	public Set<EAttribute> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}
	
	public void addAttribute(EAttribute attribute) throws GraphManipulationException {
		if (getReferencedObject().eClass().getEAllAttributes().contains(attribute)) {
			attributes.add(attribute);
		} else {
			throw new GraphManipulationException("Attribute is not part of referenced object");
		}
	}
	
	public void removeAttribute(EAttribute attribute) {
		attributes.remove(attribute);
	}
	
	/**
	 * Removes all attributes currently considered by the element.
	 */
	public void clearAttributes() {
		attributes.clear();
	}
}
