package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.CoSpan.PullbackConstructionException;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelNode;
import testinterfaces.GraphTest;

class CoSpanTest extends GraphTest {
	
	private ModelGraph firstDomain;
	private ModelNode firstDomainSource;
	private ModelNode firstDomainTarget;
	private ModelEdge firstDomainEdge;
	
	private ModelGraph secondDomain;
	private ModelNode secondDomainSource;
	private ModelNode secondDomainTarget;
	private ModelEdge secondDomainEdge;
	
	private ModelGraph coDomain;
	private ModelNode coDomainSource;
	private ModelNode coDomainTarget;
	private ModelEdge coDomainEdge;
	
	@BeforeEach
	public void setupCommonGraphs() throws GraphManipulationException {
		firstDomain = new ModelGraph();
		firstDomainSource = new ModelNode();
		firstDomainTarget = new ModelNode();
		firstDomainEdge = new ModelEdge(firstDomainSource, firstDomainTarget);
		firstDomain.addNode(firstDomainSource);
		firstDomain.addNode(firstDomainTarget);
		firstDomain.addEdge(firstDomainEdge);
		
		secondDomain = new ModelGraph();
		secondDomainSource = new ModelNode();
		secondDomainTarget = new ModelNode();
		secondDomainEdge = new ModelEdge(secondDomainSource, secondDomainTarget);
		secondDomain.addNode(secondDomainSource);
		secondDomain.addNode(secondDomainTarget);
		secondDomain.addEdge(secondDomainEdge);
		
		coDomain = new ModelGraph();
		coDomainSource = new ModelNode();
		coDomainTarget = new ModelNode();
		coDomainEdge = new ModelEdge(coDomainSource, coDomainTarget);
		coDomain.addNode(coDomainSource);
		coDomain.addNode(coDomainTarget);
		coDomain.addEdge(coDomainEdge);
	}

	@Test
	@DisplayName("Calculate pullback of a cospan with completely disjoint domains.")
	void testCalculatePullbackOfDisjointCoSpan() throws GraphManipulationException {	
		ModelGraphMapping firstToCodomain = new ModelGraphMapping(firstDomain, coDomain);
		ModelGraphMapping secondToCodomain = new ModelGraphMapping(secondDomain, coDomain);
		CoSpan cospan = new CoSpan(firstToCodomain, secondToCodomain);
		firstDomain.removeNode(firstDomainTarget);
		secondDomain.removeNode(secondDomainSource);
		firstToCodomain.addMapping(firstDomainSource, coDomainSource);
		secondToCodomain.addMapping(secondDomainTarget, coDomainTarget);
		ModelGraph pullback = cospan.getPullback();
		saveGraph(pullback, "pullback");
		assertEquals(0, pullback.getElements().size());
	}
	
	@Test
	@DisplayName("Calculate pullback of a cospan with completely overlapping domains.")
	void testCalculatePullbackOfFullyOverlappingCospan() throws GraphManipulationException {	
		ModelGraphMapping firstToCodomain = new ModelGraphMapping(firstDomain, coDomain);
		ModelGraphMapping secondToCodomain = new ModelGraphMapping(secondDomain, coDomain);
		CoSpan cospan = new CoSpan(firstToCodomain, secondToCodomain);
		firstToCodomain.addMapping(firstDomainSource, coDomainSource);
		firstToCodomain.addMapping(firstDomainTarget, coDomainTarget);
		firstToCodomain.addMapping(firstDomainEdge, coDomainEdge);
		secondToCodomain.addMapping(secondDomainSource, coDomainSource);
		secondToCodomain.addMapping(secondDomainTarget, coDomainTarget);
		secondToCodomain.addMapping(secondDomainEdge, coDomainEdge);
		ModelGraph pullback = cospan.getPullback();
		saveGraph(pullback, "pullback");		
		assertEquals(2, pullback.getNodes().size());
		assertEquals(1, pullback.getEdges().size());
		ModelEdge pbEdge = pullback.getEdges().iterator().next();
		assertEquals(firstDomainEdge, cospan.getPullbackToFirstDomain().getImage(pbEdge));
		assertEquals(secondDomainEdge, cospan.getPullbackToSecondDomain().getImage(pbEdge));
	}
	
	@Test
	@DisplayName("Calculate pullback of a cospan with partially overlapping domains.")
	void testCalculatePullbackOfPartiallyOverlappingCospan() throws GraphManipulationException {	
		ModelGraphMapping firstToCodomain = new ModelGraphMapping(firstDomain, coDomain);
		ModelGraphMapping secondToCodomain = new ModelGraphMapping(secondDomain, coDomain);
		CoSpan cospan = new CoSpan(firstToCodomain, secondToCodomain);
		firstToCodomain.addMapping(firstDomainSource, coDomainSource);
		firstToCodomain.addMapping(firstDomainTarget, coDomainTarget);
		secondToCodomain.addMapping(secondDomainSource, coDomainSource);
		ModelGraph pullback = cospan.getPullback();
		saveGraph(pullback, "pullback");		
		assertEquals(1, pullback.getNodes().size());
		assertEquals(0, pullback.getEdges().size());
		ModelNode pbNode = pullback.getNodes().iterator().next();
		assertEquals(firstDomainSource, cospan.getPullbackToFirstDomain().getImage(pbNode));
		assertEquals(secondDomainSource, cospan.getPullbackToSecondDomain().getImage(pbNode));
	}
	
	@Test
	@DisplayName("Calculate pullback of a cospan causing a dangling edge.")
	void testCalculatePullbackOfPartiallyOverlappingCospanWithDanglingEdge() throws GraphManipulationException {	
		ModelGraphMapping firstToCodomain = new ModelGraphMapping(firstDomain, coDomain);
		ModelGraphMapping secondToCodomain = new ModelGraphMapping(secondDomain, coDomain);
		CoSpan cospan = new CoSpan(firstToCodomain, secondToCodomain);
		firstToCodomain.addMapping(firstDomainSource, coDomainSource);
		firstToCodomain.addMapping(firstDomainEdge, coDomainEdge);
		secondToCodomain.addMapping(secondDomainSource, coDomainSource);
		secondToCodomain.addMapping(secondDomainEdge, coDomainEdge);
		assertThrows(PullbackConstructionException.class, () -> cospan.getPullback());
	}
}
