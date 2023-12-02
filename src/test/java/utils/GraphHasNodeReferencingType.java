package utils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

public class GraphHasNodeReferencingType extends TypeSafeMatcher<ModelGraph> {
	
	private Class<?> clazz;
	public GraphHasNodeReferencingType(Class<?>  clazz) {
		this.clazz = clazz;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("graph contains a node referencing object of type " + clazz.getName());
	}

	@Override
	protected boolean matchesSafely(ModelGraph graph) {
		for (ModelNode node : graph.getNodes()) {
			if (clazz.isInstance(node.getReferencedObject())) {
				return true;
			}					
		}
		return false;
	}
	
	public static Matcher<ModelGraph> containsNodeOfType(Class<?> clazz) {
		return new GraphHasNodeReferencingType(clazz);		
	}

}
