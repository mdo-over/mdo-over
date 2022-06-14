/**
 */
package metamodels.architectureCRA;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodels.architectureCRA.Feature#getIsEncapsulatedBy <em>Is Encapsulated By</em>}</li>
 * </ul>
 *
 * @see metamodels.architectureCRA.ArchitectureCRAPackage#getFeature()
 * @model abstract="true"
 * @generated
 */
public interface Feature extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Is Encapsulated By</b></em>' reference list.
	 * The list contents are of type {@link metamodels.architectureCRA.Class}.
	 * It is bidirectional and its opposite is '{@link metamodels.architectureCRA.Class#getEncapsulates <em>Encapsulates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Encapsulated By</em>' reference list.
	 * @see metamodels.architectureCRA.ArchitectureCRAPackage#getFeature_IsEncapsulatedBy()
	 * @see metamodels.architectureCRA.Class#getEncapsulates
	 * @model opposite="encapsulates"
	 * @generated
	 */
	EList<metamodels.architectureCRA.Class> getIsEncapsulatedBy();

} // Feature
