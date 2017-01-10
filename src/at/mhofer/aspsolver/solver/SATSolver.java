package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Nogood;

public interface SATSolver {

	public Assignment solve(List<Nogood> instance, Assignment initialAssignment);
	
	public List<Assignment> solveAll(List<Nogood> instance, Assignment initialAssignment);
}
