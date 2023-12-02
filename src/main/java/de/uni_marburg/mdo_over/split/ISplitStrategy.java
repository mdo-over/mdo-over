package de.uni_marburg.mdo_over.split;

import de.uni_marburg.mdo_over.model.CoSpan;
import de.uni_marburg.mdo_over.model.modelgraph.ModelGraph;

public interface ISplitStrategy {

	/**
	 * Splits the given graph returning a {@link CoSpan} containing the split parts with mappings to the goven graph. 
	 * @param graph graph to split
	 * @return cospan containing split parts and mappings
	 */
	public CoSpan split(ModelGraph graph);
}
