package crossover;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import metamodels.architectureCRA.ArchitectureCRAPackage;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphFactory;
import testinterfaces.GraphTest;
import utils.ResourceUtils;

class ProblemPartCrossoverTest extends GraphTest {
	
	private static final Engine ENGINE = new EngineImpl();

	@Test
	@Disabled
	void integrationTest() {
		Set<EObject> problemEdgeTypes = new HashSet<>();
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.CLASS_MODEL__FEATURES);
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.METHOD__DATA_DEPENDENCY);
		problemEdgeTypes.add(ArchitectureCRAPackage.Literals.METHOD__FUNCTIONAL_DEPENDENCY);
				
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
}
