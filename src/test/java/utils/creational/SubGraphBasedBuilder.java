package utils.creational;

import graphtools.SubGraphConstructor;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraphElement;
import model.modelgraph.ModelNode;

public class SubGraphBasedBuilder {
	
	protected ModelGraphElement addElement(SubGraphConstructor domainConstructor, ModelGraphElement element)
			throws GraphManipulationException {
		ModelGraphElement copy;
		if (element instanceof ModelNode) {
			copy = domainConstructor.addNodeCopy((ModelNode) element);
		} else if (element instanceof ModelEdge) {
			copy = domainConstructor.addEdgeCopy((ModelEdge) element);
		} else {
			throw new GraphManipulationException("Unknown element type");
		}
		ModelNode codomainRoot = domainConstructor.getCompleteGraph().getRoot();
		ModelGraphElement rootCopy = domainConstructor.getMapping().getOrigin(codomainRoot);
		if (rootCopy != null) {
			domainConstructor.getSubGraph().setRoot((ModelNode)rootCopy);
		}
		return copy;
	}
}
