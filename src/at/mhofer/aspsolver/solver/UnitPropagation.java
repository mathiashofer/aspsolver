package at.mhofer.aspsolver.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;

public class UnitPropagation implements Propagation {

	private Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist;

	private Map<Literal, List<Nogood>> literalWatchlist;

	public UnitPropagation(Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist,
			Map<Literal, List<Nogood>> literalWatchlist) {
		super();
		this.nogoodWatchlist = nogoodWatchlist;
		this.literalWatchlist = literalWatchlist;
	}

	@Override
	public Assignment propagate(List<Nogood> instance, Assignment assignment, Literal recentlyAssigned,
			HashMap<Literal, Integer> decisionLevels, HashMap<Literal, Nogood> implicants) {
		List<Literal> changedLiterals = new LinkedList<Literal>();
		changedLiterals.add(recentlyAssigned);
		Assignment last = assignment;
		Assignment extended = null;
		do {
			Literal changedLiteral = changedLiterals.remove(0);
			extended = unitPropagationStep(instance, assignment, nogoodWatchlist, literalWatchlist, changedLiteral,
					decisionLevels, implicants);
			changedLiterals.addAll(extended.difference(last));
			last = extended;
		} while (!changedLiterals.isEmpty());
		return last;
	}

	private Assignment unitPropagationStep(List<Nogood> instance, Assignment assignment,
			Map<Nogood, Tuple<Literal, Literal>> wn, Map<Literal, List<Nogood>> wl, Literal recentlyAssigned,
			HashMap<Literal, Integer> decisionLevels, HashMap<Literal, Nogood> implicants) {
		Assignment extended = assignment;
		if (wl.get(recentlyAssigned) != null) {
			// used to avoid a ConcurrentModificationException
			List<Nogood> tempWl = new ArrayList<Nogood>(wl.get(recentlyAssigned));

			for (Nogood nogood : tempWl) {
				Tuple<Literal, Literal> watchedLiterals = wn.get(nogood);
				Literal watchedLiteral = watchedLiterals.getValue1() == recentlyAssigned ? watchedLiterals.getValue2()
						: watchedLiterals.getValue1();

				if (nogood.isFalsifiedBy(extended)) {
					// Nogood already falsified
					return extended;
				}

				for (Nogood n : instance) {
					if (n.isSatisfiedBy(extended)) {
						// Nogood already satisfied
						return extended;
					}
				}

				Literal unassignedLiteral = nogood.getFirstUnassignedLiteral(assignment, watchedLiteral);
				if (unassignedLiteral != null) {
					// modify watchlists
					wl.get(recentlyAssigned).remove(nogood);
					wl.get(unassignedLiteral).add(nogood);
					wn.put(nogood, new Tuple<Literal, Literal>(watchedLiteral, unassignedLiteral));
				} else {
					Literal negation = watchedLiteral.negation();
					extended.assign(negation);
					implicants.put(negation, nogood);
					// calculate max decision level
					int maxDL = 0;
					for (Literal l : nogood) {
						if (!l.equals(watchedLiteral)) {
							Integer dl = decisionLevels.get(l);
							if (dl != null && dl > maxDL) {
								maxDL = dl;
							}
						}
					}
					decisionLevels.put(negation, maxDL);
				}
			}
		}

		return extended;
	}
}
