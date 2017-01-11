package at.mhofer.aspsolver.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Tuple;

public class CDNLSATSolver implements SATSolver {

	private PropagationFactory propagationFactory;

	private ConflictAnalysis conflictAnalysis;

	// TODO change datastructure
	private Map<Integer, Literal> guesses = new HashMap<Integer, Literal>();

	public CDNLSATSolver(PropagationFactory propagationFactory, ConflictAnalysis conflictAnalysis) {
		this.propagationFactory = propagationFactory;
		this.conflictAnalysis = conflictAnalysis;
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
		Set<Atom> atoms = new HashSet<Atom>();
		for (Nogood n : instance) {
			for (Literal l : n) {
				atoms.add(l.getAtom());
			}
		}
		return solve(instance, initialAssignment, null, 0, propagation, decisionLevels, implicants, atoms);
	}

	@Override
	public List<Assignment> solveAll(List<Nogood> instance, Assignment initialAssignment) {
		List<Assignment> results = new LinkedList<Assignment>();
		List<Nogood> modifiedInstance = new LinkedList<Nogood>(instance);
		Propagation propagation = propagationFactory.create(modifiedInstance);
		HashMap<Literal, Nogood> implicants = new HashMap<Literal, Nogood>();
		HashMap<Literal, Integer> decisionLevels = new HashMap<Literal, Integer>();
		Assignment result = null;
		List<Nogood> oldResults = new LinkedList<Nogood>();
		Set<Atom> atoms = new HashSet<Atom>();
		for (Nogood n : instance) {
			for (Literal l : n) {
				atoms.add(l.getAtom());
			}
		}
		while ((result = solve(modifiedInstance, new Assignment(initialAssignment), null, 0, propagation,
				decisionLevels, implicants, atoms)) != null) {		
			List<Literal> literals = new ArrayList<Literal>();
			for (Literal l : result) {
				if (l.getAtom().getId() > 0) {
					literals.add(l);
				}
			}			
			Nogood nogood = new Nogood(literals, false);
			oldResults.add(nogood);
			modifiedInstance = new LinkedList<Nogood>(instance);
			modifiedInstance.addAll(oldResults);

			implicants = new HashMap<Literal, Nogood>();
			decisionLevels = new HashMap<Literal, Integer>();
			propagation = propagationFactory.create(modifiedInstance);
			results.add(result);
		}

		return results;
	}

	private Assignment solve(List<Nogood> instance, Assignment initialAssignment, Literal recentlyAssigned,
			int currentDL, Propagation propagation, HashMap<Literal, Integer> decisionLevels,
			HashMap<Literal, Nogood> implicants, Set<Atom> atoms) {
		Assignment assignment = propagation.propagate(instance, initialAssignment, recentlyAssigned, decisionLevels,
				implicants);

		for (Nogood n : instance) {
			if (n.isSatisfiedBy(assignment) && currentDL == 0) {
				return null;
			} else if (n.isSatisfiedBy(assignment) && currentDL > 0) {
				// backtracking
				Tuple<Nogood, Integer> analysisResult = conflictAnalysis.analyse(n, instance, assignment,
						decisionLevels, implicants);
				Nogood learnedNogood = analysisResult.getValue1();
				int backtrackDL = analysisResult.getValue2();
				instance.add(learnedNogood);
				// update watchlists
				propagation = propagationFactory.create(instance);

				List<Literal> copy = new ArrayList<Literal>(assignment.getAssignedLiterals());
				for (Literal l : copy) {
					if (decisionLevels.get(l) > backtrackDL && l.isPositive()) {
						assignment.unassign(l);
					}
				}

				return solve(instance, assignment, recentlyAssigned, backtrackDL, propagation, decisionLevels,
						implicants, atoms);
			}
		}

		if (assignment.isComplete(atoms)) {
			return assignment;
		} else {
			// guess
			Literal guessed = select(assignment, atoms);
			currentDL++;
			guesses.put(currentDL, guessed);
			decisionLevels.put(guessed, currentDL);
			implicants.put(guessed, null);
			assignment.assign(guessed);
			return solve(instance, assignment, guessed, currentDL, propagation, decisionLevels, implicants, atoms);
		}
	}

	/**
	 * 
	 * @param assignment
	 * @return
	 */
	private Literal select(Assignment assignment, Set<Atom> atoms) {
		for (Atom a : atoms) {
			if (!assignment.isAssigned(a)) {
				return new Literal(a, true);
			}
		}
		return null;
	}
}
