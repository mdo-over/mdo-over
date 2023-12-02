package de.uni_marburg.mdo_over.graphtools;

import de.uni_marburg.mdo_over.model.ModelGraphMapping;
import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

/**
 * Supports the construction of subgraphs of a {@link ModelGraph}. To that end, it maintains a {@link ModelGraphMapping}
 * from the subgraph to the original graph and offers methods to extend the subgraph.
 * 
 * @author S. John
 */
public class SubGraphConstructor {

	private final ModelGraph completeGraph;
	private final ModelGraph subGraph;
	private final ModelGraphMapping subToCompleteGraph;

	public SubGraphConstructor(ModelGraph completeGraph) {
		this.completeGraph = completeGraph;
		this.subGraph = new ModelGraph();
		subToCompleteGraph = new ModelGraphMapping(subGraph, this.completeGraph);
	}

	/**
	 * Adds a copy of a node of the complete graph to the subgraph if the node is not already represented there.
	 * 
	 * @param node a node of the underlying complete graph
	 * @return the node representing the given node in the subgraph
	 * @throws GraphManipulationException if adding the node was aborted - in this case the subgraph remains unchanged
	 */
	public ModelNode addNodeCopy(ModelNode node) throws GraphManipulationException {
		ModelNode copy = (ModelNode) subToCompleteGraph.getOrigin(node);
		if (copy == null) {
			copy = new ModelNode(node);
			boolean addedCopy = subGraph.addNode(copy);
			try {
				subToCompleteGraph.addMapping(copy, node);
			} catch (IllegalArgumentException e) {
				if (addedCopy) {
					subGraph.removeNode(copy);
				}
				throw e;
			}
		}
		return copy;
	}

	/**
	 * Adds a copy of an edge of the complete graph to the subgraph if the edge is not already represented there. Also
	 * creates and adds copies of source and target nodes if necessary.
	 * 
	 * @param edge an edge of the underlying complete graph
	 * @return the edge representing the given edge in the subgraph
	 * @throws GraphManipulationException if adding the edge was aborted - in this case the subgraph remains unchanged
	 */
	public ModelEdge addEdgeCopy(ModelEdge edge) throws GraphManipulationException {
		ModelEdge copy = (ModelEdge) subToCompleteGraph.getOrigin(edge);
		if (copy == null) {
			copy = new ModelEdge(edge);
			boolean addedSource = false;
			boolean addedTarget = false;
			boolean addedEdge = false;
			ModelNode sourceCopy = null;
			ModelNode targetCopy = null;
			try {
				ModelNode source = edge.getSource();
				sourceCopy = (ModelNode) subToCompleteGraph.getOrigin(source);
				if (sourceCopy == null) {
					sourceCopy = addNodeCopy(source);
					addedSource = true;
				}
				ModelNode target = edge.getTarget();
				targetCopy = (ModelNode) subToCompleteGraph.getOrigin(target);
				if (targetCopy == null) {
					targetCopy = addNodeCopy(target);
					addedTarget = true;
				}
				copy.setSource(sourceCopy);
				copy.setTarget(targetCopy);
				addedEdge = subGraph.addEdge(copy);
				subToCompleteGraph.addMapping(copy, edge);
			} catch (GraphManipulationException | IllegalArgumentException e) {
				if (addedEdge) {
					subGraph.removeEdge(edge);
				}
				if (addedSource) {
					subGraph.removeNode(sourceCopy);
				}
				if (addedTarget) {
					subGraph.removeNode(targetCopy);
				}
				throw e;
			}
		}
		return copy;
	}

	public ModelGraph getCompleteGraph() {
		return completeGraph;
	}

	public ModelGraph getSubGraph() {
		return subGraph;
	}

	public ModelGraphMapping getMapping() {
		return subToCompleteGraph;
	}
}
