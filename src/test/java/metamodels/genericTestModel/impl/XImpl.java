/**
 */
package metamodels.genericTestModel.impl;

import java.util.Collection;

import metamodels.genericTestModel.A;
import metamodels.genericTestModel.B;
import metamodels.genericTestModel.GenericTestModelPackage;
import metamodels.genericTestModel.X;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>X</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.impl.XImpl#getX_to_a <em>Xto a</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.XImpl#getX_to_b <em>Xto b</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.XImpl#getX_to_x <em>Xto x</em>}</li>
 * </ul>
 *
 * @generated
 */
public class XImpl extends MinimalEObjectImpl.Container implements X {
	/**
	 * The cached value of the '{@link #getX_to_a() <em>Xto a</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX_to_a()
	 * @generated
	 * @ordered
	 */
	protected EList<A> x_to_a;

	/**
	 * The cached value of the '{@link #getX_to_b() <em>Xto b</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX_to_b()
	 * @generated
	 * @ordered
	 */
	protected EList<B> x_to_b;

	/**
	 * The cached value of the '{@link #getX_to_x() <em>Xto x</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX_to_x()
	 * @generated
	 * @ordered
	 */
	protected EList<X> x_to_x;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GenericTestModelPackage.Literals.X;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<A> getX_to_a() {
		if (x_to_a == null) {
			x_to_a = new EObjectResolvingEList<A>(A.class, this, GenericTestModelPackage.X__XTO_A);
		}
		return x_to_a;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<B> getX_to_b() {
		if (x_to_b == null) {
			x_to_b = new EObjectResolvingEList<B>(B.class, this, GenericTestModelPackage.X__XTO_B);
		}
		return x_to_b;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<X> getX_to_x() {
		if (x_to_x == null) {
			x_to_x = new EObjectResolvingEList<X>(X.class, this, GenericTestModelPackage.X__XTO_X);
		}
		return x_to_x;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GenericTestModelPackage.X__XTO_A:
				return getX_to_a();
			case GenericTestModelPackage.X__XTO_B:
				return getX_to_b();
			case GenericTestModelPackage.X__XTO_X:
				return getX_to_x();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GenericTestModelPackage.X__XTO_A:
				getX_to_a().clear();
				getX_to_a().addAll((Collection<? extends A>)newValue);
				return;
			case GenericTestModelPackage.X__XTO_B:
				getX_to_b().clear();
				getX_to_b().addAll((Collection<? extends B>)newValue);
				return;
			case GenericTestModelPackage.X__XTO_X:
				getX_to_x().clear();
				getX_to_x().addAll((Collection<? extends X>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case GenericTestModelPackage.X__XTO_A:
				getX_to_a().clear();
				return;
			case GenericTestModelPackage.X__XTO_B:
				getX_to_b().clear();
				return;
			case GenericTestModelPackage.X__XTO_X:
				getX_to_x().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GenericTestModelPackage.X__XTO_A:
				return x_to_a != null && !x_to_a.isEmpty();
			case GenericTestModelPackage.X__XTO_B:
				return x_to_b != null && !x_to_b.isEmpty();
			case GenericTestModelPackage.X__XTO_X:
				return x_to_x != null && !x_to_x.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //XImpl
