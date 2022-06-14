package recombination;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import model.CoSpan;

public interface IRecombinationStrategy {
	
	/**
	 * Recombines two parent solutions (represented by splits) and creates two or more EMF models as offspring.
	 * 
	 * @param firstSplit the first parent represented by a split 
	 * @param secondSplit the second parent represented by a split
	 * @return a list containing the roots of the offspring models created as recombinations of the input splits
	 */
	public List<EObject> recombine(CoSpan firstSplit, CoSpan secondSplit);
}
