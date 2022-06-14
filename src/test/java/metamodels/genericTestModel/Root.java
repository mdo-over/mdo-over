/**
 */
package metamodels.genericTestModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.Root#getA_cont <em>Acont</em>}</li>
 *   <li>{@link metamodels.genericTestModel.Root#getB_cont <em>Bcont</em>}</li>
 * </ul>
 *
 * @see metamodels.genericTestModel.GenericTestModelPackage#getRoot()
 * @model
 * @generated
 */
public interface Root extends EObject {
	/**
	 * Returns the value of the '<em><b>Acont</b></em>' containment reference list.
	 * The list contents are of type {@link metamodels.genericTestModel.A}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Acont</em>' containment reference list.
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getRoot_A_cont()
	 * @model containment="true"
	 * @generated
	 */
	EList<A> getA_cont();

	/**
	 * Returns the value of the '<em><b>Bcont</b></em>' containment reference list.
	 * The list contents are of type {@link metamodels.genericTestModel.B}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bcont</em>' containment reference list.
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getRoot_B_cont()
	 * @model containment="true"
	 * @generated
	 */
	EList<B> getB_cont();

} // Root
