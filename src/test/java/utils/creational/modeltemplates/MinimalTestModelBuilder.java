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
 * </ul>
 * and edges:
 * <ul>
 * <li>rootToA
 * </ul>
 * 
 * @author Despro
 */
public class MinimalTestModelBuilder extends TestModelBuilder {
	
	public ModelNode rootNode;
	public ModelNode aNode;
	public ModelEdge rootToA;

	MinimalTestModelBuilder() {		
	}
	
	public void initModel() {
		if (graph.getElements().isEmpty()) {
			try {
				rootNode = createRoot();
				aNode = createA();
				rootToA = createRootToA(rootNode, aNode);
			} catch (GraphManipulationException e) {
				e.printStackTrace();
				throw new TestModelCreationException();
			}
		}
	}
}
