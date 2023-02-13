package recombination.crossoverpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.model.Action;
import org.eclipse.emf.henshin.model.Node;

import graphtools.emf.MappingRuleAdapter;
import graphtools.emf.ModelGraphToEmfModelConverter;
import model.CoSpan;
import model.ModelGraphMapping;
import model.Span;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphElement;
import model.modelgraph.ModelNode;
import recombination.IRecombinationStrategy;
import recombination.RecombinationException;
import recombination.crossoverpoint.inference.IInferenceStrategy;
import recombination.crossoverpoint.postprocessing.IAttributeProcessor;

/**
 * A recombination strategy based on the notion of a crossover point as discussed in the papers  
 * <a href="https://link.springer.com/chapter/10.1007/978-3-031-09843-7_6">A Generic Construction for Crossovers of 
 * Graph-Like Structures</a> and <a href="https://dl.acm.org/doi/abs/10.1145/3550356.3561603">Towards a configurable
 * crossover operator for model-driven optimization</a>. * 
 * In short, the crossover point specifies which model elements should be identified when two parts of the given 
 * parent models are recombined. 
 * <p>
 * An {@link IInferenceStrategy} needs to be provided specifying the desired crossover point.
 * A crossover point may contain {@link ModelNode}s for which the respective original nodes in the parents differ with
 * regard to their attribute values. Additionally, attribute values might depend on the underlying model (e.g., derived
 * attributes). For these cases an {@link IAttributeProcessor} needs to specify how the respective attribute values can
 * be calculated for the offspring of the recombination.
 * A Henshin engine needs to be provided as graph transformation rules are used to recombine model parts.  
 *   
 * @author S. John
 *
 */
public class CrossoverPointRecombination implements IRecombinationStrategy {

	private IInferenceStrategy crossoverPointStr;
	private IAttributeProcessor attributeProcessor;
	private Engine engine;

	// TODO: Sanity check on problem specification (opposite edges should both be problem or solution, problem edges
	// (together with their end point types) should build up a connected graph. Maybe better done in MDEO?

	public CrossoverPointRecombination(IInferenceStrategy strategy, IAttributeProcessor attributeProcessor,
			Engine engine) {
		this.crossoverPointStr = strategy;
		this.attributeProcessor = attributeProcessor;
		if (crossoverPointStr == null || attributeProcessor == null) {
			throw new IllegalArgumentException(
					"CrossoverPointStrategy and AttributePostProcessor need to be specified.");
		}
		this.engine = engine;
	}

	@Override
	public List<EObject> recombine(CoSpan firstSplit, CoSpan secondSplit) {
		Span crossoverPoint = crossoverPointStr.inferCrossoverPoint(firstSplit, secondSplit);

		// First part of the first split is recombined with the second part of the second split.
		ModelGraphMapping cpToFirstSplitFirstPart = createCompositionMapping(crossoverPoint.getDomainToFirstCodomain(),
				firstSplit.getPullbackToFirstDomain());
		EObject firstOffspring = createOffSpring(cpToFirstSplitFirstPart, secondSplit.getSecondDomain());

		// Second part of the first split is recombined with the first part of the second split.
		ModelGraphMapping cpToFirstSplitSecondPart = createCompositionMapping(crossoverPoint.getDomainToFirstCodomain(),
				firstSplit.getPullbackToSecondDomain());
		EObject secondOffspring = createOffSpring(cpToFirstSplitSecondPart, secondSplit.getFirstDomain());

		List<EObject> offspring = new ArrayList<>();
		offspring.add(firstOffspring);
		offspring.add(secondOffspring);
		return offspring;
	}

	/**
	 * Creates a mapping representing the composition of the second mapping applied after the first mapping.
	 * 
	 * @param firstMapping  a mapping between model graphs
	 * @param secondMapping a mapping between model graphs
	 * @return mapping from the elements of the origin of the first mapping to the image of the second mapping
	 */
	private ModelGraphMapping createCompositionMapping(ModelGraphMapping firstMapping,
			ModelGraphMapping secondMapping) {
		ModelGraph firstOrigin = firstMapping.getOriginGraph();
		ModelGraph secondImage = secondMapping.getImageGraph();
		ModelGraphMapping mapping = new ModelGraphMapping(firstOrigin, secondImage);
		for (ModelGraphElement element : firstOrigin.getElements()) {
			ModelGraphElement firstImageElement = firstMapping.getImage(element);
			ModelGraphElement secondOriginElement = secondMapping.getImage(firstImageElement);
			if (secondOriginElement == null) {
				throw new IllegalArgumentException("Mappings are not compatible.");
			}
			mapping.addMapping(element, secondOriginElement);
		}
		return mapping;
	}

