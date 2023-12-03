package de.uni_marburg.mdo_over.testinterfaces;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.util.EGraphIsomorphyChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import de.uni_marburg.mdo_over.graphtools.DotSerializer;
import de.uni_marburg.mdo_over.graphtools.emf.ModelGraphToEmfModelConverter;
import de.uni_marburg.mdo_over.model.modelgraph.ModelEdge;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;
import de.uni_marburg.mdo_over.model.modelgraph.ModelNode;

public abstract class GraphTest {

	protected static final String DOT_DIR = "src/test/resources/dot_output/";

	protected TestInfo testInfo;
	protected DotSerializer dotSerializer;
	protected ModelGraphToEmfModelConverter converter;

	@BeforeEach
	public void setup(TestInfo testInfo) {
		this.testInfo = testInfo;
		this.dotSerializer = new DotSerializer(
				DOT_DIR + testInfo.getTestClass().get().getSimpleName() + File.separator);
		this.converter = new ModelGraphToEmfModelConverter();
	}

	/**
	 * From within a test method save a graph in a DOT file. Files are organized by placing them in {@link #DOT_DIR the
	 * dot output directory} where subfolders with the name of the respective test class and test method are created.
	 * <p>
	 * Setting <code>resetIds</code> to false the IDs used for labeling the graph elements in the DOT format are kept
	 * for the next call to {@link GraphTest#saveGraph(ModelGraph, String) saveGraph}. In that case, the DOT
	 * representations of the given and the next saved graph will share the same labels for graph elements
	 * <b>referencing the same objects</b>. Otherwise the IDs are cleared and the labels of next graph are calculated
	 * independently.
	 * 
	 * @param graph    a graph to save as DOT file
	 * @param filename name of the file
	 * @param resetIds whether to reset the IDs used for labeling graph elements or not
	 */
	protected void saveGraph(ModelGraph graph, String filename, boolean resetIds) {
		try {
			dotSerializer.saveAsDot(graph, testInfo.getTestMethod().get().getName() + File.separator + filename,
					resetIds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save the graph in a DOT file and afterwards clear the IDs used for labeling the graph elements in the DOT format.
	 * The next call to {@link #saveGraph(ModelGraph, String, boolean) saveGraph} will calculate new IDs regardless of
	 * whether or not the referenced objects of the next graph overlap with the objects of the given graph.
	 * <p>
	 * Equivalent to a call to {@link #saveGraph(ModelGraph, String, boolean) saveGraph(ModelGraph, String, true)}.
	 * 
	 * @param graph    a graph to save as DOT file
	 * @param filename name of the file
	 */
	protected void saveGraph(ModelGraph graph, String filename) {
		saveGraph(graph, filename, true);
	}

	/**
	 * Checks isomorphy of given model graphs. The graphs are considered isomorphic if their underlying EMF models are
	 * isomorphic.
	 * 
	 * @param first  first graph to check
	 * @param second second graph to check
	 * @return true if the EMF models represented by both graphs are isomorphic
	 */
	protected boolean areIsomorphic(ModelGraph first, ModelGraph second) {
		EObject firstEmfModel = converter.createEmfModel(first);
		EObject secondEmfModel = converter.createEmfModel(second);
		return areIsomorphic(firstEmfModel, secondEmfModel);
	}
	
	/**
	 * Checks isomorphy of given EMF models.
	 * 
	 * @param first  first model to check
	 * @param second second model to check
	 * @return true if the EMF models are isomorphic
	 */
	protected boolean areIsomorphic(EObject first, EObject second) {
		EGraph firstEGraph = new EGraphImpl(first);
		EGraph secondEGraph = new EGraphImpl(second);
		EGraphIsomorphyChecker checker = new EGraphIsomorphyChecker(firstEGraph, null);
		return checker.isIsomorphicTo(secondEGraph, null);
	}

	/**
	 * Returns how often the given element is represented in the given graph. I.e., for nodes, how many nodes in the
	 * graph reference the same object as the given node and for edges how many edges reference the same object as the
	 * given edge where the objects referenced by their source and target correspond to the objects referenced by the
	 * source and target of the given edge.
	 * 
	 * @param graph   graph which is checked for a representations of the given element
	 * @param element for which number of representations is calculated
	 * @return number of representations of the given element
	 */
	protected int numberOfElementRepresentionsInGraph(ModelGraph graph, ModelGraphElement element) {
		int representationCount = 0;
		if (element instanceof ModelNode) {
			EObject refObject = element.getReferencedObject();
			representationCount = graph.getNodes(refObject).size();
		} else {
			ModelEdge edge = (ModelEdge) element;
			Iterator<ModelEdge> itPossibles = graph.getEdges(edge.getReferencedObject()).iterator();
			while (itPossibles.hasNext()) {
				ModelEdge possibleEdge = itPossibles.next();
				if (possibleEdge.getSource().getReferencedObject() == edge.getSource().getReferencedObject()
						&& possibleEdge.getTarget().getReferencedObject() == edge.getTarget().getReferencedObject()) {
					representationCount++;
				}
			}
		}
		return representationCount;
	}

	/**
	 * Checks if all of the given elements are represented exactly once in the given graph by applying
	 * {@link #isElementRepresentedOnceInGraph(ModelGraph, ModelGraphElement)} for each element.
	 * 
	 * @param graph    graph which is checked for a representation of the given edge
	 * @param elements elements which are checked w.r.t. whether they are represented in the given graph
	 * @return true if all of the given elements are represented by exactly one corresponding element in the given graph
	 *         - false otherwise
	 */
	protected RepresentationResult areElementsRepresentedOnceInGraph(ModelGraph graph, ModelGraphElement... elements) {
		RepresentationResult result;
		boolean representationFound = false;
		boolean representsAllElements = true;
		for (ModelGraphElement element : elements) {
			int representations = numberOfElementRepresentionsInGraph(graph, element);
			if (representations == 1) {
				representationFound = true;
			} else if (representations == 0) {
				representsAllElements = false;
			} else {
				return RepresentationResult.DUPLICATES;
			}
		}
		if (!representationFound) {
			result = RepresentationResult.NONE;
		} else if (representsAllElements) {
			result = RepresentationResult.ALL;
		} else {
			result = RepresentationResult.SOME;
		}
		return result;
	}

	protected enum RepresentationResult {
		NONE, SOME, ALL, DUPLICATES
	}
}
