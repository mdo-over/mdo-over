/**
 */
package metamodels.genericTestModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.A#getX_cont <em>Xcont</em>}</li>
 *   <li>{@link metamodels.genericTestModel.A#getValue <em>Value</em>}</li>
 *   <li>{@link metamodels.genericTestModel.A#getA_opp_b <em>Aopp b</em>}</li>
 *   <li>{@link metamodels.genericTestModel.A#getA_opp_cont <em>Aopp cont</em>}</li>
 * </ul>
 *
 * @see metamodels.genericTestModel.GenericTestModelPackage#getA()
 * @model
 * @generated
 */
public interface A extends EObject {
	/**
	 * Returns the value of the '<em><b>Xcont</b></em>' containment reference list.
	 * The list contents are of type {@link metamodels.genericTestModel.X}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xcont</em>' containment reference list.
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getA_X_cont()
	 * @model containment="true"
	 * @generated
	 */
	EList<X> getX_cont();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getA_Value()
	 * @model
	 * @generated
	 */
	int getValue();

	/**
	 * Sets the value of the '{@link metamodels.genericTestModel.A#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(int value);

	/**
	 * Returns the value of the '<em><b>Aopp b</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link metamodels.genericTestModel.B#getB_opp_a <em>Bopp a</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aopp b</em>' reference.
	 * @see #setA_opp_b(B)
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getA_A_opp_b()
	 * @see metamodels.genericTestModel.B#getB_opp_a
	 * @model opposite="b_opp_a"
	 * @generated
	 */
	B getA_opp_b();

	/**
	 * Sets the value of the '{@link metamodels.genericTestModel.A#getA_opp_b <em>Aopp b</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Aopp b</em>' reference.
	 * @see #getA_opp_b()
	 * @generated
	 */
	void setA_opp_b(B value);

	/**
	 * Returns the value of the '<em><b>Aopp cont</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aopp cont</em>' reference.
	 * @see #setA_opp_cont(Root)
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getA_A_opp_cont()
	 * @model
	 * @generated
	 */
	Root getA_opp_cont();

	/**
	 * Sets the value of the '{@link metamodels.genericTestModel.A#getA_opp_cont <em>Aopp cont</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Aopp cont</em>' reference.
	 * @see #getA_opp_cont()
	 * @generated
	 */
	void setA_opp_cont(Root value);

} // A
