package de.uni_marburg.mdo_over.model;

import java.util.HashMap;
import java.util.Map;

import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraphElement;

/**
 * A {@link ModelGraphMapping} represents a mapping of elements between two {@link ModelGraph graphs}. An element of one
 * graph can only be mapped to at most one element of the other graph. While there is made a distinction between the
 * origin and the image graph of such a mapping, actually, the mapping is bi-directional.
 * {@link ModelGraphMapping#getImage(ModelGraphElement) Retrieving the image} of a mapped element of the origin graph as
 * well as {@link ModelGraphMapping#getOrigin(ModelGraphElement) retrieving the origin} of an element of the image graph
 * is possible.
 * <p>
 * Note: While a mapping is forcibly left- and right-unique no assumptions can be made about left- or right-totality.
 * 
 * @author S. John
 *
 */
public class ModelGraphMapping {
	private final ModelGraph originGraph;
	private final ModelGraph imageGraph;
	private final Map<ModelGraphElement, ModelGraphElement> originToImageMap = new HashMap<>();
	private final Map<ModelGraphElement, ModelGraphElement> imageToOriginMap = new HashMap<>();

	public ModelGraphMapping(ModelGraph originGraph, ModelGraph imageGraph) {
		this.originGraph = originGraph;
		this.imageGraph = imageGraph;
	}

	/**
	 * Adds a mapping from origin to image and vice versa. Both origin and image need to be part of the respective
	 * graphs and may not already be mapped. If a mapping between elements needs to be substituted
	 * {@link ModelGraphMapping#removeMapping(ModelGraphElement, ModelGraphElement)} needs to be explicitly called,
	 * first.
	 * <p>
	 * <i>Note:</i> Changes to the underlying graphs are not automatically reflected by the mappings. It's the users
	 * responsibility to keep the graphs and the mappings aligned.
	 * 
	 * @param origin an element of the {@link #originGraph} which should be mapped
	 * @param image  an element of the {@link #imageGraph} which should be mapped
	 * @exception IllegalArgumentException thrown if either origin or image is not part of the respective graph or it is
	 *                                     already mapped
	 */
	public void addMapping(ModelGraphElement origin, ModelGraphElement image) throws IllegalArgumentException {
		if (origin == null || image == null) {
			throw new IllegalArgumentException("Neither origin nor image may be null.");
		}
		if (!originGraph.getElements().contains(origin) || !imageGraph.getElements().contains(image)) {
			throw new IllegalArgumentException("Either origin or image is not contained in the respective graph.");
		}
		if (originToImageMap.containsKey(origin) || imageToOriginMap.containsKey(image)) {
			throw new IllegalArgumentException("Neither origin nor image may already be mapped.");
		}
		originToImageMap.put(origin, image);
		imageToOriginMap.put(image, origin);
	}

	/**
	 * Removes the mapping between origin and image if this mapping exists.
	 * <p>
	 * <i>Note:</i> Changes to the underlying graphs are not automatically reflected by the mappings. It's the users
	 * responsibility to keep the graphs and the mappings aligned.
	 * 
	 * @param origin the origin of the mapping
	 * @param image  the image the origin is mapped to
	 * @exception IllegalArgumentException thrown if origin is not mapped to image
	 */
	public void removeMapping(ModelGraphElement origin, ModelGraphElement image) throws IllegalArgumentException {
		if (origin == null || image == null) {
			throw new IllegalArgumentException("Neither origin nor image may be null.");
		}
		if (originToImageMap.get(origin) != image) {
			throw new IllegalArgumentException("Origin is not mapped to image.");
		}
		originToImageMap.remove(origin);
		imageToOriginMap.remove(image);
	}

	/**
	 * Returns the image of <code>element</code> if <code>element</code> is the origin of a mapping.
	 * 
	 * @param element the element for which an image is looked up
	 * @return the image of <code>element</code> or <code>null</code> if the element is not the origin of a mapping
	 */
	public ModelGraphElement getImage(ModelGraphElement element) {
		return originToImageMap.get(element);
	}

	/**
	 * Returns the origin of <code>element</code> if <code>element</code> is the image of a mapping.
	 * 
	 * @param element the element for which an origin is looked up
	 * @return the origin of <code>element</code> or <code>null</code> if the element is not the image of a mapping
	 */
	public ModelGraphElement getOrigin(ModelGraphElement element) {
		return imageToOriginMap.get(element);
	}

	/**
	 * Get the graph used as origin for this mapping.
	 * 
	 * @return origin graph
	 */
	public ModelGraph getOriginGraph() {
		return originGraph;
	}

	/**
	 * Get the graph used as image in this mapping.
	 * 
	 * @return image graph
	 */
	public ModelGraph getImageGraph() {
		return imageGraph;
	}
}
