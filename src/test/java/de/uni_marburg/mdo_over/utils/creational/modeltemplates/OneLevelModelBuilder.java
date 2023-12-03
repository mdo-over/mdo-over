package de.uni_marburg.mdo_over.utils.creational.modeltemplates;

import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;
import de.uni_marburg.mdo_over.utils.creational.TestModelBuilder;

/**
 * A builder initializing a model with nodes:
 * <ul>
 * <li>rootNode
 * <li>aNode
 * <li>bNode
 * </ul>
 * and edges:
 * <ul>
 * <li>rootToA
 * <li>rootToB
 * </ul>
 * 
 * @author Despro
 */
public class OneLevelModelBuilder extends TestModelBuilder {
	
	public ModelNode rootNode;
	public ModelNode aNode;
	public ModelNode bNode;
	public ModelEdge rootToA;
	public ModelEdge rootToB;
	
	OneLevelModelBuilder() {
	}

	public void initModel() {
		if (graph.getElements().isEmpty()) {
			try {
				rootNode = createRoot();
				aNode = createA();
				bNode = createB();
				rootToA = createRootToA(rootNode, aNode);
				rootToB = createRootToB(rootNode, bNode);
			} catch (GraphManipulationException e) {
				e.printStackTrace();
				throw new TestModelCreationException();
			}
		}
	}
}
