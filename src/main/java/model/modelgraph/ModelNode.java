package model.modelgraph;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

public class ModelNode extends ModelGraphElement {

	private final Set<ModelEdge> incomingEdges = new LinkedHashSet<ModelEdge>();
	private final Set<ModelEdge> outgoingEdges = new LinkedHashSet<ModelEdge>();

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

	/**
	 * Returns the incoming edges of this node. 
	 * The returned set iterates over the edges in the order in which they were added. 
	 * @return incoming edges
	 */
	public Set<ModelEdge> getIncomingEdges() {
		return incomingEdges;
	}
	
	/**
	 * Returns the outgoing edges of this node. 
	 * The returned set iterates over the edges in the order in which they were added. 
	 * @return outgoing edges
	 */
	public Set<ModelEdge> getOutgoingEdges() {
		return outgoingEdges;
	}
}
