package at.mhofer.aspsolver.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

public class UnitPropagation implements Propagation{
	
	private Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist;
	
	private Map<Literal, List<Nogood>> literalWatchlist;
	
	//TODO abstract dl and impl
	private TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels;
	
	private TupleKeyHashMap<Assignment, Literal, Nogood> implicants;
	
	public UnitPropagation(Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist, Map<Literal, List<Nogood>> literalWatchlist, TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels, TupleKeyHashMap<Assignment, Literal, Nogood> implicants) {
		super();
		this.nogoodWatchlist = nogoodWatchlist;
		this.literalWatchlist = literalWatchlist;
		this.decisionLevels = decisionLevels;
		this.implicants = implicants;
	}

	@Override
	public Assignment propagate(List<Nogood> instance, Assignment assignment, Literal recentlyAssigned) {
		List<Literal> changedLiterals = new LinkedList<Literal>();
		changedLiterals.add(recentlyAssigned);
		Assignment last = assignment;
		Assignment extended = null;
		do {
			Literal changedLiteral = changedLiterals.remove(0);
			extended = unitPropagationStep(instance, assignment, nogoodWatchlist, literalWatchlist, changedLiteral);
			changedLiterals.addAll(extended.difference(last));
			last = extended;
		} while (!changedLiterals.isEmpty());
		return last;
	}

	private Assignment unitPropagationStep(List<Nogood> instance, Assignment assignment,
			Map<Nogood, Tuple<Literal, Literal>> wn, Map<Literal, List<Nogood>> wl, Literal recentlyAssigned) {
		Assignment extended = new Assignment(assignment);
		for (Nogood nogood : wl.get(recentlyAssigned)) {
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
			}
			else {
				Literal negation = watchedLiteral.createNegation();
				extended.assign(negation);
				implicants.put(extended, negation, nogood);
				//calculate max decision level
				int maxDL = 0;
				for (Literal l : nogood) {
					if (!l.equals(watchedLiteral)){
						Integer dl = decisionLevels.get(extended, l);
						if (dl != null && dl > maxDL) {
							maxDL = dl;
						}
					}
				}
				decisionLevels.put(extended, negation, maxDL);
			}
		}

		return extended;
	}
}
