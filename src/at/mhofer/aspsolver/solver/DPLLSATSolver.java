package at.mhofer.aspsolver.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;

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
		HashMap<Literal, Nogood> implicants = new HashMap<Literal, Nogood>();
		HashMap<Literal, Integer> decisionLevels = new HashMap<Literal, Integer>();
		return solve(instance, new Assignment(initialAssignment), null, 0, propagation, decisionLevels, implicants);
	}

	@Override
	public List<Assignment> solveAll(List<Nogood> instance, Assignment initialAssignment) {
		List<Assignment> results = new LinkedList<Assignment>();
		List<Nogood> modifiedInstance = new LinkedList<Nogood>(instance);
		Propagation propagation = propagationFactory.create(modifiedInstance);
		HashMap<Literal, Nogood> implicants = new HashMap<Literal, Nogood>();
		HashMap<Literal, Integer> decisionLevels = new HashMap<Literal, Integer>();
		Assignment result = null;
		while ((result = solve(modifiedInstance, new Assignment(initialAssignment), null, 0, propagation, decisionLevels, implicants)) != null) {
			// modify instance such that we get a new answer set if there is one
			List<Literal> literals = new ArrayList<Literal>();
			for (Literal l : result) {
				if (l.getAtom().getId() > 0) {
					literals.add(l);
				}
			}
			Nogood nogood = new Nogood(literals, false);
			modifiedInstance.add(nogood);

			implicants = new HashMap<Literal, Nogood>();
			decisionLevels = new HashMap<Literal, Integer>();
			propagation = propagationFactory.create(modifiedInstance);
			results.add(result);
		}

		return results;
	}

	private Assignment solve(List<Nogood> instance, Assignment initialAssignment, Literal recentlyAssigned,
			int currentDL, Propagation propagation, HashMap<Literal, Integer> decisionLevels, HashMap< Literal, Nogood> implicants) {
		Assignment assignment = propagation.propagate(instance, initialAssignment, recentlyAssigned, decisionLevels, implicants);

		for (Nogood n : instance) {
			if (n.isSatisfiedBy(assignment) && currentDL == 0) {
				return null;
			} else if (n.isSatisfiedBy(assignment) && currentDL > 0) {
				// backtracking
				int k = 1;
				for (Integer i : guesses.keySet()) {
					if (1 <= i && i <= currentDL && i > k) {
						k = i;
					}
				}
				k -= 1;

				List<Literal> copy = new ArrayList<Literal>(assignment.getAssignedLiterals());
				for (Literal l : copy) {
					Integer dl = decisionLevels.get(l);
					if (dl > k && l.isPositive()) {
						assignment.unassign(l);
					}
				}
				Literal l = guesses.get(k + 1);
				Literal alternativeGuess = l.negation();
				assignment.assign(alternativeGuess);
				decisionLevels.put(alternativeGuess, k);
				implicants.put(alternativeGuess, null);
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
			decisionLevels.put(guessed, currentDL);
			implicants.put(guessed, null);
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
