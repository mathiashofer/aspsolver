package at.mhofer.aspsolver.solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

public class CDNLSATSolver implements SATSolver{

	private Propagation propagation;

	private ConflictAnalysis conflictAnalysis;

	private TupleKeyHashMap<Assignment, Literal, Nogood> implicants;

	private TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels;

	private List<Atom> atoms;

	// TODO change datastructure
	private Map<Integer, Literal> guesses = new HashMap<Integer, Literal>();

	public CDNLSATSolver(Propagation propagation, ConflictAnalysis conflictAnalysis, List<Atom> atoms,
			TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels,
			TupleKeyHashMap<Assignment, Literal, Nogood> implicants) {
		this.propagation = propagation;
		this.conflictAnalysis = conflictAnalysis;
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
				Tuple<Nogood, Integer> analysisResult = conflictAnalysis.analyse(n, instance, assignment);
				Nogood learnedNogood = analysisResult.getValue1();
				int backtrackDL = analysisResult.getValue2();
				instance.add(learnedNogood);

				for (Literal l : decisionLevels.getKeys(assignment)) {
					if (decisionLevels.get(assignment, l) > backtrackDL && l.isPositive()) {
						assignment.unassign(l);
					}
				}

				return solve(instance, assignment, recentlyAssigned, backtrackDL);
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
			implicants.put(assignment, guessed, null);
			assignment.assign(guessed);
			return solve(instance, assignment, guessed, currentDL);
		}
	}

	/**
	 * 
	 * @param assignment
	 * @return
	 */
	private Literal select(Assignment assignment) {
		for (Atom a : atoms) {
			if (!assignment.isAssigned(a)) {
				return new Literal(a, true);
			}
		}
		return null;
	}
}
