package de.uni_marburg.mdo_over.model;

import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

//TODO: Double-check compliance of name and description with the categorial background.
/** 
 * Models a cospan in the category of {@link ModelGraph model graphs}. As one-to-one mappings are used to model the
 * cospan, only injective morphisms between pullback, domains and codomain are considered. Identifying equivalent nodes
 * and edges by the mappings between the domains and the codomain, the pullback is constructed as the intersection of
 * nodes and edges of both domains.
 * <p>
 * <i>Note:</i> The calculated pullback and its mappings into the domains of the cospan are not reflecting changes to
 * the cospan structure. If the cospan changes a {@link #calculatePullback() recalculation} of the pullback is needed.
 * 
 * @author S. John
 *
 */
public class CoSpan {

	private final ModelGraphMapping firstDomainToCodomain;
	private final ModelGraphMapping secondDomainToCodomain;
	private ModelGraphMapping pullbackToFirstDomain;
	private ModelGraphMapping pullbackToSecondDomain;

	public CoSpan(ModelGraphMapping firstDomainToCodomain, ModelGraphMapping secondDomainToCodomain) {
		this.firstDomainToCodomain = firstDomainToCodomain;
		this.secondDomainToCodomain = secondDomainToCodomain;
	}

	/**
	 * Returns the pullback graph for the cospan. Calculates the pullback if no pullback has been calculated, yet.
	 * 
	 * @return the pullback graph
	 */
	public ModelGraph getPullback() {
		if (pullbackToFirstDomain == null) {
			calculatePullback();
		}
		return pullbackToFirstDomain.getOriginGraph();
	}

	/**
	 * Returns the mapping from the pullback to the first domain. Calculates the pullback if no pullback has been
	 * calculated, yet.
	 * 
	 * @return mapping from the pullback to the first domain
	 */
	public ModelGraphMapping getPullbackToFirstDomain() {
		if (pullbackToFirstDomain == null) {
			calculatePullback();
		}
		return pullbackToFirstDomain;
	}

	/**
	 * Returns the mapping from the pullback to the second domain. Calculates the pullback if no pullback has been
	 * calculated, yet.
	 * 
	 * @return mapping from the pullback to the second domain
	 */
	public ModelGraphMapping getPullbackToSecondDomain() {
		if (pullbackToSecondDomain == null) {
			calculatePullback();
		}
		return pullbackToSecondDomain;
	}

	/**
	 * Calculates the pullback and its mappings to the domains of the current cospan.
	 */
	private void calculatePullback() {
		ModelGraph pullback = new ModelGraph();
		pullbackToFirstDomain = new ModelGraphMapping(pullback, firstDomainToCodomain.getOriginGraph());
		pullbackToSecondDomain = new ModelGraphMapping(pullback, secondDomainToCodomain.getOriginGraph());

		CalculationMappings calcMaps = getDomainSizeOptimizedMappings();

		ModelGraph smallerDomain = calcMaps.mapWithSmallerDomain.getOriginGraph();
		for (ModelNode node : smallerDomain.getNodes()) {
			ModelGraphElement coDomainImage = calcMaps.mapWithSmallerDomain.getImage(node);
			ModelGraphElement secondDomainOrigin = calcMaps.mapWithLargerDomain.getOrigin(coDomainImage);
			if (secondDomainOrigin != null) {
				ModelNode copy = new ModelNode(node);
				try {
					pullback.addNode(copy);
					calcMaps.pullbackToSmallerDomain.addMapping(copy, node);
					calcMaps.pullbackToLargerDomain.addMapping(copy, secondDomainOrigin);
				} catch (GraphManipulationException e) {
					pullbackToFirstDomain = null;
					pullbackToSecondDomain = null;
					throw new PullbackConstructionException("Node could not be added to pullback.");
				}
			}
		}

		for (ModelEdge edge : smallerDomain.getEdges()) {
			ModelGraphElement codomainElement = calcMaps.mapWithSmallerDomain.getImage(edge);
			ModelGraphElement secondDomainElement = calcMaps.mapWithLargerDomain.getOrigin(codomainElement);
			if (secondDomainElement != null) {
				ModelEdge copy = new ModelEdge(edge);
				ModelGraphElement pullbackSourceElement = calcMaps.pullbackToSmallerDomain.getOrigin(edge.getSource());
				copy.setSource((ModelNode) pullbackSourceElement);
				ModelGraphElement pullbackTargetElement = calcMaps.pullbackToSmallerDomain.getOrigin(edge.getTarget());
				copy.setTarget((ModelNode) pullbackTargetElement);

				try {
					pullback.addEdge(copy);
					calcMaps.pullbackToSmallerDomain.addMapping(copy, edge);
					calcMaps.pullbackToLargerDomain.addMapping(copy, secondDomainElement);
				} catch (GraphManipulationException e) {
					pullbackToFirstDomain = null;
					pullbackToSecondDomain = null;
					throw new PullbackConstructionException("Dangling edge occurred during pullback construction.");
				}
			}
		}
	}

