/**
 */
package metamodels.architectureCRA;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metamodels.architectureCRA.Class#getEncapsulates <em>Encapsulates</em>}</li>
 * </ul>
 *
 * @see metamodels.architectureCRA.ArchitectureCRAPackage#getClass_()
 * @model
 * @generated
 */
public interface Class extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Encapsulates</b></em>' reference list.
	 * The list contents are of type {@link metamodels.architectureCRA.Feature}.
	 * It is bidirectional and its opposite is '{@link metamodels.architectureCRA.Feature#getIsEncapsulatedBy <em>Is Encapsulated By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Encapsulates</em>' reference list.
	 * @see metamodels.architectureCRA.ArchitectureCRAPackage#getClass_Encapsulates()
	 * @see metamodels.architectureCRA.Feature#getIsEncapsulatedBy
	 * @model opposite="isEncapsulatedBy"
	 * @generated
	 */
	EList<Feature> getEncapsulates();

} // Class
