package graphtools.emf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.Change;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.MatchImpl;
import org.eclipse.emf.henshin.model.Action;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.compact.CNode;
import org.eclipse.emf.henshin.model.compact.CRule;

import model.ModelGraphMapping;
import model.modelgraph.MalformedGraphException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelNode;

/**
 * Adapter between {@link ModelGraph model graphs}, {@link ModelGraphMapping model graph mappings} and {@link Rule
 * Henshin rules}. Can be used to create and apply rules based on model graphs and their mappings.
 * <p>
 * Note: The rule reflecting a mapping is created during initialization. Later changes to the underlying model graph or
 * model graph mapping are not reflected.
 */
public abstract class HenshinRuleAdapter {
	private final Map<ModelNode, CNode> modelToRuleMap;
	private final Engine engine;
	protected Rule rule;
	protected Match match;
	protected Match resultMatch;

	protected HenshinRuleAdapter(Engine engine) {
		modelToRuleMap = new HashMap<>();
		this.engine = engine;
		engine.getOptions().put(Engine.OPTION_DETERMINISTIC, false);
	}

	public Rule getRule() {
		return rule;
	}

	/**
	 * Applies the rule represented by this adapter to the given <code>emfModel</code>. A partial match can be specified
	 * to enforce rule nodes to be mapped to specific objects of the model. Returns the root of the resulting EMF model
	 * or <code>null</code> if the rule could not be applied.
	 * 
	 * @param emfModel     model to apply the rule to
	 * @param partialMatch a (partial) mapping of rule nodes to objects of the given model
	 * @return root of the resulting EMF model or <code>null</code> if the rule could not be applied
	 */
	public EObject applyRule(EObject emfModel, Match partialMatch) {
		match = null;
		resultMatch = null;
		EObject resultModel = null;
		EGraph graph = new EGraphImpl(emfModel);
		Iterator<Match> it = engine.findMatches(rule, graph, partialMatch).iterator();
		if (it.hasNext()) {
			match = it.next();
			resultMatch = new MatchImpl(rule, true); 
			Change change = engine.createChange(rule, graph, match, resultMatch);
			change.applyAndReverse();
			List<EObject> roots = graph.getRoots();
			if (roots.size() != 1) {
				throw new IllegalStateException("Multiple model roots after rule application.");
			}
			resultModel = roots.get(0);
		}
		return resultModel;
	}
	
	/**
	 * Returns a {@link Match} in the given <code>emfModel</code> for the rule represented by this adapter. Returns
	 * <code>null</code> if no such match exists.
	 * @param emfModel model to find a match in
	 * 
	 * @return a match for the rule or <code>null</code> if no match exists
	 */
	public Match findMatch(EObject emfModel) {
		match = null;
		EGraph graph = new EGraphImpl(emfModel);
		Iterator<Match> it = engine.findMatches(rule, graph, null).iterator();
		if (it.hasNext()) {
			match = it.next();
		}
		return match;
	}

	/**
	 * Returns the last {@link Match} created by a call to {@link #findMatch(EObject)} or {@link #applyRule(EObject, Match)}.
	 * 
	 * @return the last match for the rule or <code>null</code> if no last match exists
	 */
	public Match getLastMatch() {
		return match;
	}
	
	/**
	 * Returns the result match of the last {@link #applyRule(EObject, Match) application} of the rule of this adapter.
	 * @return the result match of the last rule application or null if the last rule application was not successful
	 */
	public Match getResultMatch() {
		return resultMatch;
	}

	protected void addModelEdgeToRule(ModelEdge edge, CRule cRule, Action action)
			throws MalformedGraphException, IllegalArgumentException {
		EObject refObj = edge.getReferencedObject();
		EReference eReference = (EReference) refObj;
		CNode sourceCNode = addModelNodeToRuleIfAbsent(edge.getSource(), cRule, action);
		CNode targetCNode = addModelNodeToRuleIfAbsent(edge.getTarget(), cRule, action);
		sourceCNode.createEdge(targetCNode, eReference, action);
	}
	
	protected CNode addModelNodeToRuleIfAbsent(ModelNode node, CRule cRule, Action action) {
		CNode cNode = modelToRuleMap.get(node);
		if (cNode == null) {
			cNode = cRule.createNode(node.getReferencedObject().eClass(), action);
			modelToRuleMap.put(node, cNode);
		}
		return cNode;
	}
}
