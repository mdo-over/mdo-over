package recombination.crossoverpoint;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.junit.jupiter.api.Test;

import metamodels.genericTestModel.A;
import metamodels.genericTestModel.GenericTestModelPackage;
import model.CoSpan;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraphFactory;
import model.modelgraph.ModelNode;
import recombination.RecombinationException;
import recombination.crossoverpoint.inference.IInferenceStrategy;
import recombination.crossoverpoint.postprocessing.DefaultAttributeProcessor;
import recombination.crossoverpoint.postprocessing.IAttributeProcessor;
import testinterfaces.GraphTest;
import utils.creational.MinimalTestModelBuilder;
import utils.creational.TestCrossoverPointBuilder;
import utils.creational.TestModelFactory;
import utils.creational.TestSplitBuilder;

class CrossoverPointRecombinationTest extends GraphTest {

	private static final Engine ENGINE = new EngineImpl();
	private static final IInferenceStrategy MOCKED_INFERENCE = mock(IInferenceStrategy.class);
	
	@Test
	void test_SplitsWithDifferingAttributeBeingPartOfCrossoverPoint_Exception()
			throws GraphManipulationException {
		// Both parts: Root, A
		MinimalTestModelBuilder firstCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (firstCodomainBuilder.aNode.getReferencedObject())).setValue(0);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		CoSpan firstSplit = firstSplitBuilder.getSplit();

