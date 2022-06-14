package model;

import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelGraph;

public class Span {

	private final ModelGraphMapping domainToFirstCodomain;
	private final ModelGraphMapping domainToSecondCodomain;
	private ModelGraphMapping firstCodomainToPushout;
	private ModelGraphMapping secondCodomainToPushout;

	public Span(ModelGraphMapping domainToFirstCodomain, ModelGraphMapping domainToSecondCodomain) {
		this.domainToFirstCodomain = domainToFirstCodomain;
		this.domainToSecondCodomain = domainToSecondCodomain;
	}

	/**
	 * Returns the pushout graph for the span. Calculates the pushout if no pushout has been calculated, yet.
	 * 
	 * @return the pushout graph
	 * @throws GraphManipulationException thrown if a valid pushout graph could not be constructed
	 */
	public ModelGraph getPushout() throws GraphManipulationException {
		if (firstCodomainToPushout == null) {
			calculatePushout();
		}
		return firstCodomainToPushout.getImageGraph();
	}

	/**
	 * Returns the mapping from the first codomain to the pushout. Calculates the pushout if no pushout has been
	 * calculated, yet.
	 * 
	 * @return mapping from the first codomain to the pushout
	 * @throws GraphManipulationException thrown if a valid pushout graph could not be constructed
	 */
	public ModelGraphMapping getFirstCodomainToPushout() throws GraphManipulationException {
		if (firstCodomainToPushout == null) {
			calculatePushout();
		}
		return firstCodomainToPushout;
	}

	/**
	 * Returns the mapping from the second codomain to the pushout. Calculates the pushout if no pushout has been
	 * calculated, yet.
	 * 
	 * @return mapping from the second codomain to the pushout
	 * @throws GraphManipulationException thrown if a valid pushout graph could not be constructed
	 */
	public ModelGraphMapping getSecondCodomainToPushout() throws GraphManipulationException {
		if (secondCodomainToPushout == null) {
			calculatePushout();
		}
		return secondCodomainToPushout;
	}

	/**
	 * Calculates the pushout and the mappings to it from the codomains of the current span.
	 * 
	 * @throws GraphManipulationException thrown if a valid pushout graph could not be constructed
	 */
	public void calculatePushout() throws GraphManipulationException {
		ModelGraph pushout = new ModelGraph();
		firstCodomainToPushout = new ModelGraphMapping(domainToFirstCodomain.getImageGraph(), pushout);
		secondCodomainToPushout = new ModelGraphMapping(domainToSecondCodomain.getImageGraph(), pushout);

//		CalculationMappings calcMaps = getDomainSizeOptimizedMappings();
//
//		ModelGraph smallerDomain = calcMaps.mapWithSmallerDomain.getOriginGraph();
//		for (ModelNode node : smallerDomain.getNodes()) {
//			ModelGraphElement coDomainImage = calcMaps.mapWithSmallerDomain.getImage(node);
//			ModelGraphElement secondDomainOrigin = calcMaps.mapWithLargerDomain.getOrigin(coDomainImage);
//			if (secondDomainOrigin != null) {
//				ModelNode copy = new ModelNode(node);
//				try {
//					pullback.addNode(copy);
//					calcMaps.pullbackToSmallerDomain.addMapping(copy, node);
//					calcMaps.pullbackToLargerDomain.addMapping(copy, secondDomainOrigin);
//				} catch (GraphManipulationException e) {
//					pullbackToFirstDomain = null;
//					pullbackToSecondDomain = null;
//					throw new IllegalStateException(
//							"Construction of pullback should use copies of nodes without edge references.");
//				}
//			}
//		}
//
//		for (ModelEdge edge : smallerDomain.getEdges()) {
//			ModelGraphElement coDomainImage = calcMaps.mapWithSmallerDomain.getImage(edge);
//			ModelGraphElement secondDomainOrigin = calcMaps.mapWithLargerDomain.getOrigin(coDomainImage);
//			if (secondDomainOrigin != null) {
//				ModelEdge copy = new ModelEdge(edge);
//				ModelGraphElement sourcePullbackOrigin = calcMaps.pullbackToSmallerDomain.getOrigin(edge.getSource());
//				copy.setSource((ModelNode) sourcePullbackOrigin);
//				ModelGraphElement targetPullbackOrigin = calcMaps.pullbackToSmallerDomain.getOrigin(edge.getTarget());
//				copy.setTarget((ModelNode) targetPullbackOrigin);
//
//				try {
//					pullback.addEdge(copy);
//					calcMaps.pullbackToSmallerDomain.addMapping(copy, edge);
//					calcMaps.pullbackToLargerDomain.addMapping(copy, secondDomainOrigin);
//				} catch (GraphManipulationException e) {
//					pullbackToFirstDomain = null;
//					pullbackToSecondDomain = null;
//					throw new GraphManipulationException("The constructed pullback would contain a dangling edge.");
//				}
//			}
//		}
	}

	public ModelGraph getFirstCodomain() {
		return domainToFirstCodomain.getImageGraph();
	}

	public ModelGraph getSecondCodomain() {
		return domainToSecondCodomain.getImageGraph();
	}

	public ModelGraph getDomain() {
		return domainToFirstCodomain.getOriginGraph();
	}

	public ModelGraphMapping getDomainToFirstCodomain() {
		return domainToFirstCodomain;
	}

	public ModelGraphMapping getDomainToSecondCodomain() {
		return domainToSecondCodomain;
	}
}
