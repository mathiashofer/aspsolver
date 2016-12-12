package at.mhofer.aspsolver.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;

public class UnitPropagation implements Propagation{

	@Override
	public Assignment propagate(List<Nogood> instance, Assignment assignment, Literal recentlyAssigned) {
		List<Literal> changedLiterals = new LinkedList<Literal>();
		changedLiterals.add(recentlyAssigned);
		do {
			
		} while (!changedLiterals.isEmpty());
		return null;
	}

	private Assignment unitPropagationStep(List<Nogood> instance, Assignment assignment,
			Map<Nogood, Tuple<Literal, Literal>> wn, Map<Literal, List<Nogood>> wl, Literal recentlyAssigned) {

		for (Nogood nogood : wl.get(recentlyAssigned)) {
			Tuple<Literal, Literal> watchedLiterals = wn.get(nogood);
			Literal watchedLiteral = watchedLiterals.getValue1() == recentlyAssigned ? watchedLiterals.getValue2()
					: watchedLiterals.getValue1();

			if (nogood.isFalsifiedBy(assignment)) {
				// Nogood already falsified
				return assignment;
			}

			for (Nogood n : instance) {
				if (n.isSatisfiedBy(assignment)) {
					// Nogood already satisfied
					return assignment;
				}
			}
			
			Literal unassignedLiteral = nogood.getFirstUnassignedLiteral(assignment, watchedLiteral);
			if (unassignedLiteral != null) {
				wl.get(recentlyAssigned).remove(nogood);
				wl.get(unassignedLiteral).add(nogood);
				wn.put(nogood, new Tuple<Literal, Literal>(watchedLiteral, unassignedLiteral));
			}
			else {
				assignment.assign(watchedLiteral.createNegation());
				//TODO adjust implicant and decision level
			}
		}

		return null;
	}
}
