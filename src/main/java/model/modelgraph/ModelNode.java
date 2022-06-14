package model.modelgraph;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

public class ModelNode extends ModelGraphElement {

	private final Set<ModelEdge> incomingEdges = new HashSet<ModelEdge>();
	private final Set<ModelEdge> outgoingEdges = new HashSet<ModelEdge>();

	public ModelNode() {		
	}
	
	/**
	 * Creates a copy of this node carrying the same name and referencing the same {@link EObject}. Incoming and
	 * outgoing edges referenced by this node will <b>not</b> be copied!
	 * 
	 * @param toCopy the node to be copied
	 */
	public ModelNode(ModelNode toCopy) {
		super(toCopy);
	}

	public Set<ModelEdge> getIncomingEdges() {
		return incomingEdges;
	}
	
	public Set<ModelEdge> getOutgoingEdges() {
		return outgoingEdges;
	}
}