	private EObject createOffSpring(ModelGraphMapping cpToFirstRecombinationPart, ModelGraph secondRecombinationPart) {
		MappingRuleAdapter henshinAdapter = new MappingRuleAdapter(engine, cpToFirstRecombinationPart);
		ModelGraphToEmfModelConverter converter = new ModelGraphToEmfModelConverter();
		EObject secondPartEmfModel = converter.createEmfModel(secondRecombinationPart);
		EObject resultModelRoot = henshinAdapter.applyRule(secondPartEmfModel, null);
		if (resultModelRoot == null) {
			throw new InvalidRecombinationRuleException("Rule performing recombination could not be applied to second recombination part.");
		}
		postProcessAttributes(henshinAdapter.getLastMatch(), henshinAdapter.getResultMatch(), henshinAdapter.getImageToRuleMap());		
		return resultModelRoot;
	}

	/**
	 * As nodes of a crossover point may have differing attribute values in the originating split parts post processing
	 * of attributes may be needed. The specified {@link IAttributeProcessor} will be applied to all 
	 * {@link EAttribute}s accepted by it. Attribute values are only adapted in the model of the second recombination
	 * part because it serves as the offspring model (the change rule is applied to it).
	 * 
	 * @param match                           mapping of rule nodes to nodes of the second recombination part
	 * @param firstRecombinationPartToRuleMap mapping of nodes of the first recombination part to rule nodes
	 * @throws RecombinationException if attributes differ although they should not
	 */
	private void postProcessAttributes(Match match, Match resultMatch,
			Map<ModelNode, Node> firstRecombinationPartToRuleMap) {
		
		for (ModelNode firstPartNode : firstRecombinationPartToRuleMap.keySet()) {
			Node ruleNode = firstRecombinationPartToRuleMap.get(firstPartNode);
			// TODO post processing may only be needed for solution parts, however, this generic implementation does not care
			// about parts. Anyways one would need to iterate over the cp to find the problem parts.
			if (ruleNode.getAction().getType().equals(Action.Type.PRESERVE)) {
				EObject secondParentObject = match.getNodeTarget(ruleNode);
				EObject firstParentObject = firstPartNode.getReferencedObject();	
				for (EAttribute attribute : firstParentObject.eClass().getEAllAttributes()) {									
					if (attributeProcessor.acceptsAttribute(attribute)) {
						Object value = attributeProcessor.processAttribute(firstParentObject, secondParentObject,
								attribute);
						// The second recombination part will be returned as offspring
						secondParentObject.eSet(attribute, value);
					} else {
						Object firstAttrValue = firstParentObject.eGet(attribute);
						Object secondAttrValue = secondParentObject.eGet(attribute);
						if ((firstAttrValue == null && secondAttrValue != null)
								|| firstAttrValue != null && !firstAttrValue.equals(secondAttrValue)) {
							throw new RecombinationException("Attribute\n" 
								  + attribute
									+ "\nof identified elements differs."
									+ "\nFirst parent:\n"
									+ firstParentObject
									+ "\nValue:\n"
									+ firstAttrValue
									+ "\nSecond parent:\n"
									+ secondParentObject
									+ "\nValue:\n"
									+ secondAttrValue
									+ "\n");
						}
					}
				}
			} else if (ruleNode.getAction().getType().equals(Action.Type.CREATE)) {
				EObject secondParentObject = resultMatch.getNodeTarget(ruleNode);
				EObject firstParentObject = firstPartNode.getReferencedObject();
				for (EAttribute attribute : firstParentObject.eClass().getEAllAttributes()) {
					Object firstParentValue = firstParentObject.eGet(attribute);
					secondParentObject.eSet(attribute, firstParentValue);
				}
			}
			
		}
	}
}
