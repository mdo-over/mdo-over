package de.uni_marburg.mdo_over.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * A matcher to check whether or not an EObject has a feature with the specified value. Equality of the actual and the
 * expected value is checked by means of the equals method of the expected value.
 * 
 * @author S. John
 *
 */
public class EObjectHasFeatureWithValue extends TypeSafeMatcher<EObject> {
	String featureName;
	Object expectedValue;

	public EObjectHasFeatureWithValue(String featureName, Object expectedValue) {
		this.featureName = featureName;
		this.expectedValue = expectedValue;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("feature " + featureName + " has value " + expectedValue);

	}

	@Override
	protected boolean matchesSafely(EObject obj) {
		EStructuralFeature feature = obj.eClass().getEStructuralFeature(featureName);
		if (feature == null) {
			new IllegalArgumentException("The object does not contain a feature with that name.");
		}
		Object value = obj.eGet(feature);
		return expectedValue.equals(value);
	}

	public static Matcher<EObject> hasfeatureWithValue(String featureName, Object expectedValue) {
		return new EObjectHasFeatureWithValue(featureName, expectedValue);
	}

}
