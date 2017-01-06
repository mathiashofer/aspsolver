package at.mhofer.aspsolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Program;
import at.mhofer.aspsolver.data.Rule;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.TupleKeyHashMap;
import at.mhofer.aspsolver.data.trans.ClarksCompletionTranslation;
import at.mhofer.aspsolver.data.trans.Translation;
import at.mhofer.aspsolver.io.LParseParser;
import at.mhofer.aspsolver.io.Parser;
import at.mhofer.aspsolver.solver.CDNLSATSolver;
import at.mhofer.aspsolver.solver.ConflictAnalysis;
import at.mhofer.aspsolver.solver.FirstUIPConflictAnalysis;
import at.mhofer.aspsolver.solver.Propagation;
import at.mhofer.aspsolver.solver.SATSolver;
import at.mhofer.aspsolver.solver.UnitPropagation;

public class Main {

	public static void main(String[] args) throws IOException {
		File input = new File(args[0]);
		Parser parser = new LParseParser();
		Program program = parser.parse(input);

		//TODO generate watchlists
		Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist = null;
		Map<Literal, List<Nogood>> literalWatchlist = null;

		TupleKeyHashMap<Assignment, Literal, Nogood> implicants = new TupleKeyHashMap<Assignment, Literal, Nogood>();
		TupleKeyHashMap<Assignment, Literal, Integer> decisionLevels = new TupleKeyHashMap<Assignment, Literal, Integer>();
		Propagation propagation = new UnitPropagation(nogoodWatchlist, literalWatchlist, decisionLevels, implicants);
		ConflictAnalysis conflictAnalysis = new FirstUIPConflictAnalysis(decisionLevels, implicants);
		SATSolver solver = new CDNLSATSolver(propagation, conflictAnalysis, program.getAtoms(), decisionLevels,
				implicants);

		Translation translation = new ClarksCompletionTranslation();
		List<Nogood> instance = translation.translate(program);

		Assignment initialAssignment = program.getInitialAssignment();

		solver.solve(instance, initialAssignment);
	}

}
