package de.uni_marburg.mdo_over.recombination.crossoverpoint.inference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.junit.jupiter.api.Test;

import de.uni_marburg.mdo_over.model.Span;
import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;
import de.uni_marburg.mdo_over.recombination.RecombinationException;
import de.uni_marburg.mdo_over.testinterfaces.GraphTest;
import de.uni_marburg.mdo_over.utils.creational.TestSplitBuilder;
import de.uni_marburg.mdo_over.utils.creational.modeltemplates.LinearTestModelBuilder;
import de.uni_marburg.mdo_over.utils.creational.modeltemplates.OneLevelModelBuilder;
import de.uni_marburg.mdo_over.utils.creational.modeltemplates.TestModelFactory;
import de.uni_marburg.mdo_over.utils.creational.modeltemplates.TwoLevelModelBuilder;
import metamodels.genericTestModel.GenericTestModelPackage;

class ProblemPartInferenceTest extends GraphTest {

	EngineImpl engine = new EngineImpl();

	@Test
	void test_FirstSplitPointLargerInProblemPart_InferenceFails() throws GraphManipulationException {
		LinearTestModelBuilder firstCodomainBuilder = TestModelFactory.createLinearModel();
		ModelNode a2Node = firstCodomainBuilder.createA();
		ModelEdge rootToA2 = firstCodomainBuilder.createRootToA(firstCodomainBuilder.rootNode, a2Node);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA, rootToA2);
		firstSplitBuilder.extendFirstDomainBy(firstCodomainBuilder.aToX);

		LinearTestModelBuilder secondCodomainBuilder = TestModelFactory.createLinearModel();
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		secondSplitBuilder.extendFirstDomainBy(secondCodomainBuilder.aToX);

