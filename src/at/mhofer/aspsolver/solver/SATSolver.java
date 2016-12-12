package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Nogood;

public class SATSolver {

	private Propagation propagation;

	public SATSolver(Propagation propagation) {
		this.propagation = propagation;
	}

	/**
	 * @param rules
	 * @return true if the set of rules is satisfiable, false otherwise
	 */
	public boolean solve(List<Nogood> instance, Assignment initialAssignment) {
		Assignment assignment = propagation.propagate(instance, initialAssignment, null);

		return true;
	}

}
