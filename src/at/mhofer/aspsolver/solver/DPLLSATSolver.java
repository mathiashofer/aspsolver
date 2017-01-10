package at.mhofer.aspsolver.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.TupleKeyHashMap;

public class DPLLSATSolver implements SATSolver {

	private PropagationFactory propagationFactory;

	private List<Atom> atoms;

	// TODO change datastructure
	private Map<Integer, Literal> guesses = new HashMap<Integer, Literal>();

	public DPLLSATSolver(PropagationFactory propagationFactory, List<Atom> atoms) {
		this.propagationFactory = propagationFactory;
		this.atoms = atoms;
	}

	/**
	 * @param rules
	 * @return true if the set of rules is satisfiable, false otherwise
	 */
	@Override
	public Assignment solve(List<Nogood> instance, Assignment initialAssignment) {
		Propagation propagation = propagationFactory.create(instance);
		TupleKeyHashMap<Assignment, Literal, Nogood> implicants = new TupleKeyHashMap<Assignment, Literal, Nogood>();
		TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels = new TupleKeyHashMap<Assignment, Literal, Integer>();
		return solve(instance, initialAssignment, null, 0, propagation, decisionLevels, implicants);
	}

	@Override
	public List<Assignment> solveAll(List<Nogood> instance, Assignment initialAssignment) {
		List<Assignment> results = new LinkedList<Assignment>();
		List<Nogood> modifiedInstance = new LinkedList<Nogood>(instance);
		Propagation propagation = propagationFactory.create(modifiedInstance);
		TupleKeyHashMap<Assignment, Literal, Nogood> implicants = new TupleKeyHashMap<Assignment, Literal, Nogood>();
		TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels = new TupleKeyHashMap<Assignment, Literal, Integer>();
		Assignment result = null;
		while ((result = solve(modifiedInstance, initialAssignment, null, 0, propagation, decisionLevels, implicants)) != null) {
			// modify instance such that we get a new answer set if there is one
			List<Literal> literals = new ArrayList<Literal>(result.getAssignedLiterals());
			Nogood nogood = new Nogood(literals, false);
			modifiedInstance.add(nogood);

			implicants = new TupleKeyHashMap<Assignment, Literal, Nogood>();
			decisionLevels = new TupleKeyHashMap<Assignment, Literal, Integer>();
			propagation = propagationFactory.create(modifiedInstance);
			results.add(result);
		}

		return results;
	}

	private Assignment solve(List<Nogood> instance, Assignment initialAssignment, Literal recentlyAssigned,
			int currentDL, Propagation propagation, TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels, TupleKeyHashMap<Assignment, Literal, Nogood> implicants) {
		Assignment assignment = propagation.propagate(instance, initialAssignment, recentlyAssigned, decisionLevels, implicants);

		for (Nogood n : instance) {
			if (n.isSatisfiedBy(assignment) && currentDL == 0) {
				return null;
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
				implicants.put(assignment, alternativeGuess, null);
				return solve(instance, assignment, recentlyAssigned, currentDL - 1, propagation, decisionLevels, implicants);
			}
		}

		if (assignment.isComplete(atoms)) {
			return assignment;
		} else {
			// guess
			Literal guessed = select(assignment);
			currentDL++;
			guesses.put(currentDL, guessed);
			decisionLevels.put(assignment, guessed, currentDL);
			implicants.put(assignment, guessed, null);
			assignment.assign(guessed);
			return solve(instance, assignment, guessed, currentDL, propagation, decisionLevels, implicants);
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
