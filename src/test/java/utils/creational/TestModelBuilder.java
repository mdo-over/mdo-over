package utils.creational;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import metamodels.genericTestModel.A;
import metamodels.genericTestModel.B;
import metamodels.genericTestModel.GenericTestModelFactory;
import metamodels.genericTestModel.GenericTestModelPackage;
import metamodels.genericTestModel.Root;
import metamodels.genericTestModel.X;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelGraph;
import model.modelgraph.ModelNode;

public class TestModelBuilder {
	
	public ModelGraph graph;
	public EObject emfModelRoot;
	GenericTestModelFactory fac = GenericTestModelFactory.eINSTANCE;
			
	public TestModelBuilder() {
		this.graph = new ModelGraph();
	}
	
	public ModelNode createRoot() throws GraphManipulationException {
		Root root = fac.createRoot();
		ModelNode rootNode = new ModelNode();
		rootNode.setReferencedObject(root);
		graph.addNode(rootNode);
		graph.setRoot(rootNode);
		emfModelRoot = root;
		return rootNode;
	}
	
	public ModelNode createA() throws GraphManipulationException {
		A a = fac.createA();
		ModelNode aNode = new ModelNode();
		aNode.setReferencedObject(a);
		graph.addNode(aNode);
		return aNode;
	}	
	
	public ModelEdge createRootToA(ModelNode root, ModelNode aNode) throws GraphManipulationException {
		ModelEdge rootToAEdge = new ModelEdge(root, aNode);
		((Root)root.getReferencedObject()).getA_cont().add((A)aNode.getReferencedObject());
		rootToAEdge.setReferencedObject(GenericTestModelPackage.Literals.ROOT__ACONT);
		graph.addEdge(rootToAEdge);
		return rootToAEdge;
	}
	
	public ModelNode createB() throws GraphManipulationException {
		B b = fac.createB();
		ModelNode bNode = new ModelNode();
		bNode.setReferencedObject(b);
		graph.addNode(bNode);
		return bNode;
	}
	
	public ModelEdge createRootToB(ModelNode root, ModelNode bNode) throws GraphManipulationException {
		ModelEdge rootToBEdge = new ModelEdge(root, bNode);
		((Root)root.getReferencedObject()).getB_cont().add((B)bNode.getReferencedObject());
		rootToBEdge.setReferencedObject(GenericTestModelPackage.Literals.ROOT__BCONT);
		graph.addEdge(rootToBEdge);
		return rootToBEdge;
	}
	
	public ModelEdge createAOppB(ModelNode aNode, ModelNode bNode) throws GraphManipulationException {
		ModelEdge aOppBEdge = new ModelEdge(aNode, bNode);
		((A)aNode.getReferencedObject()).setA_opp_b((B)bNode.getReferencedObject());
		aOppBEdge.setReferencedObject(GenericTestModelPackage.Literals.A__AOPP_B);
		graph.addEdge(aOppBEdge);
		return aOppBEdge;
	}
	
	public ModelEdge createBOppA(ModelNode bNode, ModelNode aNode) throws GraphManipulationException {
		ModelEdge bOppAEdge = new ModelEdge(bNode, aNode);
		((B)bNode.getReferencedObject()).setB_opp_a((A)aNode.getReferencedObject());
		bOppAEdge.setReferencedObject(GenericTestModelPackage.Literals.B__BOPP_A);
		graph.addEdge(bOppAEdge);
		return bOppAEdge;
	}
	
	public ModelEdge createBToA(ModelNode bNode, ModelNode aNode) throws GraphManipulationException {
		ModelEdge bToAEdge = new ModelEdge(bNode, aNode);
		((B)bNode.getReferencedObject()).setB_to_a((A)aNode.getReferencedObject());
		bToAEdge.setReferencedObject(GenericTestModelPackage.Literals.B__BTO_A);
		graph.addEdge(bToAEdge);
		return bToAEdge;
	}
	
	public ModelNode createX() throws GraphManipulationException {
		X x = fac.createX();
		ModelNode xNode = new ModelNode();
		xNode.setReferencedObject(x);
		graph.addNode(xNode);
		return xNode;
	}
	
	public ModelEdge createAToX(ModelNode aNode, ModelNode xNode) throws GraphManipulationException {
		ModelEdge aToXEdge = new ModelEdge(aNode, xNode);
		((A)aNode.getReferencedObject()).getX_cont().add((X)xNode.getReferencedObject());
		aToXEdge.setReferencedObject(GenericTestModelPackage.Literals.A__XCONT);
		graph.addEdge(aToXEdge);
		return aToXEdge;
	}
	
	public ModelEdge createXToA(ModelNode xNode, ModelNode aNode) throws GraphManipulationException {
		ModelEdge xToAEdge = new ModelEdge(xNode, aNode);
		((X)xNode.getReferencedObject()).getX_to_a().add((A)aNode.getReferencedObject());
		xToAEdge.setReferencedObject(GenericTestModelPackage.Literals.X__XTO_A);
		graph.addEdge(xToAEdge);
		return xToAEdge;
	}
	
	public ModelEdge createXToB(ModelNode xNode, ModelNode bNode) throws GraphManipulationException {
		ModelEdge xToBEdge = new ModelEdge(xNode, bNode);
		((X)xNode.getReferencedObject()).getX_to_b().add((B)bNode.getReferencedObject());
		xToBEdge.setReferencedObject(GenericTestModelPackage.Literals.X__XTO_B);
		graph.addEdge(xToBEdge);
		return xToBEdge;
	}
	
	public ModelEdge createXToX(ModelNode xSourceNode, ModelNode xTargetNode) throws GraphManipulationException {
		ModelEdge xToXEdge = new ModelEdge(xSourceNode, xTargetNode);
		((X)xSourceNode.getReferencedObject()).getX_to_x().add((X)xTargetNode.getReferencedObject());
		xToXEdge.setReferencedObject(GenericTestModelPackage.Literals.X__XTO_X);
		graph.addEdge(xToXEdge);
		return xToXEdge;
	}
	
	public void remove(ModelNode node) {
		for (ModelEdge outEdge : node.getOutgoingEdges()) {
			remove(outEdge);
		}
		for (ModelEdge inEdge : node.getIncomingEdges()) {
			remove(inEdge);
		}
		graph.removeNode(node);
		
	}
	
	public void remove(ModelEdge edge) {
		EObject srcObj = edge.getSource().getReferencedObject();
		EObject trgObj = edge.getTarget().getReferencedObject();
		EReference refObj = (EReference)edge.getReferencedObject();
		if (refObj.isMany()) {
			@SuppressWarnings("unchecked")
			EList<EObject> values = (EList<EObject>) srcObj.eGet(refObj);
			values.remove(trgObj);
		} else {
			srcObj.eUnset(refObj);
		}
		graph.removeEdge(edge);
	}
	
	@SuppressWarnings("serial")
	protected class TestModelCreationException extends RuntimeException {
	}
	
}
