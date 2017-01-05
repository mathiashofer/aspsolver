package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;

public interface ConflictAnalysis {

	public Tuple<Nogood, Integer> analyse(Nogood conflictingNogood, List<Nogood> instance, Assignment assignment);
	
}
