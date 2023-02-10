package split.emf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import graphtools.SubGraphConstructor;
import model.CoSpan;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.MalformedGraphException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelNode;
import split.ISplitStrategy;
import split.SplitException;

/**
 * A split strategy expecting the input graph to be a one-to-one representation of an EMF model, i.e., each node
 * references an {@link EObject} and each edge an {@link EReference} between such objects. Furthermore, the root of the
 * graph represents an {@link EObject} transitively containing all other nodes via edges representing
 * {@link EReference#isContainment() containments}.
 * <p>
 * The problem part of the graph is copied to both of the generated split parts. The problem part is specified by a set
 * of edges. Whether or not a node is part of the problem is already specified by whether or not its incoming
 * containment edge is part of the problem. In order to not construct empty split parts, the root is considered to be
 * part of the problem and is always contained in both split parts.
 * <p>
 * Subtrees, w.r.t. the containment hierarchy, connected to the problem part are distributed randomly among the split
 * parts. If only one subtree exists which is directly connected to the problem part, the root of that subtree is copied
 * to both split parts (i.e., it is considered to be part of the split point). The subtrees directly connected to the
 * subtree root are then considered next for random distribution. This extension of the split point continues until
 * there is either more than one subtree available to distribute or the whole containment hierarchy is part of the split
 * point.
 * <p>
 * All non-containment edges not belonging to the specified problem part are copied to the split part which contains the
 * majority of the nodes connected by the edge. In case both split parts contain the same number of the edge's nodes,
 * the edge is copied to one split part randomly. If the chosen split part lacks one of the edge's nodes, that node is
 * also copied to that split part together with all of its containment ancestors.
 * 
 * @author S. John
 *
 */
public class ContainmentSubtreeSplit implements ISplitStrategy {

	private class EdgeCollector {
		/**
		 * The containment edges directly connected to the current split point (i.e., the part of the model which is already
		 * distributed to both split parts). 
		 */
		List<ModelEdge> borderContainments = new ArrayList<>();
		List<ModelEdge> solutionNonContainments = new ArrayList<>();
		List<ModelEdge> problemNonContainments = new ArrayList<>();

	}

	private final Random rng;
	private final double distributionRatio;
	/**
	 * Set of EReferences considered to be elements of the problem part. Note that source and target of each EReference
	 * are also considered problem elements.
	 */
	private final Set<EReference> problemEdgeTypes;
	private SubGraphConstructor firstPart;
	private SubGraphConstructor secondPart;

	public ContainmentSubtreeSplit(double distributionRatio, Set<EReference> problemEdgeTypes) {
		this(new Random(), distributionRatio, problemEdgeTypes);
	}

	/**
	 * A user may provide a random number generator used for randomly distributing elements between split parts. The
	 * distribution ratio determines whether or not there is a skew in the distribution. A ratio of <code>0</code> will
	 * cause all distributable elements to be assigned to the first split part; likewise a ratio of <code>1</code> will
	 * put all elements in the second split part.
	 * 
	 * @param rng               a random number generator
	 * @param distributionRatio a ratio determining the distribution of elements
	 * @param problemEdgeTypes  a set of edges defining the problem part of the graphs
	 */
	public ContainmentSubtreeSplit(Random rng, double distributionRatio, Set<EReference> problemEdgeTypes) {
		this.rng = rng;
		this.distributionRatio = distributionRatio;
		if (problemEdgeTypes == null) {
			this.problemEdgeTypes = new HashSet<EReference>(0);
		} else {
			this.problemEdgeTypes = problemEdgeTypes;
		}
	}

