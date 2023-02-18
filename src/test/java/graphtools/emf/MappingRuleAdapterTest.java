package graphtools.emf;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graphtools.SubGraphConstructor;
import model.modelgraph.GraphManipulationException;
import utils.creational.modeltemplates.OneLevelModelBuilder;
import utils.creational.modeltemplates.TestModelFactory;

class MappingRuleAdapterTest extends HenshinRuleAdapterTest {
	
	private SubGraphConstructor mappingConstructor;
	private OneLevelModelBuilder image;
	private MappingRuleAdapter ruleAdapter;
	@BeforeEach
	void setupTestModel() throws GraphManipulationException {
		image = TestModelFactory.createOneLevelModel();
		mappingConstructor = new SubGraphConstructor(image.graph);
	}

	@Test
	void test_AllElementsAndAttributesMapped_OnlyPreservedElementsWithAllAttributes() throws GraphManipulationException, IOException {
		mappingConstructor.addEdgeCopy(image.rootToA);
		mappingConstructor.addEdgeCopy(image.rootToB);
		ruleAdapter = new MappingRuleAdapter(engine, mappingConstructor.getMapping());
		saveRule(ruleAdapter.getRule().getModule());
//		TODO: Manually create expected rule and check for isomorphy of the rule models. 		
//		CModule module = new CModule("MRA");
//		CRule rule = module.createRule("mapping_based");
		
	}
	
	@Test
	void test_AllElementsMappedButAttributesNotInOrigin_OnlyPreservedElementsWithUnmappedAttributesIgnored() throws GraphManipulationException, IOException {
		mappingConstructor.addEdgeCopy(image.rootToA);
		mappingConstructor.addEdgeCopy(image.rootToB);
		image.aNode.clearAttributes();
		image.bNode.clearAttributes();
		ruleAdapter = new MappingRuleAdapter(engine, mappingConstructor.getMapping());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_NodeNotInOrigin_RepresentedByCreateNode() throws GraphManipulationException, IOException {
		mappingConstructor.addEdgeCopy(image.rootToA);
		image.remove(image.rootToB);
		ruleAdapter = new MappingRuleAdapter(engine, mappingConstructor.getMapping());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_EdgeBetweenMappedNodesNotInOrigin_RepresentedByCreatedEdgeBetweenPreservedNodes() throws GraphManipulationException, IOException {
		mappingConstructor.addEdgeCopy(image.rootToA);
		mappingConstructor.addNodeCopy(image.bNode);
		ruleAdapter = new MappingRuleAdapter(engine, mappingConstructor.getMapping());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_EdgeAndTargetNotInOrigin_RepresentedByCreatedEdgeAndTargetNode() throws GraphManipulationException, IOException {
		mappingConstructor.addEdgeCopy(image.rootToA);
		ruleAdapter = new MappingRuleAdapter(engine, mappingConstructor.getMapping());
		saveRule(ruleAdapter.getRule().getModule());
	}
	
	@Test
	void test_NodeAndEdgeNotInImage_OnlyPreservedElementsForMappedElements() throws GraphManipulationException, IOException {
		mappingConstructor.addEdgeCopy(image.rootToA);
		mappingConstructor.addEdgeCopy(image.rootToB);
		image.remove(image.bNode);
		ruleAdapter = new MappingRuleAdapter(engine, mappingConstructor.getMapping());
		saveRule(ruleAdapter.getRule().getModule());
	}	
}
