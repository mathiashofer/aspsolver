package at.mhofer.aspsolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Program;
import at.mhofer.aspsolver.data.Rule;
import at.mhofer.aspsolver.data.trans.ClarksCompletionTranslation;
import at.mhofer.aspsolver.data.trans.Translation;
import at.mhofer.aspsolver.solver.CDNLSATSolver;
import at.mhofer.aspsolver.solver.FirstUIPConflictAnalysis;
import at.mhofer.aspsolver.solver.SATSolver;
import at.mhofer.aspsolver.solver.UnitPropagationFactory;

public class Main {

	public static void main(String[] args) throws IOException {
		// File input = new File(args[0]);
		// Parser parser = new LParseParser();
		// Program program = parser.parse(input);

		Program program = testProgram1();

		Translation translation = new ClarksCompletionTranslation();
		List<Nogood> instance = translation.translate(program);

		System.out.println(instance);

		SATSolver solver = new CDNLSATSolver(new UnitPropagationFactory(), new FirstUIPConflictAnalysis(),
				program.getAtoms());

//		 SATSolver solver = new DPLLSATSolver(new UnitPropagationFactory(),
//		 program.getAtoms());

		Assignment initialAssignment = program.getInitialAssignment();

		List<Assignment> result = solver.solveAll(instance, initialAssignment);
		// Assignment result = solver.solve(instance, initialAssignment);
		System.out.println(result);
	}

	/**
	 * a :- b. b :- a.
	 */
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
