package model.modelgraph;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

/**
 * ModelGraph is meant to represent (parts of) EMF models in a simple graph structure. Any {@link ModelGraphElement
 * element} of such a graph may or may not refer to an {@link EObject} on the EMF side allowing the user to work with
 * graphs mapping only partially to an existing EMF model. A root node may be specified but note that this class does
 * not tie specific semantics to it.
 * <p>
 * There are intentionally no restrictions on how often the same {@link EObject} may be referenced by different elements
 * of the graph. Some examples where this may be useful are:
 * <ul>
 * <li>One-to-one mappings where edges refer to probably many-valued EReferences, i.e., an EReference may be represented
 * by multiple edges.
 * <li>Abstract model representations where EReferences are represented as nodes.
 * </ul>
 * Any constraints on the mappings between graph and EMF model need to be either enforced by the user or by extending
 * this class accordingly.
 * <p>
 * {@link #getNodes() Nodes} and {@link #getEdges() edges} are stored in the order of their insertion into the graph.
 * 
 * @author S. John
 *
 */
public class ModelGraph {

	private ModelNode root;
	private Set<ModelNode> nodes;
	private Set<ModelEdge> edges;

	// Make sure to distinguish model elements by their instance not by the equals method.
	private IdentityHashMap<EObject, Set<ModelNode>> objectNodeMappings;
	private IdentityHashMap<EObject, Set<ModelEdge>> objectEdgeMappings;

	public ModelGraph() {
		nodes = new LinkedHashSet<ModelNode>();
		edges = new LinkedHashSet<ModelEdge>();
		objectNodeMappings = new IdentityHashMap<EObject, Set<ModelNode>>();
		objectEdgeMappings = new IdentityHashMap<EObject, Set<ModelEdge>>();
	}

	/**
	 * Creates a graph anticipating the maximum number of nodes, edges and objects referenced by either the set of nodes
	 * or the set of edges. The first two parameters influence the number of buckets of the {@link HashSet HashSets}
	 * used to store nodes and edges internally. The last two parameters influence the number of buckets of the
	 * {@link IdentityHashMap IdentityHashMaps} used to store the mappings between objects and their referencing
	 * {@link ModelGraphElement elements}. All parameters have an impact on performance and, if possible, should be
	 * chosen carefully to prevent excessive rehashing (to few buckets) and lengthy iterations (too many buckets).
	 * 
	 * Parameters are specified as Integer objects to allow <code>null</code> values. In case of a <code>null</code>
	 * value the standard size of the respective data structure will be used.
	 * @param expNodes                  expected maximum number of nodes stored in the graph
	 * @param expEdges                  expected maximum number of edges stored in the graph
	 * @param expNodeToObjectReferences expected maximum number of objects referenced by the {@link ModelNode
	 *                                       nodes} of the graph.
	 * @param expectedEdgeToObjectReferences expected maximum number of objects referenced by the {@link ModelEdge
	 *                                       edges} of the graph.
	 */
	public ModelGraph(Integer expNodes, Integer expEdges, Integer expNodeToObjectReferences,
			Integer expectedEdgeToObjectReferences) {
		nodes = (expNodes == null ? new LinkedHashSet<>() : new LinkedHashSet<>(expNodes));
		edges = (expEdges == null ? new LinkedHashSet<>() : new LinkedHashSet<>(expEdges));
		objectNodeMappings = (expNodeToObjectReferences == null ? new IdentityHashMap<>()
				: new IdentityHashMap<>(expNodeToObjectReferences));
		objectEdgeMappings = (expectedEdgeToObjectReferences == null ? new IdentityHashMap<>()
				: new IdentityHashMap<>(expectedEdgeToObjectReferences));
	}

	/**
	 * Adds a {@link ModelNode node} to the graph if the node is not already in the graph and does not reference any
	 * edges.
	 * 
	 * @param node node to be added to the graph
	 * @return true if the node has successfully been added - false otherwise
	 * @throws GraphManipulationException if <code>node</code> is not in the graph yet but references edges
	 * @throws IllegalArgumentException   if <code>node</code> is <code>null</code>
	 */
	public boolean addNode(ModelNode node) throws GraphManipulationException {
		boolean added = false;
		if (node == null) {
			throw new IllegalArgumentException("Null cannot be added as node.");
		}
		if (!nodes.contains(node) && (!node.getIncomingEdges().isEmpty() || !node.getOutgoingEdges().isEmpty())) {
			throw new GraphManipulationException("Only nodes without edges can be added to a graph.");
		}
		if (nodes.add(node)) {
			EObject referencedObject = node.getReferencedObject();
			if (referencedObject != null) {
				objectNodeMappings.computeIfAbsent(referencedObject, k -> new HashSet<>()).add(node);
			}
			added = true;
		}
		return added;
	}

