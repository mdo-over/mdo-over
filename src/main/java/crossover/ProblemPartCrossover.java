package crossover;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.Engine;

import model.CoSpan;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphFactory;
import recombination.IRecombinationStrategy;
import recombination.crossoverpoint.CrossoverPointRecombination;
import recombination.crossoverpoint.inference.ProblemPartInference;
import recombination.crossoverpoint.postprocessing.DefaultAttributeProcessor;
import split.ISplitStrategy;
import split.emf.ContainmentSubtreeSplit;

public class ProblemPartCrossover implements ICrossoverStrategy {

	private ISplitStrategy splitStr;
	private IRecombinationStrategy recombineStr;
	public ModelGraphFactory graphFac = ModelGraphFactory.getInstance();

	public ProblemPartCrossover(Set<EObject> problemPartTypes, Engine engine) {
		Set<EReference> problemEdgeTypes = problemPartTypes.stream().filter(EReference.class::isInstance)
				.map(EReference.class::cast).collect(Collectors.toSet());
		this.splitStr = new ContainmentSubtreeSplit(0.5, problemEdgeTypes);
		ProblemPartInference inferenceStrategy = new ProblemPartInference(problemEdgeTypes, engine);
		DefaultAttributeProcessor attributeProcessor = new DefaultAttributeProcessor();
		this.recombineStr = new CrossoverPointRecombination(inferenceStrategy, attributeProcessor, engine);
	}

	@Override
	public List<EObject> evolve(EObject firstParent, EObject secondParent) {
		ModelGraph firstGraph = graphFac.createModelGraph(firstParent);
		ModelGraph secondGraph = graphFac.createModelGraph(secondParent);

		CoSpan firstSplit = splitStr.split(firstGraph);
		CoSpan secondSplit = splitStr.split(secondGraph);
		
		List<EObject> offspring = recombineStr.recombine(firstSplit, secondSplit);
		return offspring;
	}
}
