package recombination.crossoverpoint.postprocessing;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public class DefaultAttributeProcessor implements IAttributeProcessor {

	@Override
	public boolean acceptsAttribute(EAttribute attribute) {
		return false;
	}

	@Override
	public Object processAttribute(EObject firstParent, EObject secondParent, EAttribute attribute) {
		return null;
	}

}
