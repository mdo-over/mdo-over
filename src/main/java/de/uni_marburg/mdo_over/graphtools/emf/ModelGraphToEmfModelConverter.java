package de.uni_marburg.mdo_over.graphtools.emf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

public class ModelGraphToEmfModelConverter {

	private final Map<EObject, ModelNode> emfObjectToOriginNodeMap = new HashMap<>();

	/**
	 * Creates a new EMF model from the given {@link ModelGraph graph}. The resulting model is made up of copies of the
	 * objects referenced by the graph nodes. The copy of the object referenced by the root node of the graph will be
	 * returned as the root of the new EMF model.
	 * 
	 * @param graph model graph to create an EMF model from
	 * 
	 * @return the root object of the created model
	 */
	public EObject createEmfModel(ModelGraph graph) {
		emfObjectToOriginNodeMap.clear();
		if (graph.getRoot() == null) {
			throw new IllegalArgumentException("The input graph needs to specify a root.");
		}

		PartialModelCopier copier = new PartialModelCopier();
		for (ModelNode node : graph.getNodes()) {
			EObject refObj = node.getReferencedObject();
			EObject copy = copier.copy(refObj);
			/*
			 * TODO: After adding attributes to the ModelGraphElements this has not yet been updated. Not all attributes of a
			 * referenced object should be included in the generated EMF model but only those which are explicitly mentioned
			 * in the attributes of the respective ModelGraphElement.
			 */
			emfObjectToOriginNodeMap.put(copy, node);
		}

		EObject rootRef = graph.getRoot().getReferencedObject();
		Set<EObject> uncontainedObjects = new HashSet<>(copier.keySet());
		uncontainedObjects.remove(rootRef);
		for (ModelEdge edge : graph.getEdges()) {
			EObject sourceRefCopy = copier.get(edge.getSource().getReferencedObject());
			EObject targetRef = edge.getTarget().getReferencedObject();
			EObject targetRefCopy = copier.get(targetRef);
			EReference edgeRef = (EReference) edge.getReferencedObject();
			if (edgeRef.isContainment()) {
				uncontainedObjects.remove(targetRef);
			}
			if (edgeRef.isMany()) {
				@SuppressWarnings("unchecked")
				EList<EObject> valueList = (EList<EObject>) sourceRefCopy.eGet(edgeRef);
				valueList.add(targetRefCopy);
			} else {
				sourceRefCopy.eSet(edgeRef, targetRefCopy);
			}
		}
		if (!uncontainedObjects.isEmpty()) {
			throw new IllegalArgumentException("The graph contains " + uncontainedObjects.size()
					+ " nodes (except the root) missing incoming edges representing containment.");
		}

		return copier.get(rootRef);
	}

	/**
	 * For an object created by the last call to {@link #createEmfModel(ModelGraph)} this method returns the
	 * {@link ModelNode} which served as the origin for creating the node.
	 * 
	 * @param object an object created by the last call to {@link #createEmfModel(ModelGraph)}
	 * @return the model node which served as the origin to create the given object or null if this object has not been
	 *         created during the last EMF model creation
	 */
	public ModelNode getOrigin(EObject object) {
		return emfObjectToOriginNodeMap.get(object);
	}
}
