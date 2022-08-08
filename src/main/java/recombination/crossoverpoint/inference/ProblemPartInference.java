package recombination.crossoverpoint.inference;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.model.Node;

import graphtools.SubGraphConstructor;
import graphtools.emf.GraphRuleAdapter;
import graphtools.emf.ModelGraphToEmfModelConverter;
import model.CoSpan;
import model.ModelGraphMapping;
import model.Span;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelNode;
import recombination.RecombinationException;

/**
 * An inference strategy constructing a crossover point which comprises only the problem part of the optimization
 * problem. It uses Henshin to find a mapping between the problem parts of the split points of the two input splits. If
 * several such mappings exist, one of them is used non-deterministically.
 * 
 * <p>
 * Note: The root node of a {@link ModelGraph} is always considered to be part of the problem. For all other nodes, the
 * {@link EReference#isContainment() containment reference} of their referenced object is determining.
 * 
 * @author Despro
 */
public class ProblemPartInference implements IInferenceStrategy {
	/**
	 * Set of EReferences considered to be elements of the problem part. Note that source and target of each EReference
	 * are also considered elements of the problem part.
	 */
	private final Set<EReference> problemEdgeTypes;
	private final Engine engine;

	/**
	 * Constructs the strategy specifying which edges are considered elements of the problem part.
	 * 
	 * @param problemEdgeTypes edges considered elements of the problem part
	 * @param engine           a Henshin engine used to drive Henshin rules
	 */
	public ProblemPartInference(Set<EReference> problemEdgeTypes, Engine engine) {
		this.problemEdgeTypes = problemEdgeTypes;
		this.engine = engine;
	}

	@Override
	public Span inferCrossoverPoint(CoSpan firstSplit, CoSpan secondSplit) {
		ModelGraphMapping cpToFirstSplitPoint = createMappingCrossoverPointToFirstSplitPoint(firstSplit);
		ModelGraphMapping cpToSecondSplitPoint = createMappingCrossoverPointToSecondSplitPoint(secondSplit,
				cpToFirstSplitPoint);

		Span crossoverPoint = new Span(cpToFirstSplitPoint, cpToSecondSplitPoint);
		return crossoverPoint;
	}

	private ModelGraphMapping createMappingCrossoverPointToFirstSplitPoint(CoSpan firstSplit) {
		ModelGraph firstSplitPoint = firstSplit.getPullback();
		SubGraphConstructor crossoverPointConstructor = new SubGraphConstructor(firstSplitPoint);
		setPullbackRoot(firstSplit);
		try {
			crossoverPointConstructor.addNodeCopy(firstSplitPoint.getRoot());
			if (problemEdgeTypes != null && !problemEdgeTypes.isEmpty()) {
				for (ModelEdge edge : firstSplitPoint.getEdges()) {
					if (problemEdgeTypes.contains(edge.getReferencedObject())) {
						crossoverPointConstructor.addEdgeCopy(edge);
					}
				}
			}
		} catch (GraphManipulationException e) {
			e.printStackTrace();
			throw new RecombinationException("Crossover point could not be constructed from first split.");
		}
		return crossoverPointConstructor.getMapping();
	}

	private ModelGraphMapping createMappingCrossoverPointToSecondSplitPoint(CoSpan secondSplit,
			ModelGraphMapping cpToFirstSplitPoint) {
		ModelGraph secondSplitPoint = secondSplit.getPullback();
		ModelGraphMapping cpToSecondSplitPoint = new ModelGraphMapping(cpToFirstSplitPoint.getOriginGraph(),
				secondSplitPoint);

		if (!cpToFirstSplitPoint.getOriginGraph().getElements().isEmpty()) {
			setPullbackRoot(secondSplit);
			GraphRuleAdapter henshinAdapter = new GraphRuleAdapter(engine, cpToFirstSplitPoint.getOriginGraph());
			ModelGraphToEmfModelConverter converter = new ModelGraphToEmfModelConverter();

			EObject emfModelOfSecondSplitPoint = converter.createEmfModel((ModelGraph) secondSplitPoint);
			Match match = henshinAdapter.findMatch(emfModelOfSecondSplitPoint);
			if (match == null) {
				throw new RecombinationException("Problem parts of parents are not isomorph.");
			}
			
			// TODO: Should we check if the match covers all problem elements of second split?

			for (ModelNode cpNode : cpToFirstSplitPoint.getOriginGraph().getNodes()) {
				Node ruleNode = henshinAdapter.getGraphToRuleMap().get(cpNode);
				EObject nodeOfEmfModelOfSecondSplitPoint = match.getNodeTarget(ruleNode);
				ModelNode secondSplitPointNode = converter.getOrigin(nodeOfEmfModelOfSecondSplitPoint);
				cpToSecondSplitPoint.addMapping(cpNode, secondSplitPointNode);

				// Add mapping for outgoing edges leading to matched nodes
				for (ModelEdge cpEdge : cpNode.getOutgoingEdges()) {
					Node targetRuleNode = henshinAdapter.getGraphToRuleMap().get(cpEdge.getTarget());
					EObject targetNodeOfEmfModelOfSecondSplitPoint = match.getNodeTarget(targetRuleNode);
					ModelNode secondSplitPointTargetNode = converter.getOrigin(targetNodeOfEmfModelOfSecondSplitPoint);
					for (ModelEdge spEdge : secondSplitPointNode.getOutgoingEdges()) {
						if (spEdge.getTarget() == secondSplitPointTargetNode) {
							cpToSecondSplitPoint.addMapping(cpEdge, spEdge);
							break;
						}
					}
				}
			}
		}
		return cpToSecondSplitPoint;
	}

	private void setPullbackRoot(CoSpan split) {
		ModelGraph splitPoint = split.getPullback();
		ModelNode codomainRoot = split.getCodomain().getRoot();
		ModelNode firstDomainRoot = (ModelNode) split.getFirstDomainToCodomain().getOrigin(codomainRoot);
		ModelNode firstSplitPointRoot = (ModelNode) split.getPullbackToFirstDomain().getOrigin(firstDomainRoot);
		try {
			splitPoint.setRoot(firstSplitPointRoot);
		} catch (GraphManipulationException e) {
			e.printStackTrace();
			throw new RecombinationException("Could not set root on split point.");
		}
	}
}
