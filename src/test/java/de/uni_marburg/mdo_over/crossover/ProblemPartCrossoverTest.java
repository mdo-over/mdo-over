package de.uni_marburg.mdo_over.crossover;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.junit.jupiter.api.Test;

import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphFactory;
import de.uni_marburg.mdo_over.recombination.RecombinationException;
import de.uni_marburg.mdo_over.testinterfaces.GraphTest;
import de.uni_marburg.mdo_over.utils.ResourceUtils;
import metamodels.architectureCRA.ArchitectureCRAPackage;

class ProblemPartCrossoverTest extends GraphTest {
	
	private static final Engine ENGINE = new EngineImpl();

	@Test
	//@Disabled
	/* TODO: When the test class is run in isolation or as the first test class of the suite, the tests succeed. 
	 * However, running other test cases before (e.g.,
	 * de.uni_marburg.mdo_over.split.generic.RandomSolutionSplitTest#testFirstNodeInSecondSplitPart
	 * the integrationTest fails (see TestSuite.java for debugging the problem). Strangely, the other test case does not change 
	 * the loaded model. There might be a conflict in how the meta model is stored in the registry.
	 */ 
	void integrationTest() {
		Set<EObject> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.CLASS_MODEL__FEATURES);
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.METHOD__DATA_DEPENDENCY);
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.METHOD__FUNCTIONAL_DEPENDENCY);
				
		// As problem parts are specified on implemented (static) metamodel, test will fail for dynamically loaded models.
		EObject firstModel = ResourceUtils.loadCRAModel("models/cra/SolutionWith2Classes.xmi", false);
		EObject secondModel = ResourceUtils.loadCRAModel("models/cra/SolutionWithOneClass.xmi", false);
		ProblemPartCrossover ppc = new ProblemPartCrossover(problemEdgeTypes, ENGINE);
		
		List<EObject> offspring = ppc.evolve(firstModel, secondModel);
		ModelGraphFactory fac = ModelGraphFactory.getInstance();
		ListIterator<String> prefix = Arrays.asList(new String[] {"first", "second"}).listIterator();
		for (EObject o : offspring) {
			ModelGraph oGraph = fac.createModelGraph(o);
			saveGraph(oGraph, prefix.next() + "OffSpring", false);
		}
		
	}
	
	@Test
	//@Disabled
	void integrationTestDifferentProblemParts() {
		Set<EObject> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.CLASS_MODEL__FEATURES);
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.METHOD__DATA_DEPENDENCY);
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.METHOD__FUNCTIONAL_DEPENDENCY);
		
		// As problem parts are specified on implemented (static) metamodel, test will fail for dynamically loaded models.
		EObject firstModel = ResourceUtils.loadCRAModel("models/cra/SolutionWithOneClass.xmi", false);
		EObject secondModel = ResourceUtils.loadCRAModel("models/cra/SolutionWithOneClassDifferentlyNamedAttributes.xmi", false);
		ProblemPartCrossover ppc = new ProblemPartCrossover(problemEdgeTypes, ENGINE);
		
		RecombinationException e = assertThrows(RecombinationException.class,	
				() -> ppc.evolve(firstModel, secondModel));
		assertTrue(e.getMessage().contains("not isomorph"));
	}
}
