package graphtools.emf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.eclipse.emf.henshin.model.Node;
import org.junit.jupiter.api.Test;

import de.uni_marburg.mdo_over.graphtools.emf.GraphRuleAdapter;
import metamodels.genericTestModel.A;
import metamodels.genericTestModel.B;
import metamodels.genericTestModel.GenericTestModelPackage;
import utils.creational.modeltemplates.OneLevelModelBuilder;
import utils.creational.modeltemplates.TestModelFactory;

class GraphRuleAdapterTest extends HenshinRuleAdapterTest {

	@Test
	void test_GraphContainingNullInStringAttribute_RuleContainsAttributeWithNullString() throws IOException {
		OneLevelModelBuilder modelBuilder = TestModelFactory.createOneLevelModel();
		((A)modelBuilder.aNode.getReferencedObject()).setValue(1);
		((B)modelBuilder.bNode.getReferencedObject()).setName(null);
		GraphRuleAdapter ruleAdapter = new GraphRuleAdapter(engine, modelBuilder.graph);
		Node b = ruleAdapter.getRule().getLhs().getNodes(GenericTestModelPackage.Literals.B).get(0);
		assertEquals("null", b.getAttribute(GenericTestModelPackage.Literals.B__NAME).getValue());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_GraphContainingEmptyStringInAttribute_RuleContainsEscapedEmptyString() throws IOException {
		OneLevelModelBuilder modelBuilder = TestModelFactory.createOneLevelModel();
		((A)modelBuilder.aNode.getReferencedObject()).setValue(1);
		((B)modelBuilder.bNode.getReferencedObject()).setName("");
		GraphRuleAdapter ruleAdapter = new GraphRuleAdapter(engine, modelBuilder.graph);
		Node b = ruleAdapter.getRule().getLhs().getNodes(GenericTestModelPackage.Literals.B).get(0);
		assertEquals("\"\"", b.getAttribute(GenericTestModelPackage.Literals.B__NAME).getValue());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_GraphContainingNonEmptyStringInAttribute_RuleContainsEscapedTestString() throws IOException {
		OneLevelModelBuilder modelBuilder = TestModelFactory.createOneLevelModel();
		((A)modelBuilder.aNode.getReferencedObject()).setValue(1);
		((B)modelBuilder.bNode.getReferencedObject()).setName("test");
		GraphRuleAdapter ruleAdapter = new GraphRuleAdapter(engine, modelBuilder.graph);
		Node b = ruleAdapter.getRule().getLhs().getNodes(GenericTestModelPackage.Literals.B).get(0);
		assertEquals("\"test\"", b.getAttribute(GenericTestModelPackage.Literals.B__NAME).getValue());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_GraphContainingUnsetIntAttribute_RuleContainsStringRepresentationOfDefaultValue() throws IOException {
		OneLevelModelBuilder modelBuilder = TestModelFactory.createOneLevelModel();
		((B)modelBuilder.bNode.getReferencedObject()).setName("test");
		GraphRuleAdapter ruleAdapter = new GraphRuleAdapter(engine, modelBuilder.graph);
		Node a = ruleAdapter.getRule().getLhs().getNodes(GenericTestModelPackage.Literals.A).get(0);
		assertEquals("0", a.getAttribute(GenericTestModelPackage.Literals.A__VALUE).getValue());
		saveRule(ruleAdapter.getRule().getModule());
	}
}

