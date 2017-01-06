package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Nogood;

public interface SATSolver {

	public boolean solve(List<Nogood> instance, Assignment initialAssignment);
	
}
