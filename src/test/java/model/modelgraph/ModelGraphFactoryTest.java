package model.modelgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import testinterfaces.GraphTest;
import utils.ResourceUtils;

class ModelGraphFactoryTest extends GraphTest {
	
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testSimpleProblemGraph(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SimpleProblemOnly.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		saveGraph(graph, "graph");
		assertEquals(root, graph.getRoot().getReferencedObject());
		assertEquals(3, graph.getNodes().size());
		assertEquals(3, graph.getEdges().size());		
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testBidirectionalEdgesProblemGraph(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/BidirectionalEdgesProblemOnly.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		saveGraph(graph, "graph");
		assertEquals(root, graph.getRoot().getReferencedObject());
		assertEquals(3, graph.getNodes().size());
		assertEquals(4, graph.getEdges().size());		
	}
	
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testSolutionGraph(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		saveGraph(graph, "graph");
		assertEquals(root, graph.getRoot().getReferencedObject());
		assertEquals(7, graph.getNodes().size());
		assertEquals(16, graph.getEdges().size());		
	}
}
