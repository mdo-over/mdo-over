package de.uni_marburg.mdo_over.graphtools.emf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.model.Action;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.compact.CModule;
import org.eclipse.emf.henshin.model.compact.CNode;
import org.eclipse.emf.henshin.model.compact.CRule;

import de.uni_marburg.mdo_over.model.modelgraph.MalformedGraphException;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

public class GraphRuleAdapter extends HenshinRuleAdapter {

	private final Map<ModelNode, Node> graphToRuleMap;

	/**
	 * Creates an adapter wrapping a rule implementing the elements of the given <code>graph</code> as preserved
	 * elements.
	 * <p>
	 * Note: The rule implementing the graph is created during initialization. Later changes to the graph are not
	 * reflected by the adapter.
	 * 
	 * @param engine engine used to apply the wrapped rule
	 * @param graph  graph to create the rule from
	 */
	public GraphRuleAdapter(Engine engine, ModelGraph graph) {
		super(engine);
		graphToRuleMap = new HashMap<>();
		rule = createRule(graph);
	}

	/**
	 * Returns a map between the nodes of the graph used to create the rule represented by this adapter and their
	 * counterpart {@link Node nodes} in the rule.
	 * 
	 * @return mapping of nodes of the graph to rule nodes
	 */
	public Map<ModelNode, Node> getGraphToRuleMap() {
		return graphToRuleMap;
	}

	private Rule createRule(ModelGraph graph) throws MalformedGraphException {
		CModule cModule = new CModule("GRA");
		CRule cRule = cModule.createRule("graphBased");
		Action action = new Action(Action.Type.PRESERVE);
		for (ModelNode node : graph.getNodes()) {
			CNode cNode = addModelNodeToRuleIfAbsent(node, Collections.emptySet(), cRule, action);
			graphToRuleMap.put(node, cNode.getNode());
		}
		for (ModelEdge edge : graph.getEdges()) {
			addModelEdgeToRule(edge, cRule, action);
		}
		return cRule.getUnit();
	}
}
