package de.uni_marburg.mdo_over.graphtools.emf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.model.Action;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.compact.CModule;
import org.eclipse.emf.henshin.model.compact.CNode;
import org.eclipse.emf.henshin.model.compact.CRule;

import de.uni_marburg.mdo_over.model.ModelGraphMapping;
import de.uni_marburg.mdo_over.model.modelgraph.MalformedGraphException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

public class MappingRuleAdapter extends HenshinRuleAdapter {

	private final Map<ModelNode, Node> originToRuleMap;
	private final Map<ModelNode, Node> imageToRuleMap;

	/**
	 * Creates an adapter wrapping a rule implementing the given <code>mapping</code>. Mapped elements are represented by
	 * preserved elements in the rule. Unmapped nodes and edges only appearing in the image graph are represented as 
	 * created elements. However, unmapped attributes {@link ModelGraphElement#getAttributes() attributes} of the image
	 * graph will not be considered if their containing element is mapped, i.e, an attribute may not be created in a 
	 * preserved element. Unmapped elements of the origin graph are completely ignored for now.
	
	 * <p>
	 * Note: The rule implementing the mapping is created during initialization. Later changes to the mapping are not
	 * reflected by the adapter.
	 * 
	 * @param engine  engine used to apply the wrapped rule
	 * @param mapping mapping to create the rule from
	 */
	public MappingRuleAdapter(Engine engine, ModelGraphMapping mapping) {
		super(engine);
		originToRuleMap = new HashMap<>();
		imageToRuleMap = new HashMap<>();
		rule = createRule(mapping);
	}

	/**
	 * Returns a map between the nodes of the origin graph of the {@link ModelGraphMapping} used to create the rule
	 * represented by this adapter and their counterpart {@link Node nodes} in the rule.
	 * 
	 * @return mapping of nodes of the origin graph of the used mapping to rule nodes
	 */
	public Map<ModelNode, Node> getOriginToRuleMap() {
		return originToRuleMap;
	}

	/**
	 * Returns a map between the nodes of the image graph of the {@link ModelGraphMapping} used to create the rule
	 * represented by this adapter and their counterpart {@link Node nodes} in the rule.
	 * 
	 * @return mapping of nodes of the image graph of the used mapping to rule nodes
	 */
	public Map<ModelNode, Node> getImageToRuleMap() {
		return imageToRuleMap;
	}

	//TODO: Needs to be checked. CP nodes may contain less attributes as their counterpart in a split point.
	private Rule createRule(ModelGraphMapping modelGraphMapping) throws MalformedGraphException {
		CModule cModule = new CModule("MRA");
		CRule cRule = cModule.createRule("mappingBased");
		ModelGraph imageGraph = modelGraphMapping.getImageGraph();
		for (ModelNode node : imageGraph.getNodes()) {
			addModelNodeToRuleIfAbsent(node, cRule, modelGraphMapping);			
		}
		for (ModelEdge edge : imageGraph.getEdges()) {
			addModelEdgeToRule(edge, cRule, modelGraphMapping);
		}
		return cRule.getUnit();
	}

	private void addModelEdgeToRule(ModelEdge edge, CRule cRule, ModelGraphMapping mapping)
			throws MalformedGraphException, IllegalArgumentException {
		EObject refObj = edge.getReferencedObject();
		EReference eReference = (EReference) refObj;
		CNode sourceCNode = addModelNodeToRuleIfAbsent(edge.getSource(), cRule, mapping);
		CNode targetCNode = addModelNodeToRuleIfAbsent(edge.getTarget(), cRule, mapping);
		Action edgeAction = createRuleAction(edge, mapping);
		sourceCNode.createEdge(targetCNode, eReference, edgeAction);
	}

	private CNode addModelNodeToRuleIfAbsent(ModelNode node, CRule cRule, ModelGraphMapping mapping) {
		ModelNode originNode = (ModelNode) mapping.getOrigin(node);
		Action action = createRuleAction(node, mapping);
		Set<EAttribute> ignoredAttributes = new HashSet<>();
		if (action.getType() == Action.Type.PRESERVE) {
			for (EAttribute attribute : node.getAttributes()) {
				if (!originNode.getAttributes().contains(attribute)) {
					ignoredAttributes.add(attribute);
				}
			}
		}
		CNode cNode = addModelNodeToRuleIfAbsent(node, ignoredAttributes, cRule, action);
		if (originNode != null) {
			originToRuleMap.put(originNode, cNode.getNode());
		}
		imageToRuleMap.put(node, cNode.getNode());
		return cNode;
	}

	/**
	 * Elements for which a mapping exists need to be represented as preserved nodes. Elements which only exist in the
	 * image graph are represented as created nodes.
	 * 
	 * @param element element which should be represented in a rule
	 * @param mapping mapping between crossover point and split part
	 * @return action which should be associated with the element representation in a rule
	 */
	private Action createRuleAction(ModelGraphElement element, ModelGraphMapping mapping) {
		Action action;
		if (mapping.getOrigin(element) != null) {
			action = new Action(Action.Type.PRESERVE);
		} else {
			action = new Action(Action.Type.CREATE);
		}
		return action;
	}
}
