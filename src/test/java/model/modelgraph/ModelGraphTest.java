package model.modelgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModelGraphTest {
	
	ModelGraph graph;
	
	@BeforeEach
	void setup() {
		graph = new ModelGraph();
	}

	@Test
	@DisplayName("Edge with reference successfully added.")
	void testAddNewEdge() throws GraphManipulationException {
		ModelNode source = new ModelNode();
		ModelNode target = new ModelNode();
		graph.addNode(source);
		graph.addNode(target);	
		EClass referencedObject = EcoreFactory.eINSTANCE.createEClass();
		ModelEdge edge = new ModelEdge(source, target);
		edge.setReferencedObject(referencedObject);
		assertTrue(graph.addEdge(edge));
		assertEquals(2, graph.getNodes().size());
		assertEquals(1, graph.getEdges().size());
		assertEquals(edge, graph.getElements(referencedObject).iterator().next());
	}
	
	@Test
	@DisplayName("Add edge with missing target node.")
	void testAddEdgeWithMissingTarget() throws GraphManipulationException {
		ModelNode source = new ModelNode();
		ModelNode target = new ModelNode();
		graph.addNode(source);
		ModelEdge edge = new ModelEdge(source, target);
		assertThrows(GraphManipulationException.class, () -> graph.addEdge(edge));
	}
	
	@Test
	@DisplayName("Add edge with null value as source reference")
	void testAddEdgeWithNullSource() throws GraphManipulationException {
		ModelNode target = new ModelNode();
		graph.addNode(target);
		ModelEdge edge = new ModelEdge(null, target);
		assertThrows(GraphManipulationException.class, () -> graph.addEdge(edge));
	}
	
	@Test
	@DisplayName("Add existing edge after removal of target node.")
	void testAddExistingEdgeWithMissingTarget() throws GraphManipulationException {
		ModelNode source = new ModelNode();
		ModelNode target = new ModelNode();
		graph.addNode(source);
		graph.addNode(target);
		ModelEdge edge = new ModelEdge(source, target);
		graph.addEdge(edge);
		graph.removeNode(target);
		assertThrows(GraphManipulationException.class, () -> graph.addEdge(edge));
	}
	
	@Test
	@DisplayName("Adding nodes referencing the same object.")
	void testAddNodesWithSameReference() throws GraphManipulationException {
		ModelNode nodeOne = new ModelNode();
		ModelNode nodeTwo = new ModelNode();
		EClass referencedObject = EcoreFactory.eINSTANCE.createEClass();
		nodeOne.setReferencedObject(referencedObject);
		nodeTwo.setReferencedObject(referencedObject);
		graph.addNode(nodeOne);
		graph.addNode(nodeTwo);
		assertEquals(2, graph.getNodes().size());
		assertEquals(2, graph.getElements(referencedObject).size());
	}
	
	@Test
	@DisplayName("Adding new node with incoming edge.")
	void testAddNewNodeWithIncomingEdge() {
		ModelNode source = new ModelNode();
		ModelNode target = new ModelNode();
		ModelEdge edge = new ModelEdge(source, target);
		source.getIncomingEdges().add(edge);
		assertThrows(GraphManipulationException.class, () -> graph.addNode(source));
	}
	
	@Test
	@DisplayName("Adding existing node with incoming edge.")
	void testAddExistingNodeWithIncomingEdge() throws GraphManipulationException {
		ModelNode source = new ModelNode();
		graph.addNode(source);
		ModelNode target = new ModelNode();
		ModelEdge edge = new ModelEdge(source, target);
		source.getIncomingEdges().add(edge);
		assertFalse(graph.addNode(source));
	}
	
	@Test
	@DisplayName("Remove node referencing an onject referenced by another node.")
	void testRemoveNodeWithReference() throws GraphManipulationException {
		ModelNode nodeOne = new ModelNode();
		ModelNode nodeTwo = new ModelNode();
		EClass referencedObject = EcoreFactory.eINSTANCE.createEClass();
		nodeOne.setReferencedObject(referencedObject);
		nodeTwo.setReferencedObject(referencedObject);
		graph.addNode(nodeOne);
		graph.addNode(nodeTwo);
		assertTrue(graph.removeNode(nodeOne));
		Set<ModelGraphElement> references = graph.getElements(referencedObject);
		assertEquals(1, references.size());		
		assertTrue(references.contains(nodeTwo));
	}
	
	@Test
	@DisplayName("Remove node together with its outgoing edge.")
	void testRemoveNodeWithEdge() throws GraphManipulationException {
		ModelNode source = new ModelNode();
		ModelNode target = new ModelNode();
		graph.addNode(source);
		graph.addNode(target);
		ModelEdge edge = new ModelEdge(source, target);
		graph.addEdge(edge);		
		assertTrue(graph.removeNode(source));
		assertEquals(0, graph.getEdges().size());
		
	}
	
	@Test
	@DisplayName("Remove edge referencing an object not refrenced by another element.")
	void testRemoveEdgeWithReference() throws GraphManipulationException {
		ModelNode source = new ModelNode();
		ModelNode target = new ModelNode();
		graph.addNode(source);
		graph.addNode(target);
		EClass referencedObject = EcoreFactory.eINSTANCE.createEClass();
		ModelEdge edge = new ModelEdge(source, target);
		edge.setReferencedObject(referencedObject);
		graph.addEdge(edge);
		assertTrue(graph.removeEdge(edge));
		Set<ModelGraphElement> references = graph.getElements(referencedObject);
		assertEquals(0, references.size());
	}
}
