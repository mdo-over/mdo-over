/**
 */
package metamodels.genericTestModel.impl;

import metamodels.genericTestModel.A;
import metamodels.genericTestModel.B;
import metamodels.genericTestModel.GenericTestModelPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>B</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.impl.BImpl#getB_to_a <em>Bto a</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.BImpl#getB_opp_a <em>Bopp a</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.BImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BImpl extends MinimalEObjectImpl.Container implements B {
	/**
	 * The cached value of the '{@link #getB_to_a() <em>Bto a</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getB_to_a()
	 * @generated
	 * @ordered
	 */
	protected A b_to_a;

	/**
	 * The cached value of the '{@link #getB_opp_a() <em>Bopp a</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getB_opp_a()
	 * @generated
	 * @ordered
	 */
	protected A b_opp_a;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GenericTestModelPackage.Literals.B;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public A getB_to_a() {
		if (b_to_a != null && b_to_a.eIsProxy()) {
			InternalEObject oldB_to_a = (InternalEObject)b_to_a;
			b_to_a = (A)eResolveProxy(oldB_to_a);
			if (b_to_a != oldB_to_a) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GenericTestModelPackage.B__BTO_A, oldB_to_a, b_to_a));
			}
		}
		return b_to_a;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public A basicGetB_to_a() {
		return b_to_a;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setB_to_a(A newB_to_a) {
		A oldB_to_a = b_to_a;
		b_to_a = newB_to_a;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.B__BTO_A, oldB_to_a, b_to_a));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public A getB_opp_a() {
		if (b_opp_a != null && b_opp_a.eIsProxy()) {
			InternalEObject oldB_opp_a = (InternalEObject)b_opp_a;
			b_opp_a = (A)eResolveProxy(oldB_opp_a);
			if (b_opp_a != oldB_opp_a) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GenericTestModelPackage.B__BOPP_A, oldB_opp_a, b_opp_a));
			}
		}
		return b_opp_a;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public A basicGetB_opp_a() {
		return b_opp_a;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetB_opp_a(A newB_opp_a, NotificationChain msgs) {
		A oldB_opp_a = b_opp_a;
		b_opp_a = newB_opp_a;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.B__BOPP_A, oldB_opp_a, newB_opp_a);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setB_opp_a(A newB_opp_a) {
		if (newB_opp_a != b_opp_a) {
			NotificationChain msgs = null;
			if (b_opp_a != null)
				msgs = ((InternalEObject)b_opp_a).eInverseRemove(this, GenericTestModelPackage.A__AOPP_B, A.class, msgs);
			if (newB_opp_a != null)
				msgs = ((InternalEObject)newB_opp_a).eInverseAdd(this, GenericTestModelPackage.A__AOPP_B, A.class, msgs);
			msgs = basicSetB_opp_a(newB_opp_a, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.B__BOPP_A, newB_opp_a, newB_opp_a));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.B__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GenericTestModelPackage.B__BOPP_A:
				if (b_opp_a != null)
					msgs = ((InternalEObject)b_opp_a).eInverseRemove(this, GenericTestModelPackage.A__AOPP_B, A.class, msgs);
				return basicSetB_opp_a((A)otherEnd, msgs);
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
			case GenericTestModelPackage.B__BOPP_A:
				return basicSetB_opp_a(null, msgs);
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
			case GenericTestModelPackage.B__BTO_A:
				if (resolve) return getB_to_a();
				return basicGetB_to_a();
			case GenericTestModelPackage.B__BOPP_A:
				if (resolve) return getB_opp_a();
				return basicGetB_opp_a();
			case GenericTestModelPackage.B__NAME:
				return getName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GenericTestModelPackage.B__BTO_A:
				setB_to_a((A)newValue);
				return;
			case GenericTestModelPackage.B__BOPP_A:
				setB_opp_a((A)newValue);
				return;
			case GenericTestModelPackage.B__NAME:
				setName((String)newValue);
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
			case GenericTestModelPackage.B__BTO_A:
				setB_to_a((A)null);
				return;
			case GenericTestModelPackage.B__BOPP_A:
				setB_opp_a((A)null);
				return;
			case GenericTestModelPackage.B__NAME:
				setName(NAME_EDEFAULT);
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
			case GenericTestModelPackage.B__BTO_A:
				return b_to_a != null;
			case GenericTestModelPackage.B__BOPP_A:
				return b_opp_a != null;
			case GenericTestModelPackage.B__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //BImpl