		Set<EReference> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(GenericTestModelPackage.Literals.ROOT__ACONT);
		ProblemPartInference inference = new ProblemPartInference(problemEdgeTypes, engine);
		RecombinationException e = assertThrows(RecombinationException.class,
				() -> inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit()));
		assertTrue(e.getMessage().contains("not isomorph"));
	}

	// TODO this test fails because of missing sanity checks. probably the input models should be checked for compatible
	// problem parts
	@Test
	void test_SecondSplitPointLargerInProblemPart_InferenceFails() throws GraphManipulationException {
		LinearTestModelBuilder firstCodomainBuilder = TestModelFactory.createLinearModel();
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		firstSplitBuilder.extendFirstDomainBy(firstCodomainBuilder.aToX);

		LinearTestModelBuilder secondCodomainBuilder = TestModelFactory.createLinearModel();
		ModelNode a2Node = secondCodomainBuilder.createA();
		ModelEdge rootToA2 = secondCodomainBuilder.createRootToA(secondCodomainBuilder.rootNode, a2Node);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA, rootToA2);
		secondSplitBuilder.extendFirstDomainBy(secondCodomainBuilder.aToX);

		Set<EReference> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(GenericTestModelPackage.Literals.ROOT__ACONT);
		ProblemPartInference inference = new ProblemPartInference(problemEdgeTypes, engine);
		RecombinationException e = assertThrows(RecombinationException.class,
				() -> inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit()));
		assertTrue(e.getMessage().contains("not isomorph"));
	}
	
	@Test
	void test_SplitPointsDifferInProblemPartAttributeValues_InferenceFails() throws GraphManipulationException {
		LinearTestModelBuilder firstCodomainBuilder = TestModelFactory.createLinearModel();
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.aToX);

		LinearTestModelBuilder secondCodomainBuilder = TestModelFactory.createLinearModel();
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondCodomainBuilder.aNode.getReferencedObject().eSet(GenericTestModelPackage.Literals.A__VALUE, 2);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.aToX);		

		Set<EReference> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(GenericTestModelPackage.Literals.ROOT__ACONT);
		ProblemPartInference inference = new ProblemPartInference(problemEdgeTypes, engine);
		RecombinationException e = assertThrows(RecombinationException.class,
				() -> inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit()));
		assertTrue(e.getMessage().contains("not isomorph"));
	}

	@Test
	void test_EqualSplitsNoProblemPartDefined_CrossoverPointContainsRoot() throws GraphManipulationException {
		LinearTestModelBuilder firstCodomainBuilder = TestModelFactory.createLinearModel();
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		firstSplitBuilder.extendFirstDomainBy(firstCodomainBuilder.aToX);

		LinearTestModelBuilder secondCodomainBuilder = TestModelFactory.createLinearModel();
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		secondSplitBuilder.extendFirstDomainBy(secondCodomainBuilder.aToX);

		ProblemPartInference inference = new ProblemPartInference(null, engine);
		Span cp = inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit());
		assertEquals(1, cp.getDomain().getElements().size());
		areElementsRepresentedOnceInGraph(cp.getDomain(), firstCodomainBuilder.rootNode);
	}

	@Test
	void test_SplitPointsContainOnlyRootNoProblemPartDefined_CrossoverPointContainsRoot()
			throws GraphManipulationException {
		OneLevelModelBuilder firstCodomainBuilder = TestModelFactory.createOneLevelModel();
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendFirstDomainBy(firstCodomainBuilder.rootToA);
		firstSplitBuilder.extendSecondDomainBy(firstCodomainBuilder.rootToB);

		OneLevelModelBuilder secondCodomainBuilder = TestModelFactory.createOneLevelModel();
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendFirstDomainBy(secondCodomainBuilder.rootToA);
		secondSplitBuilder.extendSecondDomainBy(secondCodomainBuilder.rootToB);

		ProblemPartInference inference = new ProblemPartInference(null, engine);
		Span cp = inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit());
		assertEquals(1, cp.getDomain().getElements().size());
		areElementsRepresentedOnceInGraph(cp.getDomain(), firstCodomainBuilder.rootNode);
	}

	@Test
	void test_EqualSplitPointsContainSolutionAndProblemPartWithCrossReferenceBetweenProblemPartNodes_CrossoverPointContainsOnlyProblemPart()
			throws GraphManipulationException {
		TwoLevelModelBuilder firstCodomainBuilder = TestModelFactory.createTwoLevelModel();
		ModelEdge firstBToA = firstCodomainBuilder.createBToA(firstCodomainBuilder.bNode, firstCodomainBuilder.aNode);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);

		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA, firstCodomainBuilder.rootToB,
				firstCodomainBuilder.aToX1, firstCodomainBuilder.aToX2, firstBToA);

		TwoLevelModelBuilder secondCodomainBuilder = TestModelFactory.createTwoLevelModel();
		ModelEdge secondBToA = secondCodomainBuilder.createBToA(secondCodomainBuilder.bNode,
				secondCodomainBuilder.aNode);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA, secondCodomainBuilder.rootToB,
				secondCodomainBuilder.aToX1, secondCodomainBuilder.aToX2, secondBToA);

		Set<EReference> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(GenericTestModelPackage.Literals.ROOT__ACONT);
		problemEdgeTypes.add(GenericTestModelPackage.Literals.ROOT__BCONT);
		ProblemPartInference inference = new ProblemPartInference(problemEdgeTypes, engine);
		Span cp = inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit());
		assertEquals(5, cp.getDomain().getElements().size());
		RepresentationResult r = areElementsRepresentedOnceInGraph(cp.getDomain(), firstCodomainBuilder.rootNode,
				firstCodomainBuilder.aNode, firstCodomainBuilder.bNode, firstCodomainBuilder.rootToA,
				firstCodomainBuilder.rootToB);
		assertEquals(RepresentationResult.ALL, r);
	}

	@Test
	void test_SplitPointsDifferingInSolutionPartContainProblemAndSolutionPart_CrossoverPointContainsProblemPart()
			throws GraphManipulationException {
		// Contains Root, A, B, and X1
		TwoLevelModelBuilder firstCodomainBuilder = TestModelFactory.createTwoLevelModel();
		firstCodomainBuilder.graph.removeNode(firstCodomainBuilder.x2Node);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA, firstCodomainBuilder.rootToB,
				firstCodomainBuilder.aToX1);

		// Contains Root, A, and X2
		TwoLevelModelBuilder secondCodomainBuilder = TestModelFactory.createTwoLevelModel();
		secondCodomainBuilder.graph.removeNode(secondCodomainBuilder.x1Node);
		secondCodomainBuilder.graph.removeNode(secondCodomainBuilder.bNode);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA, secondCodomainBuilder.aToX2);

		Set<EReference> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(GenericTestModelPackage.Literals.ROOT__ACONT);
		ProblemPartInference inference = new ProblemPartInference(problemEdgeTypes, engine);
		Span cp = inference.inferCrossoverPoint(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit());
		assertEquals(3, cp.getDomain().getElements().size());
		RepresentationResult r = areElementsRepresentedOnceInGraph(cp.getDomain(), firstCodomainBuilder.rootNode,
				firstCodomainBuilder.aNode, firstCodomainBuilder.rootToA);
		assertEquals(RepresentationResult.ALL, r);
	}

}
