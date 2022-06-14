package model.modelgraph;

import org.eclipse.emf.ecore.EObject;

public class ModelEdge extends ModelGraphElement {

	private ModelNode source;
	private ModelNode target;

	/**
	 * Creates a shallow copy of this edge carrying the same name and referencing the same {@link EObject}. Source and target
	 * nodes are <b>not</b> copied and will be uninitialized!
	 * 
	 * @param toCopy the edge to be copied
	 */
	public ModelEdge(ModelEdge toCopy) {
		super(toCopy);
	}

	public ModelEdge(ModelNode source, ModelNode target) {
		this.source = source;
		this.target = target;
	}

	public ModelNode getSource() {
		return source;
	}	
	
	public void setSource(ModelNode source) {
		this.source = source;
	}

	public ModelNode getTarget() {
		return target;
	}

	public void setTarget(ModelNode target) {
		this.target = target;
	}
}
