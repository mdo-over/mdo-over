package de.uni_marburg.mdo_over.graphtools.emf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.interpreter.util.EcoreCopier;

@SuppressWarnings("serial")
public class PartialModelCopier extends EcoreCopier {

	@Override
	protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject) {
		// Do not copy containments transitively. All EReferences need to be copied manually.
	}
	
	
}
