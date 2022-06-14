package model.modelgraph;

import org.eclipse.emf.ecore.EObject;

public abstract class ModelGraphElement {

	private String name;
	private EObject referencedObject;

	public ModelGraphElement() {
	}

	/**
	 * Creates a copy of the given element.
	 * @param toCopy model graph to create a copy from
	 */
	public ModelGraphElement(ModelGraphElement toCopy) {
		setName(toCopy.getName());
		setReferencedObject(toCopy.getReferencedObject());
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
	 * Sets the object referenced by this element. The reference may be null to allow {@link ModelGraph model graphs}
	 * only partially mapping to concrete EMF models.
	 * 
	 * @param referencedObject object referenced by the element
	 */
	public void setReferencedObject(EObject referencedObject) {
		this.referencedObject = referencedObject;
	}
}
