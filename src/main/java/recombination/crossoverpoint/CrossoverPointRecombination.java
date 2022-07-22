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
		postProcessAttributes(cpToFirstRecombinationPart, henshinAdapter.getLastMatch(), henshinAdapter.getResultMatch(), henshinAdapter.getImageToRuleMap());		
		return resultModelRoot;
	}

	/**
	 * As nodes of a crossover point may have differing attribute values in the originating split parts post processing
	 * of attributes may be needed.
	 * 
	 * @param match                           mapping of rule nodes to nodes of the second recombination part
	 * @param firstRecombinationPartToRuleMap mapping of nodes of the first recombination part to rule nodes
	 * @throws RecombinationException if attributes differ although they should not
	 */
	private void postProcessAttributes(ModelGraphMapping cpToFirstRecombinationPart, Match match, Match resultMatch,
			Map<ModelNode, Node> firstRecombinationPartToRuleMap) {
		
		for (ModelNode firstPartNode : firstRecombinationPartToRuleMap.keySet()) {
			Node ruleNode = firstRecombinationPartToRuleMap.get(firstPartNode);
			// TODO post processing may only be needed for problem parts, however, this generic implementation does not care
			// about problem parts. Anyways one would need to iterate over the cp to find the problem parts.
			if (ruleNode.getAction().getType().equals(Action.Type.PRESERVE)) {
				EObject secondParentObject = match.getNodeTarget(ruleNode);
				EObject firstParentObject = firstPartNode.getReferencedObject();	
				for (EAttribute attribute : firstParentObject.eClass().getEAllAttributes()) {									
					if (attributeProcessor.acceptsAttribute(attribute)) {
						Object value = attributeProcessor.processAttribute(firstParentObject, secondParentObject,
								attribute);
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
