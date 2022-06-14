package utils.creational;

import graphtools.SubGraphConstructor;
import model.CoSpan;
import model.ModelGraphMapping;
import model.Span;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphElement;

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

	public Span getCrossoverPoint() {
		return span;
	}
}
