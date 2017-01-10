package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

/**
 * A deterministic propagation algorithm.
 * 
 * @author Mathias
 *
 */
public interface Propagation {

	/**
	 * Returns a new assignment and must not modify the given one!
	 * 
	 * @param instance
	 * @param assignment
	 * @param recentlyAssigned
	 * @return extended assignment
	 */
	public Assignment propagate(List<Nogood> instance, Assignment assignment, Literal recentlyAssigned, TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels, TupleKeyHashMap<Assignment, Literal, Nogood> implicants);

}
