package recombination.crossoverpoint.inference;

import model.CoSpan;
import model.Span;

public interface IInferenceStrategy {

	/**
	 * Calculates a crossover point for the splits of two solutions. The crossover point has to be returned as a span
	 * where the codomains are identical to the pullbacks (split points) of the respective splits.
	 * 
	 * @param firstSplit  split of the first solution
	 * @param secondSplit split of the second solution
	 * @return a span representing the crossover point of the splits
	 */
	public Span inferCrossoverPoint(CoSpan firstSplit, CoSpan secondSplit);

}