	@Override
	public CoSpan split(ModelGraph graph) {
		firstPart = new SubGraphConstructor(graph);
		secondPart = new SubGraphConstructor(graph);

		if (!graph.getElements().isEmpty()) {
			ModelNode root = graph.getRoot();
			try {
				if (root == null) {
					throw new MalformedGraphException("Graph contains no root.");
				}

				EdgeCollector edgeCollector = new EdgeCollector();
				copyRoot(root);
				copyProblemContainmentHierarchy(root, edgeCollector);
				copyProblemNonContainments(edgeCollector.problemNonContainments);

				while (edgeCollector.borderContainments.size() == 1) {
					ModelEdge borderContainment = edgeCollector.borderContainments.get(0);
					edgeCollector.borderContainments.clear();
					extendSplitPoint(borderContainment, edgeCollector);
				}

				if (edgeCollector.borderContainments.size() > 1) {
					distributeContainmentSubtrees(edgeCollector);
				}

				// TODO: Sanity check whether all nodes have been processed (graph contains no unreachable nodes)

				distributeSolutionEdges(edgeCollector.solutionNonContainments);

			} catch (MalformedGraphException | GraphManipulationException e) {
				e.printStackTrace();
				throw new SplitException("Graph could not be split successfully.");
			}
		}
		CoSpan split = new CoSpan(firstPart.getMapping(), secondPart.getMapping());
		return split;
	}

	private void copyRoot(ModelNode root) throws GraphManipulationException {
		ModelNode firstCopy = firstPart.addNodeCopy(root);
		firstPart.getMapping().getOriginGraph().setRoot(firstCopy);
		ModelNode secondCopy = secondPart.addNodeCopy(root);
		secondPart.getMapping().getOriginGraph().setRoot(secondCopy);
	}

	/**
	 * Performs a breadth first search of the graph which should be split starting with its root node. Containments
	 * specified as {@link #problemEdgeTypes} and their target nodes are copied to both split parts. Non-containment
	 * edges and containments connecting the problem part to the solution part are collected in the given
	 * <code>edgeCollector</code> for further processing.
	 * 
	 * @param root          the root of the graph to split
	 * @param edgeCollector helper to collect edges for further processing
	 * @throws GraphManipulationException
	 * @throws MalformedGraphException
	 */
	private void copyProblemContainmentHierarchy(ModelNode root, EdgeCollector edgeCollector)
			throws GraphManipulationException, MalformedGraphException {
		for (ModelEdge edge : root.getOutgoingEdges()) {
			processEdge(edge, edgeCollector);
		}
	}

	private void copyProblemNonContainments(List<ModelEdge> problemEdges) throws GraphManipulationException {
		for (ModelEdge problemEdge : problemEdges) {
			firstPart.addEdgeCopy(problemEdge);
			secondPart.addEdgeCopy(problemEdge);
			addOppositeEdge(problemEdge, firstPart);
			addOppositeEdge(problemEdge, secondPart);
		}
	}

	/**
	 * Extends the split point, i.e., the set of elements copied to both split parts, by the given containment edge as
	 * well as by the target node of that edge. Non-containment edges and containment edges connecting the extended
	 * split point to the rest of the graph are collected in the given {@link EdgeCollector} for further processing.
	 * 
	 * @param edgeCollector helper to collect edges for further processing
	 * @throws GraphManipulationException
	 * @throws MalformedGraphException
	 */
	private void extendSplitPoint(ModelEdge containmentEdge, EdgeCollector edgeCollector)
			throws GraphManipulationException, MalformedGraphException {
		if (containmentEdge.getReferencedObject() == null
				|| !(containmentEdge.getReferencedObject() instanceof EReference)
				|| !((EReference) containmentEdge.getReferencedObject()).isContainment()) {
			throw new IllegalArgumentException("Containment edge expected.");
		}
		ModelNode borderNode = containmentEdge.getTarget();
		firstPart.addEdgeCopy(containmentEdge);
		secondPart.addEdgeCopy(containmentEdge);
		addOppositeEdge(containmentEdge, firstPart);
		addOppositeEdge(containmentEdge, secondPart);

		for (ModelEdge edge : borderNode.getOutgoingEdges()) {
			// Will not get recursive since all problem edges are already processed.
			processEdge(edge, edgeCollector);
		}
	}

