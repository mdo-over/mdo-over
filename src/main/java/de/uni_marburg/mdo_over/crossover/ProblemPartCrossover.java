package de.uni_marburg.mdo_over.crossover;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.Engine;

import de.uni_marburg.mdo_over.model.CoSpan;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphFactory;
import de.uni_marburg.mdo_over.recombination.IRecombinationStrategy;
import de.uni_marburg.mdo_over.recombination.crossoverpoint.CrossoverPointRecombination;
import de.uni_marburg.mdo_over.recombination.crossoverpoint.inference.ProblemPartInference;
import de.uni_marburg.mdo_over.recombination.crossoverpoint.postprocessing.DefaultAttributeProcessor;
import de.uni_marburg.mdo_over.split.ISplitStrategy;
import de.uni_marburg.mdo_over.split.emf.ContainmentSubtreeSplit;

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