	/**
	 * Adds an {@link ModelEdge edge} to the graph if the edge is not already in the graph and its source and target
	 * nodes are part of the graph.
	 * 
	 * @param edge edge to be added to the graph
	 * @return true if the edge has successfully been added - false otherwise
	 * @throws GraphManipulationException if <code>edge</code> is not in the graph yet but either source or target nodes
	 *                                    of the edge are missing in the graph
	 * @throws IllegalArgumentException   if <code>edge</code> is <code>null</code>
	 */
	public boolean addEdge(ModelEdge edge) throws GraphManipulationException {
		boolean added = false;
		if (edge == null) {
			throw new IllegalArgumentException("Null cannot be added as edge.");
		}
		if (edge.getSource() == null || edge.getTarget() == null) {
			throw new GraphManipulationException("Nodes of an edge may not be null.");
		}
		if (!edges.contains(edge) && (!nodes.contains(edge.getSource()) || !nodes.contains(edge.getTarget()))) {
			throw new GraphManipulationException("Nodes of an edge must already be part of the graph.");
		}
		if (edges.add(edge)) {
			EObject referencedObject = edge.getReferencedObject();
			if (referencedObject != null) {
				objectEdgeMappings.computeIfAbsent(referencedObject, k -> new HashSet<>()).add(edge);
			}
			edge.getSource().getOutgoingEdges().add(edge);
			edge.getTarget().getIncomingEdges().add(edge);
			added = true;
		}
		return added;
	}

	/**
	 * Returns the set of {@link ModelNode nodes} of the graph referencing the given object. Does not check for edges
	 * referencing the object.
	 * 
	 * @param object object to look up
	 * @return set of {@link ModelNode nodes} referencing the given object
	 */
	public Set<ModelNode> getNodes(EObject object) {
		Set<ModelNode> refNodes = objectNodeMappings.get(object);
		return (refNodes == null ? Collections.emptySet() : refNodes);
	}

	/**
	 * Returns the set of {@link ModelEdge edges} of the graph referencing the given object. Does not check for nodes
	 * referencing the object.
	 * 
	 * @param object object to look up
	 * @return set of {@link ModelEdge edges} referencing the given object
	 */
	public Set<ModelEdge> getEdges(EObject object) {
		Set<ModelEdge> refEdges = objectEdgeMappings.get(object);
		return (refEdges == null ? Collections.emptySet() : refEdges);
	}

	/**
	 * Returns the set of {@link ModelGraphElement elements} of the graph referencing the given object.
	 * Iterating over the elements will first iterate over all nodes and then over all edges referencing the object in
	 * the order of their insertion into the graph, respectively.
	 * 
	 * @param object object to look up
	 * @return set of {@link ModelGraphElement elements} referencing the given object
	 */
	public Set<ModelGraphElement> getElements(EObject object) {
		Set<ModelGraphElement> elements = new HashSet<ModelGraphElement>();
		Set<? extends ModelGraphElement> refNodes = objectNodeMappings.get(object);
		if (refNodes != null) {
			elements.addAll(refNodes);
		}
		Set<? extends ModelGraphElement> refEdges = objectEdgeMappings.get(object);
		if (refEdges != null) {
			elements.addAll(refEdges);
		}
		return elements;
	}

	/**
	 * The nodes of the graph are returned in the order of their insertion.
	 * @return the nodes
	 */
	public Set<ModelNode> getNodes() {
		return nodes;
	}

	/**
	 * The edges of the graph are returned in the order of their insertion.
	 * @return the edges
	 */
	public Set<ModelEdge> getEdges() {
		return edges;
	}

	/**
	 * Returns all elements of the graph. Iterating over the elements will first iterate over all nodes and then over 
	 * all edges in the order of their insertion into the graph, respectively.
	 * @return
	 */
	public Set<ModelGraphElement> getElements() {
		Set<ModelGraphElement> elements = new LinkedHashSet<ModelGraphElement>();
		Set<ModelNode> nodes = getNodes();
		if (nodes != null) {
			elements.addAll(nodes);
		}
		Set<ModelEdge> edges = getEdges();
		if (edges != null) {
			elements.addAll(edges);
		}
		return elements;
	}

	/**
	 * Removes the node and its connected edges from the graph.
	 * 
	 * @param node a node to remove from the graph
	 * @return true if the node was present in the graph - false otherwise
	 */
	public boolean removeNode(ModelNode node) {
		if (node != null) {
			for (ModelEdge edge : node.getIncomingEdges()) {
				removeEdge(edge);
			}
			for (ModelEdge edge : node.getOutgoingEdges()) {
				removeEdge(edge);
			}
			EObject refObj = node.getReferencedObject();
			if (refObj != null) {
				objectNodeMappings.get(refObj).remove(node);
			}
		}
		return nodes.remove(node);
	}

	/**
	 * Removes the edge from the graph.
	 * 
	 * @param edge an edege to remove from the graph
	 * @return true if the edge was present in the graph - false otherwise
	 */
	public boolean removeEdge(ModelEdge edge) {
		edge.getSource().getOutgoingEdges().remove(edge);
		edge.getTarget().getIncomingEdges().remove(edge);
		EObject refObj = edge.getReferencedObject();
		if (refObj != null) {
			objectEdgeMappings.get(refObj).remove(edge);
		}
		return edges.remove(edge);
	}

	public Set<EObject> getAllNodeReferences() {
		return objectNodeMappings.keySet();
	}

	public Set<EObject> getAllEdgeReferences() {
		return objectEdgeMappings.keySet();
	}
	
	/**
	 * Sets the root node of this graph. If <code>root</code> is not <code>null</code> it needs to be part of the graph.
	 * The semantics of the root are not defined here and may depend on the context in which the graph is used.
	 * 
	 * @param root the node which should be treated as the root
	 * @throws GraphManipulationException if <code>root</code> is neither null nor part of the graph
	 */
	public void setRoot(ModelNode root) throws GraphManipulationException {
		if (root != null && !getNodes().contains(root)) {
			throw new GraphManipulationException("The root must be null or a node which is part of the graph");
		}
		this.root = root;
	}

	public ModelNode getRoot() {
		return root;
	}
}
