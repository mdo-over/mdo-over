package de.uni_marburg.mdo_over.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

import metamodels.architectureCRA.ArchitectureCRAPackage;
import metamodels.genericTestModel.GenericTestModelPackage;

public class ResourceUtils {

	private static final String BASEDIR = "src/test/resources/";
	private static HenshinResourceSet rs;

	public static EObject loadCRAModel(String modelPath, boolean dynamic) {
		if (rs == null) {
			rs = new HenshinResourceSet(BASEDIR);
		}
		if (dynamic) {
			Resource metamodelRes = rs.getResource("metamodels/architectureCRA.ecore");
			EPackage ePackage = (EPackage) metamodelRes.getContents().get(0);
			rs.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		} else {		
			rs.getPackageRegistry().put(ArchitectureCRAPackage.eINSTANCE.getNsURI(), ArchitectureCRAPackage.eINSTANCE);
		}
		Resource modelRes = rs.getResource(modelPath);
		return modelRes.getContents().get(0);
	}
	
	public static EObject loadGenericModel(String modelPath, boolean dynamic) {
		if (rs == null) {
			rs = new HenshinResourceSet(BASEDIR);
		}
		if (dynamic) {
			Resource metamodelRes = rs.getResource("metamodels/genericTestModel.ecore");
			EPackage ePackage = (EPackage) metamodelRes.getContents().get(0);
			rs.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		} else {		
			rs.getPackageRegistry().put(GenericTestModelPackage.eINSTANCE.getNsURI(), GenericTestModelPackage.eINSTANCE);
		}
		Resource modelRes = rs.getResource(modelPath);
		return modelRes.getContents().get(0);
	}
}
