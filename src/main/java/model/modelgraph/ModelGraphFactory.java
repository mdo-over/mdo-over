package model.modelgraph;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class ModelGraphFactory {
	private static ModelGraphFactory instance;

	private ModelGraphFactory() {
	}

	public static ModelGraphFactory getInstance() {
		if (instance == null) {
			instance = new ModelGraphFactory();
		}
		return instance;
	}

	/**
	 * Considering the given EObject the root of an EMF model, creates a {@link ModelGraph} representing that model. 
	 * @param model root of an EMF model
	 * @return a {@link ModelGraph} representing the EMF model
	 */
	public ModelGraph createModelGraph(EObject model) {
		ModelGraph graph = new ModelGraph();
		ModelNode rootNode = addObjectWithReferences(graph, model);
		try {
			graph.setRoot(rootNode);
		} catch (GraphManipulationException e) {
			throw new IllegalStateException("Construction should have added a non-null rootNode to the graph.");
		}
		Iterator<EObject> it = model.eAllContents();
		while (it.hasNext()) {
			EObject object = it.next();
			addObjectWithReferences(graph, object);
		}
		return graph;
	}

	private ModelNode addObjectWithReferences(ModelGraph graph, EObject object) {
		ModelNode node = createNodeIfAbsent(graph, object);

		for (EReference ref : object.eClass().getEAllReferences()) {
			Object value = object.eGet(ref);
			if (value instanceof EObject) {
				createAndAddEdge(graph, node, ref, (EObject) value);

			} else if (value != null) {
				@SuppressWarnings("unchecked")
				EList<EObject> valueList = (EList<EObject>) value;
				for (EObject listValue : valueList) {
					createAndAddEdge(graph, node, ref, (EObject) listValue);
				}
			}
		}		
		return node;
	}

	private ModelNode createNodeIfAbsent(ModelGraph graph, EObject object) {
		Set<ModelNode> refNodes = graph.getNodes(object);
		ModelNode node;
		if (refNodes.isEmpty()) {
			node = new ModelNode();
			node.setReferencedObject(object);
		} else if (refNodes.size() == 1) {
			node = refNodes.iterator().next();
		} else {
			throw new IllegalStateException(
					"Construction of the graph should not produce multiple node references to the same object.");
		}		
		try {
			graph.addNode(node);
		} catch (GraphManipulationException e) {
			throw new IllegalStateException(
					"Construction of the graph should not add nodes with referenced edges.");
		}		
		return node;
	}
	
	private ModelEdge createAndAddEdge(ModelGraph graph, ModelNode sourceNode, EReference reference,
			EObject targetObject) {
		ModelNode targetNode = createNodeIfAbsent(graph, targetObject);
		ModelEdge edge = new ModelEdge(sourceNode, targetNode);
		edge.setReferencedObject(reference);
		try {
			graph.addEdge(edge);
		} catch (GraphManipulationException e) {
			e.printStackTrace();
			throw new IllegalStateException(
					"Construction of the graph should add source and target nodes before adding an edge.");
		}
		return edge;
	}
}
