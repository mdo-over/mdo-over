package de.uni_marburg.mdo_over.graphtools.emf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.uni_marburg.mdo_over.model.modelgraph.GraphConversionException;
import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphFactory;
import de.uni_marburg.mdo_over.utils.ResourceUtils;

class ModelGraphToEmfModelConverterTest {

	@BeforeEach
	public void setupCommonStructure() throws GraphManipulationException {

	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@DisplayName("Check structural equivalence of input and created model.")
	void testCreateEquivalentEmfModel(boolean dynamic) throws GraphConversionException {
		EObject root = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		ModelGraphToEmfModelConverter converter = new ModelGraphToEmfModelConverter();
		EObject createdRoot = converter.createEmfModel(graph);

		/*
		 * If the list of target objects for a multi-valued EReference in the copy might have a different order than in
		 * the original model the following assertion fails. To use EcoreUtils.equals() care has to be taken that for a
		 * many-valued EReference the edges of the ModelGraph are added in the same order as the target objects appear
		 * in the EList stored by EReference.
		 */
		assertTrue(EcoreUtil.equals(root, createdRoot));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@DisplayName("Create EMF model from a graph where one node is not contained by the root.")
	void testCreateEmfModelWithMissingContainment(boolean dynamic) {
		EObject root = ResourceUtils.loadCRAModel("models/cra/BidirectionalEdgesProblemOnly.xmi", dynamic);
		ModelGraph graph = ModelGraphFactory.getInstance().createModelGraph(root);
		ModelEdge methodContainment = graph.getRoot().getOutgoingEdges().iterator().next();
		graph.removeEdge(methodContainment);
		ModelGraphToEmfModelConverter converter = new ModelGraphToEmfModelConverter();
		assertThrows(IllegalArgumentException.class, () -> {converter.createEmfModel(graph);});
	}

}
