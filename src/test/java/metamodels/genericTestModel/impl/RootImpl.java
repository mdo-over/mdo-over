/**
 */
package metamodels.genericTestModel.impl;

import java.util.Collection;

import metamodels.genericTestModel.A;
import metamodels.genericTestModel.B;
import metamodels.genericTestModel.GenericTestModelPackage;
import metamodels.genericTestModel.Root;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.impl.RootImpl#getA_cont <em>Acont</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.RootImpl#getB_cont <em>Bcont</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RootImpl extends MinimalEObjectImpl.Container implements Root {
	/**
	 * The cached value of the '{@link #getA_cont() <em>Acont</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getA_cont()
	 * @generated
	 * @ordered
	 */
	protected EList<A> a_cont;

	/**
	 * The cached value of the '{@link #getB_cont() <em>Bcont</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getB_cont()
	 * @generated
	 * @ordered
	 */
	protected EList<B> b_cont;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GenericTestModelPackage.Literals.ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<A> getA_cont() {
		if (a_cont == null) {
			a_cont = new EObjectContainmentEList<A>(A.class, this, GenericTestModelPackage.ROOT__ACONT);
		}
		return a_cont;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<B> getB_cont() {
		if (b_cont == null) {
			b_cont = new EObjectContainmentEList<B>(B.class, this, GenericTestModelPackage.ROOT__BCONT);
		}
		return b_cont;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GenericTestModelPackage.ROOT__ACONT:
				return ((InternalEList<?>)getA_cont()).basicRemove(otherEnd, msgs);
			case GenericTestModelPackage.ROOT__BCONT:
				return ((InternalEList<?>)getB_cont()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GenericTestModelPackage.ROOT__ACONT:
				return getA_cont();
			case GenericTestModelPackage.ROOT__BCONT:
				return getB_cont();
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
			case GenericTestModelPackage.ROOT__ACONT:
				getA_cont().clear();
				getA_cont().addAll((Collection<? extends A>)newValue);
				return;
			case GenericTestModelPackage.ROOT__BCONT:
				getB_cont().clear();
				getB_cont().addAll((Collection<? extends B>)newValue);
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
			case GenericTestModelPackage.ROOT__ACONT:
				getA_cont().clear();
				return;
			case GenericTestModelPackage.ROOT__BCONT:
				getB_cont().clear();
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
			case GenericTestModelPackage.ROOT__ACONT:
				return a_cont != null && !a_cont.isEmpty();
			case GenericTestModelPackage.ROOT__BCONT:
				return b_cont != null && !b_cont.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RootImpl
