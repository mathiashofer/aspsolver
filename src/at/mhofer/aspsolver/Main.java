package at.mhofer.aspsolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Program;
import at.mhofer.aspsolver.data.Rule;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.TupleKeyHashMap;
import at.mhofer.aspsolver.data.trans.ClarksCompletionTranslation;
import at.mhofer.aspsolver.data.trans.Translation;
import at.mhofer.aspsolver.solver.CDNLSATSolver;
import at.mhofer.aspsolver.solver.ConflictAnalysis;
import at.mhofer.aspsolver.solver.FirstUIPConflictAnalysis;
import at.mhofer.aspsolver.solver.Propagation;
import at.mhofer.aspsolver.solver.SATSolver;
import at.mhofer.aspsolver.solver.UnitPropagation;

public class Main {

	public static void main(String[] args) throws IOException {
//		File input = new File(args[0]);
//		Parser parser = new LParseParser();
//		Program program = parser.parse(input);
		
		Program program = testProgram1();

		Translation translation = new ClarksCompletionTranslation();
		List<Nogood> instance = translation.translate(program);
		
		System.out.println(instance);

		// generate watchlists
		Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist = new HashMap<Nogood, Tuple<Literal, Literal>>();
		Map<Literal, List<Nogood>> literalWatchlist = new HashMap<Literal, List<Nogood>>();
		for (Nogood nogood : instance) {
			List<Literal> literals = nogood.getLiterals();
			Literal l1 = literals.get(0);
			Literal l2 = literals.get(1);
			nogoodWatchlist.put(nogood, new Tuple<Literal, Literal>(l1, l2));

			List<Nogood> n1 = literalWatchlist.getOrDefault(l1, new LinkedList<Nogood>());
			n1.add(nogood);
			literalWatchlist.put(l1, n1);

			List<Nogood> n2 = literalWatchlist.getOrDefault(l2, new LinkedList<Nogood>());
			n2.add(nogood);
			literalWatchlist.put(l2, n2);
		}

		TupleKeyHashMap<Assignment, Literal, Nogood> implicants = new TupleKeyHashMap<Assignment, Literal, Nogood>();
		TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels = new TupleKeyHashMap<Assignment, Literal, Integer>();
		Propagation propagation = new UnitPropagation(nogoodWatchlist, literalWatchlist, decisionLevels, implicants);
		ConflictAnalysis conflictAnalysis = new FirstUIPConflictAnalysis(decisionLevels, implicants);
		SATSolver solver = new CDNLSATSolver(propagation, conflictAnalysis, program.getAtoms(), decisionLevels,
				implicants);

		Assignment initialAssignment = program.getInitialAssignment();

		solver.solve(instance, initialAssignment);
	}
	
	private static Program testProgram1() {
		Atom a = new Atom(1, "a");
		Atom b = new Atom(2, "b");

		List<Literal> h1 = new ArrayList<Literal>(1);
		h1.add(new Literal(a, true));
		List<Literal> b1 = new ArrayList<Literal>(1);
		b1.add(new Literal(b, true));
		Rule r1 = new Rule(h1, b1);

		List<Literal> h2 = new ArrayList<Literal>(1);
		h2.add(new Literal(b, true));
		List<Literal> b2 = new ArrayList<Literal>(1);
		b2.add(new Literal(a, true));
		Rule r2 = new Rule(h2, b2);

		List<Rule> rules = new ArrayList<Rule>(2);
		rules.add(r1);
		rules.add(r2);

		List<Atom> atoms = new ArrayList<Atom>(2);
		atoms.add(a);
		atoms.add(b);

		return new Program(rules, new Assignment(), atoms);
	}

}
