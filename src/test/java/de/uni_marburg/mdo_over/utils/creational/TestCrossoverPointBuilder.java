package de.uni_marburg.mdo_over.utils.creational;

import org.eclipse.emf.ecore.EAttribute;

import de.uni_marburg.mdo_over.graphtools.SubGraphConstructor;
import de.uni_marburg.mdo_over.model.CoSpan;
import de.uni_marburg.mdo_over.model.ModelGraphMapping;
import de.uni_marburg.mdo_over.model.Span;
import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;

public class TestCrossoverPointBuilder extends SubGraphBasedBuilder {

	Span span;
	CoSpan firstSplit;
	CoSpan secondSplit;
	SubGraphConstructor domainConstr;
	ModelGraphMapping domainToSecondCoDomain;
	ModelGraph domain;

	public TestCrossoverPointBuilder(CoSpan firstSplit, CoSpan secondSplit) {
		this.firstSplit = firstSplit;
		this.secondSplit = secondSplit;
		domainConstr = new SubGraphConstructor(firstSplit.getPullback());
		this.domain = domainConstr.getSubGraph();
		this.domainToSecondCoDomain = new ModelGraphMapping(domain, secondSplit.getPullback());
		span = new Span(domainConstr.getMapping(), domainToSecondCoDomain);
	}

	/**
	 * Extends the crossover point by a {@link ModelGraphElement} and appropriate mappings to connect both of the
	 * parameter elements by the mappings: crossover point -> split point -> split part -> original graph. The
	 * parameters are expected to be elements of the respective codomains of the {@link CoSpan cospans} used in this
	 * builder. Additionally, they need to be represented in the respective pullbacks of those cospans.
	 * 
	 * @param firstSplitElement an element of the codomain of the first split
	 * @param secondSplitElement an element of the codomain of the second split
	 * @return this builder
	 * @throws GraphManipulationException
	 */
	public TestCrossoverPointBuilder identify(ModelGraphElement firstSplitElement, ModelGraphElement secondSplitElement)
			throws GraphManipulationException {
		ModelGraphElement firstSplitElementInFirstPart = firstSplit.getFirstDomainToCodomain()
				.getOrigin(firstSplitElement);
		ModelGraphElement firstSplitElementInFirstSplitPoint = firstSplit.getPullbackToFirstDomain()
				.getOrigin(firstSplitElementInFirstPart);
		ModelGraphElement domainCopy = addElement(domainConstr, firstSplitElementInFirstSplitPoint);
		
		ModelGraphElement secondSplitElementInFirstPart = secondSplit.getFirstDomainToCodomain()
				.getOrigin(secondSplitElement);
		ModelGraphElement secondSplitElementInFirstSplitPoint = secondSplit.getPullbackToFirstDomain()
				.getOrigin(secondSplitElementInFirstPart);
		domainToSecondCoDomain.addMapping(domainCopy, secondSplitElementInFirstSplitPoint);
		return this;
	}
	
	/**
	 * Removes the given attribute from the element of the crossover point mapped to the given element.
	 * The given element must have been {@link #identify(ModelGraphElement, ModelGraphElement) identified} before.
	 * @param element an element of the codomain of one of the splits
	 * @param attribute an attribute of the given element
	 * @return
	 */
	public TestCrossoverPointBuilder unidentifyAttribute(ModelGraphElement element, EAttribute attribute) {
		ModelGraphElement crossoverPointElement = getDomainElementMappedToSplitElement(firstSplit, element);
		if (crossoverPointElement == null) {
			getDomainElementMappedToSplitElement(secondSplit, element);
		}
		if (crossoverPointElement == null) {
			throw new IllegalArgumentException("The given element is not contained in the crossover point.");
		}
		crossoverPointElement.removeAttribute(attribute);
		return this;
	}
	
	private ModelGraphElement getDomainElementMappedToSplitElement(CoSpan split, ModelGraphElement splitElement) {
		ModelGraphElement splitPartElement = split.getFirstDomainToCodomain().getOrigin(splitElement);
		if (splitPartElement == null) {
			throw new IllegalArgumentException("The given element is not contained in both split parts.");
		}
		ModelGraphElement splitPointElement = split.getPullbackToFirstDomain().getOrigin(splitPartElement);
		if (splitPointElement == null) {
			// This should not happen if the pullback calculation is correct.
			throw new IllegalArgumentException("The given element is contained in both split parts but not in the "
					+ "split point.");
		}
		return domainConstr.getMapping().getOrigin(splitPointElement);		
	}

	public Span getCrossoverPoint() {
		return span;
	}
}