	private CalculationMappings getDomainSizeOptimizedMappings() {
		ModelGraphMapping mapWithSmallerDomain;
		ModelGraphMapping mapWithLargerDomain;
		ModelGraphMapping pullbackToSmallerDomain;
		ModelGraphMapping pullbackToLargerDomain;

		int firstMapDomainSize = firstDomainToCodomain.getOriginGraph().getElements().size();
		int secondMapDomainSize = secondDomainToCodomain.getOriginGraph().getElements().size();
		if (firstMapDomainSize < secondMapDomainSize) {
			mapWithSmallerDomain = firstDomainToCodomain;
			mapWithLargerDomain = secondDomainToCodomain;
			pullbackToSmallerDomain = pullbackToFirstDomain;
			pullbackToLargerDomain = pullbackToSecondDomain;

		} else {
			mapWithSmallerDomain = secondDomainToCodomain;
			mapWithLargerDomain = firstDomainToCodomain;
			pullbackToSmallerDomain = pullbackToSecondDomain;
			pullbackToLargerDomain = pullbackToFirstDomain;
		}
		return new CalculationMappings(mapWithSmallerDomain, mapWithLargerDomain, pullbackToSmallerDomain,
				pullbackToLargerDomain);
	}

	public ModelGraph getFirstDomain() {
		return firstDomainToCodomain.getOriginGraph();
	}

	public ModelGraph getSecondDomain() {
		return secondDomainToCodomain.getOriginGraph();
	}

	public ModelGraph getCodomain() {
		return firstDomainToCodomain.getImageGraph();
	}

	public ModelGraphMapping getFirstDomainToCodomain() {
		return firstDomainToCodomain;
	}

	public ModelGraphMapping getSecondDomainToCodomain() {
		return secondDomainToCodomain;
	}

	private class CalculationMappings {
		private ModelGraphMapping mapWithSmallerDomain;
		private ModelGraphMapping mapWithLargerDomain;
		private ModelGraphMapping pullbackToSmallerDomain;
		private ModelGraphMapping pullbackToLargerDomain;

		public CalculationMappings(ModelGraphMapping mapWithSmallerDomain, ModelGraphMapping mapWithLargerDomain,
				ModelGraphMapping pullbackToSmallerDomain, ModelGraphMapping pullbackToLargerDomain) {
			this.mapWithSmallerDomain = mapWithSmallerDomain;
			this.mapWithLargerDomain = mapWithLargerDomain;
			this.pullbackToSmallerDomain = pullbackToSmallerDomain;
			this.pullbackToLargerDomain = pullbackToLargerDomain;
		}
	}
	
	@SuppressWarnings("serial")
	public class PullbackConstructionException extends RuntimeException {
		public PullbackConstructionException(String msg) {
			super(msg);
		}
	}
}