	/**
	 * Processes the given edge. In case of a containment edge of the problem part, the edge and its target node are
	 * copied to both split parts and the outgoing edges of the target node are processed as well. Other edges are put
	 * into the correct lists for later processing. Note: edges connecting at least one solution node cannot be an
	 * element of the problem part. As containments cannot create a cycle, the recursive processing will terminate.
	 * 
	 * @param edge          the processed edge
	 * @param edgeCollector helper to collect edges for further processing
	 * @throws MalformedGraphException
	 * @throws GraphManipulationException
	 */
	private void processEdge(ModelEdge edge, EdgeCollector edgeCollector)
			throws MalformedGraphException, GraphManipulationException {
		if (edge.getReferencedObject() == null) {
			throw new MalformedGraphException("Edge does not reference an EObject.");
		}
		if (!(edge.getReferencedObject() instanceof EReference)) {
			throw new MalformedGraphException("Edge does not represent an EReference.");
		}
		EReference eReference = (EReference) edge.getReferencedObject();
		boolean isContainment = eReference.isContainment();

		// TODO Maybe we need an iterative way of doing this in order to process large models.
		if (problemEdgeTypes.contains(eReference)) {
			if (isContainment) {
				ModelNode targetNode = edge.getTarget();
				firstPart.addEdgeCopy(edge);
				secondPart.addEdgeCopy(edge);
				addOppositeEdge(edge, firstPart);
				addOppositeEdge(edge, secondPart);

				for (ModelEdge targetEdge : targetNode.getOutgoingEdges()) {
					processEdge(targetEdge, edgeCollector);
				}
			} else {
				edgeCollector.problemNonContainments.add(edge);
			}
		} else {
			if (isContainment) {
				edgeCollector.borderContainments.add(edge);
			} else {
				edgeCollector.solutionNonContainments.add(edge);
			}
		}
	}

	/**
	 * Distributes containment subtrees, starting with the containment edges in <code>borderContainments</code>,
	 * randomly among the split parts. Non-containment solution edges emerging from these containment subtrees are
	 * collected in the given list for further processing. All problem parts have already been processed at this time.
	 * 
	 * @param edgeCollector helper to collect edges for further processing
	 * @throws GraphManipulationException
	 * @throws MalformedGraphException
	 */
	private void distributeContainmentSubtrees(EdgeCollector edgeCollector)
			throws GraphManipulationException, MalformedGraphException {
		for (ModelEdge borderContainment : edgeCollector.borderContainments) {
			if (borderContainment.getReferencedObject() == null
					|| !(borderContainment.getReferencedObject() instanceof EReference)
					|| !((EReference) borderContainment.getReferencedObject()).isContainment()) {
				throw new IllegalArgumentException("List of containment edges expected.");
			}
			SubGraphConstructor randomPart = getRandomPart(distributionRatio, firstPart, secondPart);
			addCopyOfContainmentSubtree(borderContainment, randomPart, edgeCollector.solutionNonContainments);
		}
	}

	private void addCopyOfContainmentSubtree(ModelEdge containment, SubGraphConstructor splitPart,
			List<ModelEdge> solutionNonContainments) throws GraphManipulationException, MalformedGraphException {
		ModelNode target = containment.getTarget();
		splitPart.addEdgeCopy(containment);
		ModelEdge oppositeEdge = addOppositeEdge(containment, splitPart);

		for (ModelEdge edge : target.getOutgoingEdges()) {
			if (((EReference) edge.getReferencedObject()).isContainment()) {
				addCopyOfContainmentSubtree(edge, splitPart, solutionNonContainments);
			} else {
				if (edge != oppositeEdge) {
					solutionNonContainments.add(edge);
				}
			}
		}
	}

