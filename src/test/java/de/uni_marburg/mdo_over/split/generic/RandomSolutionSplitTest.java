package de.uni_marburg.mdo_over.split.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
import java.util.Random;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.uni_marburg.mdo_over.model.CoSpan;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphFactory;
import de.uni_marburg.mdo_over.testinterfaces.GraphTest;
import de.uni_marburg.mdo_over.utils.ResourceUtils;

class RandomSolutionSplitTest extends GraphTest {
	
	Random rngMock = mock(Random.class, withSettings().withoutAnnotations());

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void testAllElementsInFirstSplitPart(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		RandomElementwiseSplit randomSplit = new RandomElementwiseSplit(1.0, null);
		CoSpan split = randomSplit.split(graph);
		ModelGraph firstDomain = split.getFirstDomain();
		ModelGraph secondDomain = split.getSecondDomain();
		assertEquals(7, firstDomain.getNodes().size());
		assertEquals(16, firstDomain.getEdges().size());
		assertEquals(0, secondDomain.getNodes().size());
		assertEquals(0, secondDomain.getEdges().size());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void testAllElementsInSecondSplitPart(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		RandomElementwiseSplit randomSplit = new RandomElementwiseSplit(0.0, null);
		CoSpan split = randomSplit.split(graph);
		assertEquals(0, split.getFirstDomain().getNodes().size());
		assertEquals(0, split.getFirstDomain().getEdges().size());
		assertEquals(7, split.getSecondDomain().getNodes().size());
		assertEquals(16, split.getSecondDomain().getEdges().size());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@DisplayName("Only the first node is put in the second part. To prevent dangling everything, including the first node, should be in first part.")
	void testFirstNodeInSecondSplitPart(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);

		// Only add the first node to the second split part
		doReturn(1d, 0d).when(rngMock).nextDouble();

		RandomElementwiseSplit randomSplit = new RandomElementwiseSplit(rngMock, 0.5, null);
		CoSpan split = randomSplit.split(graph);
		ModelGraph firstDomain = split.getFirstDomain();
		ModelGraph secondDomain = split.getSecondDomain();
		saveGraph(firstDomain, "firstDomain", false);
		saveGraph(secondDomain, "secondDomain");
		assertEquals(7, split.getFirstDomain().getNodes().size());
		assertEquals(16, split.getFirstDomain().getEdges().size());
		assertEquals(1, split.getSecondDomain().getNodes().size());
		assertEquals(0, split.getSecondDomain().getEdges().size());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@DisplayName("When all nodes are put in the second part all edges should be there as well.")
	void testAllNodesInSecondSplitPart(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);

		// For all nodes rng decides for second split part but for all edges it decides for first split part
		Double[] randomValues = new Double[graph.getNodes().size()];
		Arrays.fill(randomValues, 1d);
		randomValues[graph.getNodes().size() - 1] = 0d;
		doReturn(1d, (Object[]) randomValues).when(rngMock).nextDouble();

		RandomElementwiseSplit randomSplit = new RandomElementwiseSplit(rngMock, 0.5, null);
		CoSpan split = randomSplit.split(graph);
		assertEquals(0, split.getFirstDomain().getNodes().size());
		assertEquals(0, split.getFirstDomain().getEdges().size());
		assertEquals(7, split.getSecondDomain().getNodes().size());
		assertEquals(16, split.getSecondDomain().getEdges().size());
	}
}
