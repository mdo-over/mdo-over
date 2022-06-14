/**
 */
package metamodels.genericTestModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>X</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.X#getX_to_a <em>Xto a</em>}</li>
 *   <li>{@link metamodels.genericTestModel.X#getX_to_b <em>Xto b</em>}</li>
 *   <li>{@link metamodels.genericTestModel.X#getX_to_x <em>Xto x</em>}</li>
 * </ul>
 *
 * @see metamodels.genericTestModel.GenericTestModelPackage#getX()
 * @model
 * @generated
 */
public interface X extends EObject {
	/**
	 * Returns the value of the '<em><b>Xto a</b></em>' reference list.
	 * The list contents are of type {@link metamodels.genericTestModel.A}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xto a</em>' reference list.
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getX_X_to_a()
	 * @model
	 * @generated
	 */
	EList<A> getX_to_a();

	/**
	 * Returns the value of the '<em><b>Xto b</b></em>' reference list.
	 * The list contents are of type {@link metamodels.genericTestModel.B}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xto b</em>' reference list.
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getX_X_to_b()
	 * @model
	 * @generated
	 */
	EList<B> getX_to_b();

	/**
	 * Returns the value of the '<em><b>Xto x</b></em>' reference list.
	 * The list contents are of type {@link metamodels.genericTestModel.X}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xto x</em>' reference list.
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getX_X_to_x()
	 * @model
	 * @generated
	 */
	EList<X> getX_to_x();

} // X
