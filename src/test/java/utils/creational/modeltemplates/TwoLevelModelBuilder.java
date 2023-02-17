package utils.creational.modeltemplates;

import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelNode;
import utils.creational.TestModelBuilder;
import utils.creational.TestModelBuilder.TestModelCreationException;

/**
 * A builder initializing a model with nodes:
 * <ul>
 * <li>rootNode
 * <li>aNode
 * <li>bNode
 * <li>x1Node
 * <li>x2Node
 * </ul>
 * and edges:
 * <ul>
 * <li>rootToA
 * <li>rootToB
 * <li>aToX1
 * <li>aToX2
 * </ul>
 * 
 * @author Despro
 */
public class TwoLevelModelBuilder extends OneLevelModelBuilder {

	public ModelNode x1Node;
	public ModelNode x2Node;
	public ModelEdge aToX1;
	public ModelEdge aToX2;
	
	TwoLevelModelBuilder() {
	}
	
	public void initModel() {
		if (graph.getElements().isEmpty()) {
			try {
				super.initModel();
				x1Node = createX();
				x2Node = createX();
				aToX1 = createAToX(aNode, x1Node);
				aToX2 = createAToX(aNode, x2Node);
			} catch (GraphManipulationException e) {
				e.printStackTrace();
				throw new TestModelCreationException();
			}
		}
	}

}
