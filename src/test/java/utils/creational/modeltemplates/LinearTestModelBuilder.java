package utils.creational.modeltemplates;

import de.uni_marburg.mdo_over.model.modelgraph.GraphManipulationException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;
import utils.creational.TestModelBuilder;
import utils.creational.TestModelBuilder.TestModelCreationException;

/**
 * A builder initializing a model with nodes:
 * <ul>
 * <li>rootNode
 * <li>aNode
 * <li>xNode
 * </ul>
 * and edges:
 * <ul>
 * <li>rootToA
 * <li>aToX
 * </ul>
 * 
 * @author Despro
 */
public class LinearTestModelBuilder extends MinimalTestModelBuilder {
	
	public ModelNode xNode;
	public ModelEdge aToX;

	LinearTestModelBuilder() {		
	}
	
	public void initModel() {
		if (graph.getElements().isEmpty()) {
			try {
				super.initModel();
				xNode = createX();
				aToX = createAToX(aNode, xNode);
			} catch (GraphManipulationException e) {
				e.printStackTrace();
				throw new TestModelCreationException();
			}
		}
	}
}
