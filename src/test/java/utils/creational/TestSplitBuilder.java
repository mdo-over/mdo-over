package utils.creational;

import graphtools.SubGraphConstructor;
import model.CoSpan;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphElement;

public class TestSplitBuilder extends SubGraphBasedBuilder {

	CoSpan coSpan;
	SubGraphConstructor firstDomainConstr;
	SubGraphConstructor secondDomainConstr;
	ModelGraph codomain;

	public TestSplitBuilder(ModelGraph codomain) {
		firstDomainConstr = new SubGraphConstructor(codomain);
		secondDomainConstr = new SubGraphConstructor(codomain);
		this.codomain = codomain;
		coSpan = new CoSpan(firstDomainConstr.getMapping(), secondDomainConstr.getMapping());
	}

	public TestSplitBuilder extendFirstDomainBy(ModelGraphElement... elements) throws GraphManipulationException {
		for (ModelGraphElement e : elements) {
			addElement(firstDomainConstr, e);
		}
		return this;
	}

	public TestSplitBuilder extendSecondDomainBy(ModelGraphElement... elements) throws GraphManipulationException {
		for (ModelGraphElement e : elements) {
			addElement(secondDomainConstr, e);
		}
		return this;
	}

	public TestSplitBuilder extendBothDomainsBy(ModelGraphElement... elements) throws GraphManipulationException {
		for (ModelGraphElement e : elements) {
			addElement(firstDomainConstr, e);
			addElement(secondDomainConstr, e);
		}
		return this;
	}

	public CoSpan getSplit() {
		return coSpan;
	}
}
