package at.mhofer.aspsolver.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Program;
import at.mhofer.aspsolver.data.Rule;

public class LParseParser implements Parser {

	private static final String RULE_TYPE_NORMAL = "1";
	// private static final int RULE_TYPE_CHOICE = 3;
	// private static final int RULE_TYPE_DISJUNCTIVE = 8;

	public Program parse(File file) throws IOException {
		List<Rule> rules = new LinkedList<Rule>();
		// contains the always true resp. always false literals
		Assignment assignment = new Assignment();
		// maps atomids to atoms
		Map<Integer, Atom> atoms = new HashMap<Integer, Atom>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			ParsingPart currentPart = nextPart(ParsingPart.BEGIN);
			// interal atom
			atoms.put(1, new Atom(1, "_false"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] chunks = line.split("\\s+");

				// a line with just "0" indicates the end of each part
				if (chunks[0].equals("0")) {
					currentPart = nextPart(currentPart);
					// skip the "B+" resp. "B-" line
					if (currentPart == ParsingPart.ALWAYS_FALSE || currentPart == ParsingPart.ALWAYS_TRUE) {
						reader.readLine();
					}
					continue;
				}

				if (currentPart == ParsingPart.RULE) {
					if (chunks[0].equals(RULE_TYPE_NORMAL)) {
						int headId = Integer.parseInt(chunks[1]);
						int totalBodyLiterals = Integer.parseInt(chunks[2]);
						int negativeBodyLiterals = Integer.parseInt(chunks[3]);
						int positiveBodyLiterals = totalBodyLiterals - negativeBodyLiterals;

						// head
						Atom head = atoms.putIfAbsent(headId, new Atom(headId));

						List<Literal> literals = new ArrayList<Literal>(totalBodyLiterals);

						// negative body literals
						int negativeLiteralsOffset = 4;
						for (int i = negativeLiteralsOffset; i < negativeLiteralsOffset + negativeBodyLiterals; i++) {
							int atomId = Integer.parseInt(chunks[i]);
							Atom atom = atoms.putIfAbsent(atomId, new Atom(atomId));
							literals.add(new Literal(atom, false));
						}

						// positive body literals
						int positiveLiteralsOffset = negativeLiteralsOffset + negativeBodyLiterals;
						for (int i = positiveLiteralsOffset; i < positiveLiteralsOffset + positiveBodyLiterals; i++) {
							int atomId = Integer.parseInt(chunks[i]);
							Atom atom = atoms.putIfAbsent(atomId, new Atom(atomId));
							literals.add(new Literal(atom, true));
						}

						rules.add(new Rule(new Literal(head, true), literals));
					} else {
						throw new IOException("Illegal file format, only normal programs are supported!");
					}
				} else if (currentPart == ParsingPart.DICTIONARY) {
					int atomId = Integer.parseInt(chunks[0]);
					String label = chunks[1];
					Atom atom = atoms.get(atomId);
					if (atom != null) {
						atom.setLabel(label);
					} else {
						throw new IOException("Illegal file format, dictionary references non existing atomid!");
					}

				} else if (currentPart == ParsingPart.ALWAYS_TRUE) {
					int atomId = Integer.parseInt(chunks[0]);
					Literal alwaysTrue = new Literal(atoms.get(atomId), true);
					assignment.assign(alwaysTrue);
				} else if (currentPart == ParsingPart.ALWAYS_FALSE) {
					int atomId = Integer.parseInt(chunks[0]);
					Literal alwaysFalse = new Literal(atoms.get(atomId), false);
					assignment.assign(alwaysFalse);
				}
			}

			reader.close();
		}

		return new Program(rules, assignment, new ArrayList<Atom>(atoms.values()));
	}

	private ParsingPart nextPart(ParsingPart currentPart) {
		ParsingPart nextPart = null;
		if (currentPart == ParsingPart.BEGIN)
			nextPart = ParsingPart.RULE;
		else if (currentPart == ParsingPart.RULE)
			nextPart = ParsingPart.DICTIONARY;
		else if (currentPart == ParsingPart.DICTIONARY)
			nextPart = ParsingPart.ALWAYS_TRUE;
		else if (currentPart == ParsingPart.ALWAYS_TRUE)
			nextPart = ParsingPart.ALWAYS_FALSE;
		else if (currentPart == ParsingPart.ALWAYS_FALSE)
			nextPart = ParsingPart.END;
		return nextPart;
	}

	private enum ParsingPart {
		BEGIN, RULE, DICTIONARY, ALWAYS_TRUE, ALWAYS_FALSE, END
	}
}
