package split.emf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.eclipse.emf.ecore.EReference;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import model.CoSpan;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphElement;
import model.modelgraph.ModelNode;
import testinterfaces.GraphTest;
import utils.creational.LinearTestModelBuilder;
import utils.creational.OneLevelModelBuilder;
import utils.creational.TestModelBuilder;
import utils.creational.TestModelFactory;
import utils.creational.TwoLevelModelBuilder;

class ContainmentSubtreeSplitTest extends GraphTest {

	Random rngMock = mock(Random.class, withSettings().withoutAnnotations());

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_EmptyGraph_EmptySplitParts(boolean dynamic) throws GraphManipulationException {
		TestModelBuilder modelBuilder = new TestModelBuilder();
		// Distribution ratio should be irrelevant
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(0, null);
		CoSpan split = splitStr.split(modelBuilder.graph);
		assertTrue(split.getFirstDomain().getElements().isEmpty());
		assertTrue(split.getSecondDomain().getElements().isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_OnlyRoot_CopiedToBothSplitPartsAndSetAsRoot(boolean dynamic) throws GraphManipulationException {
		TestModelBuilder modelBuilder = new TestModelBuilder();
		ModelNode rootNode = modelBuilder.createRoot();
		// Distribution ratio should be irrelevant
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(0, null);
		CoSpan split = splitStr.split(modelBuilder.graph);
		assertTrue(areRepresentedInBothDomains(split, rootNode));
		assertNotNull(split.getFirstDomain().getRoot());
		assertNotNull(split.getSecondDomain().getRoot());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_LinearContainmentSubtreeNoProblemPart_AllElementsCopiedToBothSplitParts(boolean dynamic)
			throws GraphManipulationException {
		LinearTestModelBuilder linearBuilder = TestModelFactory.createLinearModel();
		// Distribution ratio should be irrelevant
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(0, null);
		CoSpan split = splitStr.split(linearBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");
		assertTrue(areRepresentedInBothDomains(split, linearBuilder.rootNode, linearBuilder.aNode,
				linearBuilder.rootToA, linearBuilder.xNode, linearBuilder.aToX));
		assertTrue(areIsomorphic(split.getFirstDomain(), split.getSecondDomain()));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_LinearContainmentSubtreeWithNonContainmentNoProblemPart_OnlyNonContainmentDistributed(boolean dynamic)
			throws GraphManipulationException {

		LinearTestModelBuilder linearBuilder = TestModelFactory.createLinearModel();
		ModelEdge xToA = linearBuilder.createXToA(linearBuilder.xNode, linearBuilder.aNode);

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, null);

		CoSpan split = splitStr.split(linearBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");
		assertTrue(areRepresentedInBothDomains(split, linearBuilder.rootNode, linearBuilder.aNode,
				linearBuilder.rootToA, linearBuilder.xNode, linearBuilder.aToX));
		assertNotNull(areRepresentedInSingleDomain(split, xToA));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_LinearContainmentSubtreeWithNonContainmentAllProblemPart_OnlyNonContainmentDistributed(boolean dynamic)
			throws GraphManipulationException {

		LinearTestModelBuilder linearBuilder = TestModelFactory.createLinearModel();
		ModelEdge xToA = linearBuilder.createXToA(linearBuilder.xNode, linearBuilder.aNode);

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		Set<EReference> problemPart = new HashSet<>();
		problemPart.add((EReference) linearBuilder.rootToA.getReferencedObject());
		problemPart.add((EReference) linearBuilder.aToX.getReferencedObject());
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, null);

		CoSpan split = splitStr.split(linearBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");
		assertTrue(areRepresentedInBothDomains(split, linearBuilder.rootNode, linearBuilder.aNode,
				linearBuilder.rootToA, linearBuilder.xNode, linearBuilder.aToX));
		assertNotNull(areRepresentedInSingleDomain(split, xToA));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreesAtRootNoProblemPart_ContainmentsDistributed(boolean dynamic)
			throws GraphManipulationException {

		OneLevelModelBuilder oneLvlBuilder = TestModelFactory.createOneLevelModel();

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, null);

		CoSpan split = splitStr.split(oneLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		assertTrue(areRepresentedInBothDomains(split, oneLvlBuilder.rootNode));

		ModelGraph aDomain = areRepresentedInSingleDomain(split, oneLvlBuilder.aNode, oneLvlBuilder.rootToA);
		ModelGraph bDomain = areRepresentedInSingleDomain(split, oneLvlBuilder.bNode, oneLvlBuilder.rootToB);
		assertNotNull(aDomain);
		assertNotNull(bDomain);
		assertFalse(aDomain == bDomain);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreesAtProblemBorderOnSameLevelFromRoot_ContainmentsDistributed(boolean dynamic)
			throws GraphManipulationException {
		TwoLevelModelBuilder twoLvlBuilder = TestModelFactory.createTwoLevelModel();

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		Set<EReference> problemPart = new HashSet<>();
		problemPart.add((EReference) twoLvlBuilder.rootToA.getReferencedObject());
		problemPart.add((EReference) twoLvlBuilder.rootToB.getReferencedObject());
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, problemPart);

		CoSpan split = splitStr.split(twoLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		assertTrue(areRepresentedInBothDomains(split, twoLvlBuilder.rootNode, twoLvlBuilder.aNode,
				twoLvlBuilder.rootToA, twoLvlBuilder.bNode, twoLvlBuilder.rootToB));
		ModelGraph x1Domain = areRepresentedInSingleDomain(split, twoLvlBuilder.x1Node, twoLvlBuilder.aToX1);
		ModelGraph x2Domain = areRepresentedInSingleDomain(split, twoLvlBuilder.x2Node, twoLvlBuilder.aToX2);
		assertNotNull(x1Domain);
		assertNotNull(x2Domain);
		assertFalse(x1Domain == x2Domain);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreesAtProblemBorderOnDifferentLevelsFromRoot_ContainmentsDistributed(boolean dynamic)
			throws GraphManipulationException {
		TwoLevelModelBuilder twoLvlBuilder = TestModelFactory.createTwoLevelModel();
		twoLvlBuilder.graph.removeNode(twoLvlBuilder.x2Node);

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		Set<EReference> problemPart = new HashSet<>();
		problemPart.add((EReference) twoLvlBuilder.rootToA.getReferencedObject());
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, problemPart);

		CoSpan split = splitStr.split(twoLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		assertTrue(
				areRepresentedInBothDomains(split, twoLvlBuilder.rootNode, twoLvlBuilder.aNode, twoLvlBuilder.rootToA));
		ModelGraph x1Domain = areRepresentedInSingleDomain(split, twoLvlBuilder.x1Node, twoLvlBuilder.aToX1);
		ModelGraph bDomain = areRepresentedInSingleDomain(split, twoLvlBuilder.bNode, twoLvlBuilder.rootToB);
		assertNotNull(x1Domain);
		assertNotNull(bDomain);
		assertFalse(x1Domain == bDomain);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreeWithMultipleNodesAndInnerNonContainment_SubtreeWithNonContainmentIsKeptIntact(
			boolean dynamic) throws GraphManipulationException {
		TwoLevelModelBuilder twoLvlBuilder = TestModelFactory.createTwoLevelModel();
		ModelEdge x1ToA = twoLvlBuilder.createXToA(twoLvlBuilder.x1Node, twoLvlBuilder.aNode);
		ModelEdge x1ToX2 = twoLvlBuilder.createXToX(twoLvlBuilder.x1Node, twoLvlBuilder.x2Node);

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, null);

		CoSpan split = splitStr.split(twoLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		assertTrue(areRepresentedInBothDomains(split, twoLvlBuilder.rootNode));
		ModelGraph firstSubtreeDomain = areRepresentedInSingleDomain(split, twoLvlBuilder.aNode, twoLvlBuilder.rootToA,
				twoLvlBuilder.x1Node, twoLvlBuilder.aToX1, x1ToA, x1ToX2);
		ModelGraph secondSubtreeDomain = areRepresentedInSingleDomain(split, twoLvlBuilder.bNode,
				twoLvlBuilder.rootToB);
		assertNotNull(firstSubtreeDomain);
		assertNotNull(secondSubtreeDomain);
		assertFalse(firstSubtreeDomain == secondSubtreeDomain);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreesWithCrossingNonContainment_OneSplitPartContainsAllTheOtherOnlyAOrB(boolean dynamic)
			throws GraphManipulationException {
		OneLevelModelBuilder oneLvlBuilder = TestModelFactory.createOneLevelModel();
		ModelEdge bToA = oneLvlBuilder.createBToA(oneLvlBuilder.bNode, oneLvlBuilder.aNode);

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, null);

		CoSpan split = splitStr.split(oneLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		assertTrue(areRepresentedInBothDomains(split, oneLvlBuilder.rootNode));
		ModelGraph crossRefDomain = areRepresentedInSingleDomain(split, bToA);
		assertNotNull(crossRefDomain);
		assertEquals(RepresentationResult.ALL, areElementsRepresentedOnceInGraph(crossRefDomain, oneLvlBuilder.aNode,
				oneLvlBuilder.rootToA, oneLvlBuilder.bNode, oneLvlBuilder.rootToB));

		ModelGraph nonCrossRefDomain;
		if (crossRefDomain == split.getFirstDomain()) {
			nonCrossRefDomain = split.getSecondDomain();
		} else {
			nonCrossRefDomain = split.getFirstDomain();
		}
		assertTrue((numberOfElementRepresentionsInGraph(nonCrossRefDomain, oneLvlBuilder.aNode) == 1
				&& numberOfElementRepresentionsInGraph(nonCrossRefDomain, oneLvlBuilder.bNode) == 0)
				|| (numberOfElementRepresentionsInGraph(nonCrossRefDomain, oneLvlBuilder.aNode) == 0
						&& numberOfElementRepresentionsInGraph(nonCrossRefDomain, oneLvlBuilder.bNode) == 1));
	}
	
	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreesWithCrossingNonContainmentOpposite_OneSplitPartContainsBothOpposite(boolean dynamic)
			throws GraphManipulationException {
		OneLevelModelBuilder oneLvlBuilder = TestModelFactory.createOneLevelModel();
		ModelEdge aOppB = oneLvlBuilder.createAOppB(oneLvlBuilder.aNode, oneLvlBuilder.bNode);
		ModelEdge bOppA = oneLvlBuilder.createBOppA(oneLvlBuilder.bNode, oneLvlBuilder.aNode);

		// First two are needed to distribute containment subtrees (rootToA, rootToB).
		when(rngMock.nextDouble()).thenReturn(0d, 1d, 0d, 1d);
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, null);

		CoSpan split = splitStr.split(oneLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		ModelGraph crossRefDomain = areRepresentedInSingleDomain(split, aOppB, bOppA);
		assertNotNull(crossRefDomain);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	void test_ContainmentSubtreesWithCrossingProblemNonContainment_ProblemNonContainmentCopiedToBothSplitParts(
			boolean dynamic) throws GraphManipulationException {
		OneLevelModelBuilder oneLvlBuilder = TestModelFactory.createOneLevelModel();
		ModelEdge bToA = oneLvlBuilder.createBToA(oneLvlBuilder.bNode, oneLvlBuilder.aNode);

		when(rngMock.nextDouble()).thenReturn(0d, 1d);
		Set<EReference> problemPart = new HashSet<>();
		problemPart.add((EReference) oneLvlBuilder.rootToA.getReferencedObject());
		problemPart.add((EReference) oneLvlBuilder.rootToB.getReferencedObject());
		problemPart.add((EReference) bToA.getReferencedObject());
		ContainmentSubtreeSplit splitStr = new ContainmentSubtreeSplit(rngMock, 0.5, problemPart);

		CoSpan split = splitStr.split(oneLvlBuilder.graph);
		saveGraph(split.getFirstDomain(), "firstDomain", false);
		saveGraph(split.getSecondDomain(), "secondDomain");

		assertTrue(areRepresentedInBothDomains(split, oneLvlBuilder.rootNode, oneLvlBuilder.aNode,
				oneLvlBuilder.rootToA, oneLvlBuilder.bNode, oneLvlBuilder.rootToB, bToA));
	}

	/**
	 * Checks if all of the given elements are represented once by one of the domains of the given cospan, exclusively.
	 * If this is the case, the respective domain is returned. If no domain represents all of the elements, some
	 * elements are represented by both domains, or a domain contains duplicate representations for an element,
	 * <code>null</code> is returned.
	 * 
	 * @param cospan   for which the domains are checked
	 * @param elements which should be represented by one of the domains
	 * @return the single domain which contains representations for all of the given elements. Returns <code>null</code>
	 *         if none of the domains represents all elements, some elements are represented in both domains, or a
	 *         domain contains duplicate representations for an element.
	 */
	protected ModelGraph areRepresentedInSingleDomain(CoSpan cospan, ModelGraphElement... elements) {
		ModelGraph domain;
		RepresentationResult inFirst = areElementsRepresentedOnceInGraph(cospan.getFirstDomain(), elements);
		RepresentationResult inSecond = areElementsRepresentedOnceInGraph(cospan.getSecondDomain(), elements);
		if (inFirst.equals(RepresentationResult.ALL) && inSecond.equals(RepresentationResult.NONE)) {
			domain = cospan.getFirstDomain();
		} else if (inFirst.equals(RepresentationResult.NONE) && inSecond.equals(RepresentationResult.ALL)) {
			domain = cospan.getSecondDomain();
		} else {
			domain = null;
		}
		return domain;
	}

	/**
	 * Checks if all of the elements are represented in both domains of the given cospan.
	 * 
	 * @param cospan   for which the domains are checked
	 * @param elements which should be represented by both of the domains
	 * @return true if both domains contain representations for all of the elements - false otherwise
	 */
	protected boolean areRepresentedInBothDomains(CoSpan cospan, ModelGraphElement... elements) {
		RepresentationResult inFirst = areElementsRepresentedOnceInGraph(cospan.getFirstDomain(), elements);
		RepresentationResult inSecond = areElementsRepresentedOnceInGraph(cospan.getSecondDomain(), elements);
		return (inFirst.equals(RepresentationResult.ALL) && inSecond.equals(RepresentationResult.ALL));
	}
}
