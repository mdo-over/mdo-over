package graphtools.emf;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.compact.CModule;
import org.eclipse.emf.henshin.model.compact.CRule;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import graphtools.SubGraphConstructor;
import metamodels.genericTestModel.A;
import metamodels.genericTestModel.GenericTestModelPackage;
import metamodels.genericTestModel.Root;
import model.modelgraph.GraphManipulationException;
import model.modelgraph.ModelEdge;
import model.modelgraph.ModelNode;
import utils.creational.modeltemplates.OneLevelModelBuilder;
import utils.creational.modeltemplates.TestModelFactory;

class MappingRuleAdapterTest {
	
	protected static final String RULE_DIR = "src/test/resources/rule_output/MappingRuleAdapterTest/";
	
	private TestInfo testInfo;
	private SubGraphConstructor mappingConstructor;
	private OneLevelModelBuilder image;
	private MappingRuleAdapter ruleAdapter;
	private Engine engine;
	
	@BeforeEach
	void setupTestModel(TestInfo testInfo) throws GraphManipulationException {
		this.testInfo = testInfo;
		image = TestModelFactory.createOneLevelModel();
		mappingConstructor = new SubGraphConstructor(image.graph);
		engine = new EngineImpl();
	}
	
	@Test
	void test_NonAutomatedTests() {
		fail("Test cases in MappingRuleAdapterTest are not fully automated yet but need manual inspection of the created rules.");
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
	
	private void saveRule(Module module) throws IOException {
		String filename = testInfo.getTestMethod().get().getName();
		module.getImports().add(GenericTestModelPackage.eINSTANCE);
		HenshinResourceSet rs = new HenshinResourceSet(RULE_DIR);
		Resource res = rs.createResource(filename + ".henshin");
		res.getContents().add(module);
		res.save(null);
		substituteStaticPackageReference(RULE_DIR + res.getURI().toFileString());
	}
	
	private void substituteStaticPackageReference(String original) throws IOException {
	    String outputFile = original + "_temp";
	    try (Stream<String> stream = Files.lines(Paths.get(original));
	            FileOutputStream fop = new FileOutputStream(new File(outputFile))) {
	        stream.map(line -> line.replace("testmodel#", "../../metamodels/genericTestModel.ecore#")).forEach(line -> {
	            try {
	                fop.write(line.getBytes());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        });
	    }
	    Files.copy(Paths.get(outputFile), Paths.get(original), StandardCopyOption.REPLACE_EXISTING);
	    Files.delete(Paths.get(outputFile));
	}
	
	
}
