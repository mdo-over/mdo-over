package de.uni_marburg.mdo_over.graphtools.emf;

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
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import metamodels.genericTestModel.GenericTestModelPackage;

abstract class HenshinRuleAdapterTest {

	protected static final String RULE_DIR = "src/test/resources/rule_output/";
	protected TestInfo testInfo;
	protected Engine engine;

	@BeforeEach
	void setup(TestInfo testInfo) {
		this.testInfo = testInfo;
		engine = new EngineImpl();
	}
	
	
	@Test
	void test_NonAutomatedTests() {
		fail("Test cases in MappingRuleAdapterTest are not fully automated yet but need manual inspection of the created rules.");
	}
	
	protected void saveRule(Module module) throws IOException {
		String className = testInfo.getTestClass().get().getSimpleName();
		String testName = testInfo.getTestMethod().get().getName();
		String dirName = RULE_DIR + className + "/";
		module.getImports().add(GenericTestModelPackage.eINSTANCE);
		HenshinResourceSet rs = new HenshinResourceSet(dirName);
		Resource res = rs.createResource(testName + ".henshin");
		res.getContents().add(module);
		res.save(null);
		substituteStaticPackageReference(dirName + res.getURI().toFileString());
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
