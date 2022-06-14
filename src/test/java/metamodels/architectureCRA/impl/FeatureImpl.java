/**
 */
package metamodels.architectureCRA.impl;

import java.util.Collection;

import metamodels.architectureCRA.ArchitectureCRAPackage;
import metamodels.architectureCRA.Feature;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodels.architectureCRA.impl.FeatureImpl#getIsEncapsulatedBy <em>Is Encapsulated By</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class FeatureImpl extends NamedElementImpl implements Feature {
	/**
	 * The cached value of the '{@link #getIsEncapsulatedBy() <em>Is Encapsulated By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsEncapsulatedBy()
	 * @generated
	 * @ordered
	 */
	protected EList<metamodels.architectureCRA.Class> isEncapsulatedBy;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArchitectureCRAPackage.Literals.FEATURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<metamodels.architectureCRA.Class> getIsEncapsulatedBy() {
		if (isEncapsulatedBy == null) {
			isEncapsulatedBy = new EObjectWithInverseResolvingEList.ManyInverse<metamodels.architectureCRA.Class>(metamodels.architectureCRA.Class.class, this, ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY, ArchitectureCRAPackage.CLASS__ENCAPSULATES);
		}
		return isEncapsulatedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getIsEncapsulatedBy()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY:
				return ((InternalEList<?>)getIsEncapsulatedBy()).basicRemove(otherEnd, msgs);
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
			case ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY:
				return getIsEncapsulatedBy();
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
			case ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY:
				getIsEncapsulatedBy().clear();
				getIsEncapsulatedBy().addAll((Collection<? extends metamodels.architectureCRA.Class>)newValue);
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
			case ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY:
				getIsEncapsulatedBy().clear();
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
			case ArchitectureCRAPackage.FEATURE__IS_ENCAPSULATED_BY:
				return isEncapsulatedBy != null && !isEncapsulatedBy.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //FeatureImpl