	/**
	 * Distributes the edges in the given list among the split parts. A split part is preferred if it contains more edge
	 * nodes than the other split part. Otherwise the split part is chosen randomly.
	 * 
	 * @param solutionEdges list of edges to distribute
	 * @throws GraphManipulationException
	 */
	private void distributeSolutionEdges(List<ModelEdge> solutionEdges) throws GraphManipulationException {
		Set<ModelEdge> distributedEdges = new HashSet<>();
		for (ModelEdge solutionEdge : solutionEdges) {
			if (distributedEdges.contains(solutionEdge)) {
				continue;
			}
			SubGraphConstructor chosenPart = getPreferablePart(solutionEdge, firstPart, secondPart);
			ModelNode source = solutionEdge.getSource();
			if (chosenPart.getMapping().getOrigin(source) == null) {
				chosenPart.addNodeCopy(source);
				addContainmentAncestors(source, chosenPart);
			}
			ModelNode target = solutionEdge.getTarget();
			if (chosenPart.getMapping().getOrigin(target) == null) {
				chosenPart.addNodeCopy(target);
				addContainmentAncestors(target, chosenPart);
			}
			chosenPart.addEdgeCopy(solutionEdge);
			ModelEdge oppositeEdge = addOppositeEdge(solutionEdge, chosenPart);
			if (oppositeEdge != null) {
				distributedEdges.add(oppositeEdge);
			}
		}
	}

	/**
	 * Adds all containment ancestors of the specified node by traversing backwards through the containment edges. Stops
	 * as soon as an ancestor is found which is already part of the specified split part.
	 * 
	 * @param node      node to add to the split part together with its ancestors
	 * @param splitPart split part to add the node to
	 * @throws GraphManipulationException
	 */
	private void addContainmentAncestors(ModelNode node, SubGraphConstructor splitPart)
			throws GraphManipulationException {
		for (ModelEdge edge : node.getIncomingEdges()) {
			if (((EReference) edge.getReferencedObject()).isContainment()) {
				if (splitPart.getMapping().getOrigin(edge) == null) {
					ModelNode source = edge.getSource();
					if (splitPart.getMapping().getOrigin(source) == null) {
						splitPart.addNodeCopy(source);
					}
					splitPart.addEdgeCopy(edge);
					addContainmentAncestors(source, splitPart);
				}
				addOppositeEdge(edge, splitPart);
				break;
			}
		}
	}

	private SubGraphConstructor getPreferablePart(ModelEdge edge, SubGraphConstructor firstSplitPart,
			SubGraphConstructor secondSplitPart) {
		SubGraphConstructor preferablePart;
		int firstCount = calcContainedEdgeNodes(edge, firstSplitPart);
		int secondCount = calcContainedEdgeNodes(edge, secondSplitPart);
		if (firstCount == secondCount) {
			preferablePart = getRandomPart(distributionRatio, firstSplitPart, secondSplitPart);
		} else {
			preferablePart = (firstCount > secondCount ? firstSplitPart : secondSplitPart);
		}
		return preferablePart;
	}

	private int calcContainedEdgeNodes(ModelEdge edge, SubGraphConstructor splitPart) {
		int count = 0;
		if (splitPart.getMapping().getOrigin(edge.getSource()) != null) {
			count++;
		}
		if (splitPart.getMapping().getOrigin(edge.getTarget()) != null) {
			count++;
		}
		return count;
	}

	private SubGraphConstructor getRandomPart(double distributionRatio, SubGraphConstructor firstSplitPart,
			SubGraphConstructor secondSplitPart) {
		return (rng.nextDouble() < distributionRatio ? firstSplitPart : secondSplitPart);
	}

	private ModelEdge addOppositeEdge(ModelEdge edge, SubGraphConstructor splitPart) throws GraphManipulationException {
		EReference contRef = (EReference) edge.getReferencedObject();
		EReference oppositeRef = contRef.getEOpposite();
		ModelEdge oppositeEdge = null;
		if (oppositeRef != null) {
			ModelNode target = edge.getTarget();
			List<ModelEdge> oppositeEdges = target.getOutgoingEdges().stream()
					.filter((o) -> o.getTarget() == edge.getSource())
					.filter((o) -> o.getReferencedObject() == oppositeRef).collect(Collectors.toList());
			if (oppositeEdges.size() != 1) {
				throw new MalformedGraphException(
						"Graph contains invalid number of opposite edges for the edge " + edge + " referencing " + contRef);
			}
			oppositeEdge = oppositeEdges.iterator().next();
			splitPart.addEdgeCopy(oppositeEdge);
		}
		return oppositeEdge;
	}
}