		// Both parts: Root, A
		MinimalTestModelBuilder secondCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (secondCodomainBuilder.aNode.getReferencedObject())).setValue(1);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		CoSpan secondSplit = secondSplitBuilder.getSplit();

		// Both nodes with edge in cp
		TestCrossoverPointBuilder crossoverPointBuilder = new TestCrossoverPointBuilder(firstSplit, secondSplit);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootNode, secondCodomainBuilder.rootNode);
		crossoverPointBuilder.identify(firstCodomainBuilder.aNode, secondCodomainBuilder.aNode);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootToA, secondCodomainBuilder.rootToA);

		when(MOCKED_INFERENCE.inferCrossoverPoint(any(), any())).thenReturn(crossoverPointBuilder.getCrossoverPoint());
		DefaultAttributeProcessor attributeProcessor = new DefaultAttributeProcessor();
		CrossoverPointRecombination recombination = new CrossoverPointRecombination(MOCKED_INFERENCE,
				attributeProcessor, ENGINE);
		assertThrows(InvalidRecombinationRuleException.class,
				() -> recombination.recombine(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit()));
	}
	
	@Test
	void test_SplitsWithDifferingAttributesOutsideCrossoverPoint_OffspringShouldContainBothNodesAndTheirAttributes()
			throws GraphManipulationException {
		// Both split parts: Root, A
		MinimalTestModelBuilder firstCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (firstCodomainBuilder.aNode.getReferencedObject())).setValue(0);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		CoSpan firstSplit = firstSplitBuilder.getSplit();

		// Both split parts: Root, A
		MinimalTestModelBuilder secondCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (secondCodomainBuilder.aNode.getReferencedObject())).setValue(1);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		CoSpan secondSplit = secondSplitBuilder.getSplit();

		// Only Root in cp
		TestCrossoverPointBuilder crossoverPointBuilder = new TestCrossoverPointBuilder(firstSplit, secondSplit);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootNode, secondCodomainBuilder.rootNode);
		
		saveGraph(firstSplit.getFirstDomain(), "firstSplitFirstPart", false);
		saveGraph(firstSplit.getSecondDomain(), "firstSplitSecondPart", false);
		saveGraph(secondSplit.getFirstDomain(), "secondSplitFirstPart", false);
		saveGraph(secondSplit.getSecondDomain(), "secondSplitSecondPart", false);

		when(MOCKED_INFERENCE.inferCrossoverPoint(any(), any())).thenReturn(crossoverPointBuilder.getCrossoverPoint());
		DefaultAttributeProcessor attributeProcessor = new DefaultAttributeProcessor();
		CrossoverPointRecombination recombination = new CrossoverPointRecombination(MOCKED_INFERENCE,
				attributeProcessor, ENGINE);
		List<EObject> offspring = recombination.recombine(firstSplitBuilder.getSplit(), secondSplitBuilder.getSplit());

		// Root, A, A2
		MinimalTestModelBuilder allExpectedOff = TestModelFactory.createMinimalModel();
		ModelNode a2Node = allExpectedOff.createA();
		ModelEdge rootToA2 = allExpectedOff.createRootToA(allExpectedOff.rootNode, a2Node);
		((A) (allExpectedOff.aNode.getReferencedObject())).setValue(0);
		((A) a2Node.getReferencedObject()).setValue(1);

		assertTrue(areIsomorphic(offspring.get(0), allExpectedOff.emfModelRoot));
		assertTrue(areIsomorphic(offspring.get(1), allExpectedOff.emfModelRoot));
		saveGraph(ModelGraphFactory.getInstance().createModelGraph(offspring.get(0)), "firstOffspring", false);
		saveGraph(ModelGraphFactory.getInstance().createModelGraph(offspring.get(1)), "secondOffspring");
	}
	
	@Test
	void test_SplitsWithDifferingAttributeOutsideCrossoverPointButInCrossoverPointNodeUnhandled_Exception()
			throws GraphManipulationException {
		// Both parts: Root, A
		MinimalTestModelBuilder firstCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (firstCodomainBuilder.aNode.getReferencedObject())).setValue(0);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		CoSpan firstSplit = firstSplitBuilder.getSplit();

		// Both parts: Root, A
		MinimalTestModelBuilder secondCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (secondCodomainBuilder.aNode.getReferencedObject())).setValue(1);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		CoSpan secondSplit = secondSplitBuilder.getSplit();

		// Both nodes with edge in cp
		TestCrossoverPointBuilder crossoverPointBuilder = new TestCrossoverPointBuilder(firstSplit, secondSplit);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootNode, secondCodomainBuilder.rootNode);
		crossoverPointBuilder.identify(firstCodomainBuilder.aNode, secondCodomainBuilder.aNode);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootToA, secondCodomainBuilder.rootToA);
		EStructuralFeature valueFeature = firstCodomainBuilder.aNode.getReferencedObject().eClass().getEStructuralFeature("value");
		crossoverPointBuilder.unidentifyAttribute(firstCodomainBuilder.aNode, (EAttribute)valueFeature);
				
		saveGraph(firstSplit.getFirstDomain(), "firstSplitFirstPart", false);
		saveGraph(firstSplit.getSecondDomain(), "firstSplitSecondPart", false);
		saveGraph(secondSplit.getFirstDomain(), "secondSplitFirstPart", false);
		saveGraph(secondSplit.getSecondDomain(), "secondSplitSecondPart", false);

		when(MOCKED_INFERENCE.inferCrossoverPoint(any(), any())).thenReturn(crossoverPointBuilder.getCrossoverPoint());		

		CrossoverPointRecombination recombination = new CrossoverPointRecombination(MOCKED_INFERENCE,
				new DefaultAttributeProcessor(), ENGINE);
		assertThrows(RecombinationException.class, () -> recombination.recombine(firstSplit, secondSplit));
	}
	
	@Test
	void test_SplitsWithDifferingAttributeOutsideCrossoverPointButInCrossoverPointNodeWithHandler_OffspringContainsNodeWithProcessedAttribute()
			throws GraphManipulationException {
		// Both parts: Root, A
		MinimalTestModelBuilder firstCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (firstCodomainBuilder.aNode.getReferencedObject())).setValue(1);
		TestSplitBuilder firstSplitBuilder = new TestSplitBuilder(firstCodomainBuilder.graph);
		firstSplitBuilder.extendBothDomainsBy(firstCodomainBuilder.rootToA);
		CoSpan firstSplit = firstSplitBuilder.getSplit();

		// Both parts: Root, A
		MinimalTestModelBuilder secondCodomainBuilder = TestModelFactory.createMinimalModel();
		((A) (secondCodomainBuilder.aNode.getReferencedObject())).setValue(2);
		TestSplitBuilder secondSplitBuilder = new TestSplitBuilder(secondCodomainBuilder.graph);
		secondSplitBuilder.extendBothDomainsBy(secondCodomainBuilder.rootToA);
		CoSpan secondSplit = secondSplitBuilder.getSplit();

		// Both nodes with edge in cp
		TestCrossoverPointBuilder crossoverPointBuilder = new TestCrossoverPointBuilder(firstSplit, secondSplit);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootNode, secondCodomainBuilder.rootNode);
		crossoverPointBuilder.identify(firstCodomainBuilder.aNode, secondCodomainBuilder.aNode);
		crossoverPointBuilder.identify(firstCodomainBuilder.rootToA, secondCodomainBuilder.rootToA);
		EStructuralFeature valueFeature = firstCodomainBuilder.aNode.getReferencedObject().eClass().getEStructuralFeature("value");
		crossoverPointBuilder.unidentifyAttribute(firstCodomainBuilder.aNode, (EAttribute)valueFeature);
				
		saveGraph(firstSplit.getFirstDomain(), "firstSplitFirstPart", false);
		saveGraph(firstSplit.getSecondDomain(), "firstSplitSecondPart", false);
		saveGraph(secondSplit.getFirstDomain(), "secondSplitFirstPart", false);
		saveGraph(secondSplit.getSecondDomain(), "secondSplitSecondPart", false);

		when(MOCKED_INFERENCE.inferCrossoverPoint(any(), any())).thenReturn(crossoverPointBuilder.getCrossoverPoint());		

		CrossoverPointRecombination recombination = new CrossoverPointRecombination(MOCKED_INFERENCE,
				new SumUpProcessor(), ENGINE);
		List<EObject> offspring = recombination.recombine(firstSplit, secondSplit);

		// Root, A, A2
		MinimalTestModelBuilder allExpectedOff = TestModelFactory.createMinimalModel();
		((A) (allExpectedOff.aNode.getReferencedObject())).setValue(3);

		assertTrue(areIsomorphic(offspring.get(0), allExpectedOff.emfModelRoot));
		assertTrue(areIsomorphic(offspring.get(1), allExpectedOff.emfModelRoot));
		saveGraph(ModelGraphFactory.getInstance().createModelGraph(offspring.get(0)), "firstOffspring", false);
		saveGraph(ModelGraphFactory.getInstance().createModelGraph(offspring.get(1)), "secondOffspring");
	}
	
	private class SumUpProcessor implements IAttributeProcessor {
		@Override
		public Object processAttribute(EObject firstParent, EObject secondParent, EAttribute attribute) {
			if (attribute == GenericTestModelPackage.Literals.A__VALUE) {
				return (int)firstParent.eGet(attribute) + (int)secondParent.eGet(attribute);
			}
			return null;
		}

		@Override
		public boolean acceptsAttribute(EAttribute attribute) {
			if (attribute == GenericTestModelPackage.Literals.A__VALUE) {
				return true;
			}
			return false;
		}
	}
}
