package graphtools.emf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.model.Action;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.compact.CModule;
import org.eclipse.emf.henshin.model.compact.CNode;
import org.eclipse.emf.henshin.model.compact.CRule;

import model.ModelGraphMapping;
import model.modelgraph.MalformedGraphException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphElement;
import model.modelgraph.ModelNode;

public class MappingRuleAdapter extends HenshinRuleAdapter {
	
	private final Map<ModelNode, Node> originToRuleMap;
	private final Map<ModelNode, Node> imageToRuleMap;

	/**
	 * Creates an adapter wrapping a rule implementing the given <code>mapping</code>. Mapped elements become
	 * preserved-nodes in the rule. Unmapped elements in the image graph become created-nodes. Unmapped elements of the
	 * origin graph are ignored for now.
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
	
	private Rule createRule(ModelGraphMapping modelGraphMapping) throws MalformedGraphException {
		CModule cModule = new CModule("MRA");
		CRule cRule = cModule.createRule("mappingBased");
		ModelGraph imageGraph = modelGraphMapping.getImageGraph();
		for (ModelNode node : imageGraph.getNodes()) {
			CNode cNode = addModelNodeToRuleIfAbsent(node, cRule, modelGraphMapping);
			if (modelGraphMapping.getOrigin(node) != null) {
				originToRuleMap.put(node, cNode.getNode());
			}
			imageToRuleMap.put(node, cNode.getNode());
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
		Action action = createRuleAction(node, mapping);
		CNode cNode = addModelNodeToRuleIfAbsent(node, cRule, action);
		return cNode;
	}
	
	/**
	 * Elements which can be mapped to the crossover point need to be preserved by rule applications. Otherwise they
	 * need to be created.
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
