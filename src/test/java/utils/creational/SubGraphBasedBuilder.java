package utils.creational;

import de.uni_marburg.mdo_over.graphtools.SubGraphConstructor;
import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

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
		// Set the root of the subgraph if root has been added
		ModelNode codomainRoot = domainConstructor.getCompleteGraph().getRoot();
		ModelGraphElement rootCopy = domainConstructor.getMapping().getOrigin(codomainRoot);
		if (rootCopy != null) {
			domainConstructor.getSubGraph().setRoot((ModelNode)rootCopy);
		}
		return copy;
	}
}
