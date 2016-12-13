package at.mhofer.aspsolver.solver;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Falsum;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

public class SATSolver {

	private Propagation propagation;

	private TupleKeyHashMap<Assignment, Literal, Nogood> implicants;

	private TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels;

	private List<Atom> atoms;

	// TODO change datastructure
	private Map<Integer, Literal> guesses = new HashMap<Integer, Literal>();

	// private int currentDL = 0;

	public SATSolver(Propagation propagation, List<Atom> atoms,
			TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels,
			TupleKeyHashMap<Assignment, Literal, Nogood> implicants) {
		this.propagation = propagation;
		this.atoms = atoms;
		this.decisionLevels = decisionLevels;
		this.implicants = implicants;
	}

	/**
	 * @param rules
	 * @return true if the set of rules is satisfiable, false otherwise
	 */
	public boolean solve(List<Nogood> instance, Assignment initialAssignment) {
		return solve(instance, initialAssignment, null, 0);
	}

	private boolean solve(List<Nogood> instance, Assignment initialAssignment, Literal recentlyAssigned,
			int currentDL) {
		Assignment assignment = propagation.propagate(instance, initialAssignment, recentlyAssigned);

		for (Nogood n : instance) {
			if (n.isSatisfiedBy(assignment) && currentDL == 0) {
				return false;
			} else if (n.isSatisfiedBy(assignment) && currentDL > 0) {
				// backtracking
				int k = Collections.max(guesses.keySet());

				for (Literal l : assignment) {
					if (decisionLevels.get(assignment, l) > k && l.isPositive()) {
						assignment.unassign(l);
					}
				}
				Literal l = guesses.get(k + 1);
				Literal alternativeGuess = l.negation();
				assignment.assign(alternativeGuess);
				decisionLevels.put(assignment, alternativeGuess, k);
				implicants.put(assignment, alternativeGuess, new Falsum());
				return solve(instance, assignment, recentlyAssigned, currentDL);
			}
		}

		if (assignment.isComplete(atoms)) {
			return true;
		} else {
			// guess
			Literal guessed = select(assignment);
			currentDL++;
			guesses.put(currentDL, guessed);
			decisionLevels.put(assignment, guessed, currentDL);
			implicants.put(assignment, guessed, new Falsum());
			assignment.assign(guessed);
			return solve(instance, assignment, guessed, currentDL);
		}
	}

	private Literal select(Assignment assignment) {
		for (Atom a : atoms) {
			if (!assignment.isAssigned(a)) {
				return new Literal(a, true);
			}
		}
		return null;
	}
}
