/**
 */
package metamodels.genericTestModel.impl;

import java.util.Collection;

import metamodels.genericTestModel.A;
import metamodels.genericTestModel.B;
import metamodels.genericTestModel.GenericTestModelPackage;
import metamodels.genericTestModel.Root;
import metamodels.genericTestModel.X;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metamodels.genericTestModel.impl.AImpl#getX_cont <em>Xcont</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.AImpl#getValue <em>Value</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.AImpl#getA_opp_b <em>Aopp b</em>}</li>
 *   <li>{@link metamodels.genericTestModel.impl.AImpl#getA_opp_cont <em>Aopp cont</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AImpl extends MinimalEObjectImpl.Container implements A {
	/**
	 * The cached value of the '{@link #getX_cont() <em>Xcont</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX_cont()
	 * @generated
	 * @ordered
	 */
	protected EList<X> x_cont;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final int VALUE_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected int value = VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getA_opp_b() <em>Aopp b</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getA_opp_b()
	 * @generated
	 * @ordered
	 */
	protected B a_opp_b;

	/**
	 * The cached value of the '{@link #getA_opp_cont() <em>Aopp cont</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getA_opp_cont()
	 * @generated
	 * @ordered
	 */
	protected Root a_opp_cont;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GenericTestModelPackage.Literals.A;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<X> getX_cont() {
		if (x_cont == null) {
			x_cont = new EObjectContainmentEList<X>(X.class, this, GenericTestModelPackage.A__XCONT);
		}
		return x_cont;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValue(int newValue) {
		int oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.A__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public B getA_opp_b() {
		if (a_opp_b != null && a_opp_b.eIsProxy()) {
			InternalEObject oldA_opp_b = (InternalEObject)a_opp_b;
			a_opp_b = (B)eResolveProxy(oldA_opp_b);
			if (a_opp_b != oldA_opp_b) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GenericTestModelPackage.A__AOPP_B, oldA_opp_b, a_opp_b));
			}
		}
		return a_opp_b;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public B basicGetA_opp_b() {
		return a_opp_b;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetA_opp_b(B newA_opp_b, NotificationChain msgs) {
		B oldA_opp_b = a_opp_b;
		a_opp_b = newA_opp_b;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.A__AOPP_B, oldA_opp_b, newA_opp_b);
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
	public void setA_opp_b(B newA_opp_b) {
		if (newA_opp_b != a_opp_b) {
			NotificationChain msgs = null;
			if (a_opp_b != null)
				msgs = ((InternalEObject)a_opp_b).eInverseRemove(this, GenericTestModelPackage.B__BOPP_A, B.class, msgs);
			if (newA_opp_b != null)
				msgs = ((InternalEObject)newA_opp_b).eInverseAdd(this, GenericTestModelPackage.B__BOPP_A, B.class, msgs);
			msgs = basicSetA_opp_b(newA_opp_b, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.A__AOPP_B, newA_opp_b, newA_opp_b));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Root getA_opp_cont() {
		if (a_opp_cont != null && a_opp_cont.eIsProxy()) {
			InternalEObject oldA_opp_cont = (InternalEObject)a_opp_cont;
			a_opp_cont = (Root)eResolveProxy(oldA_opp_cont);
			if (a_opp_cont != oldA_opp_cont) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GenericTestModelPackage.A__AOPP_CONT, oldA_opp_cont, a_opp_cont));
			}
		}
		return a_opp_cont;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Root basicGetA_opp_cont() {
		return a_opp_cont;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setA_opp_cont(Root newA_opp_cont) {
		Root oldA_opp_cont = a_opp_cont;
		a_opp_cont = newA_opp_cont;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GenericTestModelPackage.A__AOPP_CONT, oldA_opp_cont, a_opp_cont));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GenericTestModelPackage.A__AOPP_B:
				if (a_opp_b != null)
					msgs = ((InternalEObject)a_opp_b).eInverseRemove(this, GenericTestModelPackage.B__BOPP_A, B.class, msgs);
				return basicSetA_opp_b((B)otherEnd, msgs);
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
			case GenericTestModelPackage.A__XCONT:
				return ((InternalEList<?>)getX_cont()).basicRemove(otherEnd, msgs);
			case GenericTestModelPackage.A__AOPP_B:
				return basicSetA_opp_b(null, msgs);
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
			case GenericTestModelPackage.A__XCONT:
				return getX_cont();
			case GenericTestModelPackage.A__VALUE:
				return getValue();
			case GenericTestModelPackage.A__AOPP_B:
				if (resolve) return getA_opp_b();
				return basicGetA_opp_b();
			case GenericTestModelPackage.A__AOPP_CONT:
				if (resolve) return getA_opp_cont();
				return basicGetA_opp_cont();
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
			case GenericTestModelPackage.A__XCONT:
				getX_cont().clear();
				getX_cont().addAll((Collection<? extends X>)newValue);
				return;
			case GenericTestModelPackage.A__VALUE:
				setValue((Integer)newValue);
				return;
			case GenericTestModelPackage.A__AOPP_B:
				setA_opp_b((B)newValue);
				return;
			case GenericTestModelPackage.A__AOPP_CONT:
				setA_opp_cont((Root)newValue);
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
			case GenericTestModelPackage.A__XCONT:
				getX_cont().clear();
				return;
			case GenericTestModelPackage.A__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case GenericTestModelPackage.A__AOPP_B:
				setA_opp_b((B)null);
				return;
			case GenericTestModelPackage.A__AOPP_CONT:
				setA_opp_cont((Root)null);
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
			case GenericTestModelPackage.A__XCONT:
				return x_cont != null && !x_cont.isEmpty();
			case GenericTestModelPackage.A__VALUE:
				return value != VALUE_EDEFAULT;
			case GenericTestModelPackage.A__AOPP_B:
				return a_opp_b != null;
			case GenericTestModelPackage.A__AOPP_CONT:
				return a_opp_cont != null;
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
		result.append(" (value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} //AImpl
