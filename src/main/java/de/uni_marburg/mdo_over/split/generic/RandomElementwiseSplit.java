package de.uni_marburg.mdo_over.split.generic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import de.uni_marburg.mdo_over.graphtools.SubGraphConstructor;
import de.uni_marburg.mdo_over.model.CoSpan;
import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;
import de.uni_marburg.mdo_over.split.ISplitStrategy;

/**
 * A split strategy splitting a graph by keeping a specified problem part in both of the generated split parts. The
 * solution part nodes are randomly distributed with respect to a user desired distribution ratio. Afterwards all
 * solution edges are distributed favoring the split part where both source and target node are located. If both split
 * parts contain the same number of edge nodes the edge is allocated randomly and, if appropriate, the winning split
 * part is complemented with the missing edge node to prevent dangling edges. This may lead to split parts overlapping
 * in their solutions nodes.
 * <p>
 * <i>Note:</i> Every {@link ModelGraphElement} of the graphs handled by this strategy is expected to possess a
 * <code>non-null</code> {@link ModelGraphElement#getReferencedObject() object reference}.
 * 
 * @author S. John
 *
 */
public class RandomElementwiseSplit implements ISplitStrategy {

	private static final double EDGE_DISTRIBUTION_RATIO = 0.5d;
	private final Random rng;
	private final double nodeDistribution;
	private final Set<EObject> problemParts;

	public RandomElementwiseSplit(double nodeDistributionRatio, Set<EObject> problemParts) {
		this(new Random(), nodeDistributionRatio, problemParts);
	}

	public RandomElementwiseSplit(Random rng, double nodeDistribution, Set<EObject> problemParts) {
		this.rng = rng;
		this.nodeDistribution = nodeDistribution;
		if (problemParts == null) {
			this.problemParts = new HashSet<EObject>(0);
		} else {
			this.problemParts = problemParts;
		}
	}

	/**
	 * Splits each graph in the given list separately applying the strategy described in {@link RandomElementwiseSplit}.
	 * For each graph in the list a corresponding cospan is created. The order of cospans in the output matches the
	 * order of the corresponding graphs in the input.
	 */
	@Override
	public CoSpan split(ModelGraph graph) {
		SubGraphConstructor firstPart = new SubGraphConstructor(graph);
		SubGraphConstructor secondPart = new SubGraphConstructor(graph);

		try {
			for (ModelNode node : graph.getNodes()) {
				if (referencesProblemPart(node)) {
					firstPart.addNodeCopy(node);
					secondPart.addNodeCopy(node);
				} else {
					SubGraphConstructor randomPart = getRandomPart(nodeDistribution, firstPart, secondPart);
					randomPart.addNodeCopy(node);
				}
			}
			for (ModelEdge edge : graph.getEdges()) {
				if (referencesProblemPart(edge)) {
					firstPart.addEdgeCopy(edge);
					secondPart.addEdgeCopy(edge);
				} else {
					SubGraphConstructor chosenPart = getPreferablePart(edge, firstPart, secondPart);
					chosenPart.addEdgeCopy(edge);
				}
			}
		} catch (GraphManipulationException e) {
			// Per construction all nodes should be copies without edge references.
			// Further, for each copied edge the copied nodes should be added before the edge.
			throw new IllegalStateException("Construction of split should use copies of nodes without edge references."
					+ " For each copied edge the required nodes should be added first.");
		}
		CoSpan split = new CoSpan(firstPart.getMapping(), secondPart.getMapping());
		return split;
	}

	private SubGraphConstructor getRandomPart(double distributionRatio, SubGraphConstructor firstSplitPart, SubGraphConstructor secondSplitPart) {
		return (rng.nextDouble() < distributionRatio ? firstSplitPart : secondSplitPart);
	}

	private SubGraphConstructor getPreferablePart(ModelEdge edge, SubGraphConstructor firstSplitPart, SubGraphConstructor secondSplitPart) {
		SubGraphConstructor preferablePart;
		int firstContained = containedEdgeNodes(edge, firstSplitPart);
		int secondContained = containedEdgeNodes(edge, secondSplitPart);
		if (firstContained == secondContained) {
			preferablePart = getRandomPart(EDGE_DISTRIBUTION_RATIO, firstSplitPart, secondSplitPart);
		} else {
			preferablePart = (firstContained > secondContained ? firstSplitPart : secondSplitPart);
		}
		return preferablePart;
	}

	/**
	 * Returns the number of nodes of the given edge that are already part of the given split part.
	 * @param edge edge connecting the nodes of interest
	 * @param splitPart split part checked for the containment of nodes
	 * @return number of edge nodes contained in the split part
	 */
	private int containedEdgeNodes(ModelEdge edge, SubGraphConstructor splitPart) {
		int count = 0;
		if (splitPart.getMapping().getOrigin(edge.getSource()) != null) {
			count++;
		}
		if (splitPart.getMapping().getOrigin(edge.getTarget()) != null) {
			count++;
		}
		return count;
	}

	private boolean referencesProblemPart(ModelGraphElement element) {
		boolean isProblemPart = false;
		EObject referencedObject = element.getReferencedObject();
		if (referencedObject == null) {
			throw new IllegalStateException("All elements of a graph need to specify a reference object.");
		}
		// TODO: Check if this distinction is enough
		if (referencedObject instanceof EReference) {
			isProblemPart = problemParts.contains(referencedObject);
		} else {
			isProblemPart = problemParts.contains(referencedObject.eClass());
		}
		return isProblemPart;
	}
}
