package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

public class FirstUIPConflictAnalysis implements ConflictAnalysis {

	@Override
	public Tuple<Nogood, Integer> analyse(Nogood conflictingNogood, List<Nogood> instance, Assignment assignment, TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels,
			TupleKeyHashMap<Assignment, Literal, Nogood> implicants) {
		int newDL = -1;

		int maxDL = 0;
		for (Literal l : assignment.getAssignedLiterals()) {
			int dl = decisionLevels.get(assignment, l);
			if (dl > maxDL) {
				maxDL = dl;
			}
		}

		// is used for a faster check if there are still two distinct
		// literals available with dl(A,l1) == dl(A,l2) == maxDL
		List<Literal> literals = conflictingNogood.getLiterals();
		List<Literal> learned = conflictingNogood.getLiterals();
		while (!literals.isEmpty() && literals.size() >= 2) {
			Literal l1 = literals.get(0);
			Literal l2 = literals.get(1);
			if (decisionLevels.get(assignment, l1) != maxDL) {
				literals.remove(0);
				l1 = null;
			}
			if (decisionLevels.get(assignment, l2) != maxDL) {
				literals.remove(1);
				l2 = null;
			}

			if (l1 != null && l2 != null) {
				// here holds dl(A,l1) == dl(A,l2) == maxDL

				for (Literal l : literals) {
					Nogood implicant = implicants.get(assignment, l);
					if (decisionLevels.get(assignment, l) == maxDL && implicant != null) {
						learned.remove(l);
						learned.addAll(implicant.getLiterals());
						learned.remove(l.negation());

						// contains equal or less literals than "learned",
						// since all with dl != maxDL get removed
						literals.remove(l);
						literals.addAll(implicant.getLiterals());
						literals.remove(l.negation());
					}
				}
			}
		}

		int learnedMaxDL = 0;
		for (Literal l : learned) {
			int dl = decisionLevels.get(assignment, l);
			if (dl > learnedMaxDL) {
				learnedMaxDL = dl;
			}
			if (dl > newDL && dl < learnedMaxDL) {
				// find the second highest dl
				newDL = dl;
			}
		}

		return new Tuple<Nogood, Integer>(new Nogood(learned), newDL);
	}

}
