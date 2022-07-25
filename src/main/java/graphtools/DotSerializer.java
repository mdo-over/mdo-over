package graphtools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;

import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelGraphElement;
import model.modelgraph.ModelNode;

/**
 * Allows to generate a representation of a {@link ModelGraph} in the DOT language.
 * 
 * @see <a href="https://graphviz.org/doc/info/lang.html">DOT language</a>
 * @author S. John
 *
 */
public class DotSerializer {

	private String basepath;
	private int elementEnumerator = 0;
	private Map<EObject, String> elementIdsByReference;
	private Map<ModelGraphElement, String> elementIds;

	public DotSerializer(String basepath) {
		this.basepath = basepath;
		this.elementIdsByReference = new HashMap<>();
		this.elementIds = new HashMap<>();
	}

	/**
	 * /** Save the graph in a DOT file. Setting <code>resetIds</code> to false the IDs used for labeling the graph
	 * elements in the DOT format are kept for the next call to {@link #saveAsDot(ModelGraph, String, boolean)
	 * saveAsDot(ModelGraph, String, boolean)}. In that case, the DOT representations of the given and the next saved
	 * graph will share the same labels for graph elements <b>referencing the same objects</b>. Otherwise the IDs are
	 * cleared and the labels of next graph are calculated independently.
	 *
	 * @param graph    a graph to save as DOT file
	 * @param filename name of the file
	 * @param resetIds whether to reset the IDs used for labeling graph elements or not
	 * @throws IOException thrown if the file or any directories specified in <code>filename</code> could not be
	 *                     accessed
	 */
	public void saveAsDot(ModelGraph graph, String filename, boolean resetIds) throws IOException {
		Path filepath = Paths.get(basepath + filename + ".dot");
		Path parent = filepath.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}

		try (BufferedWriter bw = Files.newBufferedWriter(filepath)) {
			bw.write("digraph g {\n");
			bw.write(toString(graph));
			bw.write("}");
		} catch (IOException e) {
			elementEnumerator = 0;
			elementIdsByReference.clear();
		}

		if (resetIds) {
			elementEnumerator = 0;
			elementIdsByReference.clear();
		}

	}

	/**
	 * Save the graph in a DOT file and afterwards clear the IDs used for labeling the graph elements in the DOT format.
	 * The next call to {@link #saveAsDot(ModelGraph, String, boolean)} will calculate new IDs regardless of whether or
	 * not the referenced objects of the next graph overlap with the objects of the given graph.
	 * <p>
	 * Equivalent to a call to {@link #saveAsDot(ModelGraph, String, boolean) saveAsDot(ModelGraph, String, true)}.
	 * 
	 * @param graph    a graph to save as DOT file
	 * @param filename name of the file
	 * @throws IOException thrown if the file or any directories specified in <code>filename</code> could not be
   *                     accessed
	 */
	public void saveAsDot(ModelGraph graph, String filename) throws IOException {
		saveAsDot(graph, filename, true);
	}

	private String toString(ModelGraph graph) {
		StringBuilder builder = new StringBuilder();
		for (ModelNode node : graph.getNodes()) {
			processNode(node, builder);
		}
		return builder.toString();
	}
	
	// TODO: Attributes of ModelGraphElements not considered yet.
	
	private String toString(ModelNode node, boolean withLabel) {
		StringBuilder builder = new StringBuilder();
		builder.append("\"");
		builder.append(getId(node));
		builder.append("\"");
		if (withLabel) {
			builder.append(getLabel(node));
		}
		return builder.toString();
	}

	private String toString(ModelEdge edge) {
		StringBuilder builder = new StringBuilder();
		builder.append(toString(edge.getSource(), false));
		builder.append(" -> ");
		builder.append(toString(edge.getTarget(), false));
		builder.append(getLabel(edge));
		return builder.toString();
	}

	private void processNode(ModelNode node, StringBuilder builder) {
		builder.append(toString(node, true));
		builder.append(";");
		for (ModelEdge edge : node.getOutgoingEdges()) {
			builder.append("\n");
			builder.append(toString(edge));
			builder.append(";");
		}
		builder.append("\n");
	}

	private String getId(ModelGraphElement element) {
		String id;
		if (element.getReferencedObject() != null) {
			id = elementIdsByReference.computeIfAbsent(element.getReferencedObject(), obj -> {
				return String.valueOf(++elementEnumerator) + ":" + getReferenceName(element);
			});
		} else {
			id = elementIds.computeIfAbsent(element, e -> {
				return String.valueOf(++elementEnumerator);
			});
		}
		return id;
	}

	private String getReferenceName(ModelGraphElement element) {
		EObject referenced = element.getReferencedObject();
		String name;
		if (referenced instanceof ENamedElement) {
			name = ((ENamedElement) referenced).getName();
		} else {
			name = referenced.eClass().getName();
		}
		return name;
	}

	private String getLabel(ModelGraphElement element) {
		StringBuilder builder = new StringBuilder();
		builder.append("[label=<");
		builder.append(getId(element));
		if (element.getName() != null && !element.getName().isBlank()) {
			builder.append("<BR />");
			builder.append("<FONT POINT-SIZE=\"10\">");
			builder.append(element.getName());
			builder.append("</FONT>");
		}
		builder.append(">]");
		return builder.toString();
	}
}
