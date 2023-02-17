/**
 */
package metamodels.genericTestModel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>B</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.B#getB_to_a <em>Bto a</em>}</li>
 *   <li>{@link metamodels.genericTestModel.B#getB_opp_a <em>Bopp a</em>}</li>
 *   <li>{@link metamodels.genericTestModel.B#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see metamodels.genericTestModel.GenericTestModelPackage#getB()
 * @model
 * @generated
 */
public interface B extends EObject {
	/**
	 * Returns the value of the '<em><b>Bto a</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bto a</em>' reference.
	 * @see #setB_to_a(A)
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getB_B_to_a()
	 * @model
	 * @generated
	 */
	A getB_to_a();

	/**
	 * Sets the value of the '{@link metamodels.genericTestModel.B#getB_to_a <em>Bto a</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bto a</em>' reference.
	 * @see #getB_to_a()
	 * @generated
	 */
	void setB_to_a(A value);

	/**
	 * Returns the value of the '<em><b>Bopp a</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link metamodels.genericTestModel.A#getA_opp_b <em>Aopp b</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bopp a</em>' reference.
	 * @see #setB_opp_a(A)
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getB_B_opp_a()
	 * @see metamodels.genericTestModel.A#getA_opp_b
	 * @model opposite="a_opp_b"
	 * @generated
	 */
	A getB_opp_a();

	/**
	 * Sets the value of the '{@link metamodels.genericTestModel.B#getB_opp_a <em>Bopp a</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bopp a</em>' reference.
	 * @see #getB_opp_a()
	 * @generated
	 */
	void setB_opp_a(A value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see metamodels.genericTestModel.GenericTestModelPackage#getB_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link metamodels.genericTestModel.B#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // B
