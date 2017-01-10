package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

public interface ConflictAnalysis {

	public Tuple<Nogood, Integer> analyse(Nogood conflictingNogood, List<Nogood> instance, Assignment assignment, TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels,
			TupleKeyHashMap<Assignment, Literal, Nogood> implicants);
	
}
