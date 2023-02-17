/**
 */
package metamodels.genericTestModel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see metamodels.genericTestModel.GenericTestModelFactory
 * @model kind="package"
 * @generated
 */
public interface GenericTestModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "genericTestModel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "testmodel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "testmodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GenericTestModelPackage eINSTANCE = metamodels.genericTestModel.impl.GenericTestModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link metamodels.genericTestModel.impl.RootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodels.genericTestModel.impl.RootImpl
	 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getRoot()
	 * @generated
	 */
	int ROOT = 0;

	/**
	 * The feature id for the '<em><b>Acont</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__ACONT = 0;

	/**
	 * The feature id for the '<em><b>Bcont</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT__BCONT = 1;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodels.genericTestModel.impl.AImpl <em>A</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodels.genericTestModel.impl.AImpl
	 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getA()
	 * @generated
	 */
	int A = 1;

	/**
	 * The feature id for the '<em><b>Xcont</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__XCONT = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Aopp b</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__AOPP_B = 2;

	/**
	 * The feature id for the '<em><b>Aopp cont</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A__AOPP_CONT = 3;

	/**
	 * The number of structural features of the '<em>A</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>A</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int A_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodels.genericTestModel.impl.BImpl <em>B</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodels.genericTestModel.impl.BImpl
	 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getB()
	 * @generated
	 */
	int B = 2;

	/**
	 * The feature id for the '<em><b>Bto a</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int B__BTO_A = 0;

	/**
	 * The feature id for the '<em><b>Bopp a</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int B__BOPP_A = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int B__NAME = 2;

	/**
	 * The number of structural features of the '<em>B</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int B_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>B</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int B_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metamodels.genericTestModel.impl.XImpl <em>X</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metamodels.genericTestModel.impl.XImpl
	 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getX()
	 * @generated
	 */
	int X = 3;

	/**
	 * The feature id for the '<em><b>Xto a</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int X__XTO_A = 0;

	/**
	 * The feature id for the '<em><b>Xto b</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int X__XTO_B = 1;

	/**
	 * The feature id for the '<em><b>Xto x</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int X__XTO_X = 2;

	/**
	 * The number of structural features of the '<em>X</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int X_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>X</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int X_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link metamodels.genericTestModel.Root <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see metamodels.genericTestModel.Root
	 * @generated
	 */
	EClass getRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link metamodels.genericTestModel.Root#getA_cont <em>Acont</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Acont</em>'.
	 * @see metamodels.genericTestModel.Root#getA_cont()
	 * @see #getRoot()
	 * @generated
	 */
	EReference getRoot_A_cont();

	/**
	 * Returns the meta object for the containment reference list '{@link metamodels.genericTestModel.Root#getB_cont <em>Bcont</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bcont</em>'.
	 * @see metamodels.genericTestModel.Root#getB_cont()
	 * @see #getRoot()
	 * @generated
	 */
	EReference getRoot_B_cont();

	/**
	 * Returns the meta object for class '{@link metamodels.genericTestModel.A <em>A</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>A</em>'.
	 * @see metamodels.genericTestModel.A
	 * @generated
	 */
	EClass getA();

	/**
	 * Returns the meta object for the containment reference list '{@link metamodels.genericTestModel.A#getX_cont <em>Xcont</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Xcont</em>'.
	 * @see metamodels.genericTestModel.A#getX_cont()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_X_cont();

	/**
	 * Returns the meta object for the attribute '{@link metamodels.genericTestModel.A#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see metamodels.genericTestModel.A#getValue()
	 * @see #getA()
	 * @generated
	 */
	EAttribute getA_Value();

	/**
	 * Returns the meta object for the reference '{@link metamodels.genericTestModel.A#getA_opp_b <em>Aopp b</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Aopp b</em>'.
	 * @see metamodels.genericTestModel.A#getA_opp_b()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_A_opp_b();

	/**
	 * Returns the meta object for the reference '{@link metamodels.genericTestModel.A#getA_opp_cont <em>Aopp cont</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Aopp cont</em>'.
	 * @see metamodels.genericTestModel.A#getA_opp_cont()
	 * @see #getA()
	 * @generated
	 */
	EReference getA_A_opp_cont();

	/**
	 * Returns the meta object for class '{@link metamodels.genericTestModel.B <em>B</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>B</em>'.
	 * @see metamodels.genericTestModel.B
	 * @generated
	 */
	EClass getB();

	/**
	 * Returns the meta object for the reference '{@link metamodels.genericTestModel.B#getB_to_a <em>Bto a</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bto a</em>'.
	 * @see metamodels.genericTestModel.B#getB_to_a()
	 * @see #getB()
	 * @generated
	 */
	EReference getB_B_to_a();

	/**
	 * Returns the meta object for the reference '{@link metamodels.genericTestModel.B#getB_opp_a <em>Bopp a</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bopp a</em>'.
	 * @see metamodels.genericTestModel.B#getB_opp_a()
	 * @see #getB()
	 * @generated
	 */
	EReference getB_B_opp_a();

	/**
	 * Returns the meta object for the attribute '{@link metamodels.genericTestModel.B#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see metamodels.genericTestModel.B#getName()
	 * @see #getB()
	 * @generated
	 */
	EAttribute getB_Name();

	/**
	 * Returns the meta object for class '{@link metamodels.genericTestModel.X <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>X</em>'.
	 * @see metamodels.genericTestModel.X
	 * @generated
	 */
	EClass getX();

	/**
	 * Returns the meta object for the reference list '{@link metamodels.genericTestModel.X#getX_to_a <em>Xto a</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Xto a</em>'.
	 * @see metamodels.genericTestModel.X#getX_to_a()
	 * @see #getX()
	 * @generated
	 */
	EReference getX_X_to_a();

	/**
	 * Returns the meta object for the reference list '{@link metamodels.genericTestModel.X#getX_to_b <em>Xto b</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Xto b</em>'.
	 * @see metamodels.genericTestModel.X#getX_to_b()
	 * @see #getX()
	 * @generated
	 */
	EReference getX_X_to_b();

	/**
	 * Returns the meta object for the reference list '{@link metamodels.genericTestModel.X#getX_to_x <em>Xto x</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Xto x</em>'.
	 * @see metamodels.genericTestModel.X#getX_to_x()
	 * @see #getX()
	 * @generated
	 */
	EReference getX_X_to_x();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GenericTestModelFactory getGenericTestModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link metamodels.genericTestModel.impl.RootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodels.genericTestModel.impl.RootImpl
		 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getRoot()
		 * @generated
		 */
		EClass ROOT = eINSTANCE.getRoot();

		/**
		 * The meta object literal for the '<em><b>Acont</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROOT__ACONT = eINSTANCE.getRoot_A_cont();

		/**
		 * The meta object literal for the '<em><b>Bcont</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROOT__BCONT = eINSTANCE.getRoot_B_cont();

		/**
		 * The meta object literal for the '{@link metamodels.genericTestModel.impl.AImpl <em>A</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodels.genericTestModel.impl.AImpl
		 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getA()
		 * @generated
		 */
		EClass A = eINSTANCE.getA();

		/**
		 * The meta object literal for the '<em><b>Xcont</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__XCONT = eINSTANCE.getA_X_cont();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute A__VALUE = eINSTANCE.getA_Value();

		/**
		 * The meta object literal for the '<em><b>Aopp b</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__AOPP_B = eINSTANCE.getA_A_opp_b();

		/**
		 * The meta object literal for the '<em><b>Aopp cont</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference A__AOPP_CONT = eINSTANCE.getA_A_opp_cont();

		/**
		 * The meta object literal for the '{@link metamodels.genericTestModel.impl.BImpl <em>B</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodels.genericTestModel.impl.BImpl
		 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getB()
		 * @generated
		 */
		EClass B = eINSTANCE.getB();

		/**
		 * The meta object literal for the '<em><b>Bto a</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference B__BTO_A = eINSTANCE.getB_B_to_a();

		/**
		 * The meta object literal for the '<em><b>Bopp a</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference B__BOPP_A = eINSTANCE.getB_B_opp_a();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute B__NAME = eINSTANCE.getB_Name();

		/**
		 * The meta object literal for the '{@link metamodels.genericTestModel.impl.XImpl <em>X</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metamodels.genericTestModel.impl.XImpl
		 * @see metamodels.genericTestModel.impl.GenericTestModelPackageImpl#getX()
		 * @generated
		 */
		EClass X = eINSTANCE.getX();

		/**
		 * The meta object literal for the '<em><b>Xto a</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference X__XTO_A = eINSTANCE.getX_X_to_a();

		/**
		 * The meta object literal for the '<em><b>Xto b</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference X__XTO_B = eINSTANCE.getX_X_to_b();

		/**
		 * The meta object literal for the '<em><b>Xto x</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference X__XTO_X = eINSTANCE.getX_X_to_x();

	}

} //GenericTestModelPackage
