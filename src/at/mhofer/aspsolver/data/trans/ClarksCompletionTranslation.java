package at.mhofer.aspsolver.data.trans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Atom;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Program;
import at.mhofer.aspsolver.data.Rule;

public class ClarksCompletionTranslation implements Translation {

	/**
	 * Use negative numbers for the ids of the auxiliary atoms
	 */
	private int currentAuxId = -1;

	@Override
	public List<Nogood> translate(Program program) {
		// reset aux ids
		currentAuxId = -1;

		// maps the auxiliary atoms for the rule bodies
		Map<Rule, Atom> auxMap = new HashMap<Rule, Atom>();

		List<Nogood> nogoods = new LinkedList<Nogood>();

		// translate rules
		for (Rule rule : program.getRules()) {
			nogoods.addAll(gamma(rule.getBody()));

			Atom auxAtom = new Atom(currentAuxId, "aux_" + -currentAuxId);
			auxMap.put(rule, auxAtom);

			List<Literal> literals = new LinkedList<Literal>(rule.getHead());
			literals.add(new Literal(auxAtom, false));
			nogoods.add(new Nogood(literals));
		}

		// translate atoms
		for (Atom atom : program.getAtoms()) {
			List<Literal> literals = new LinkedList<Literal>();
			literals.add(new Literal(atom, true));
			for (Rule rule : program.getRulesWithHead(atom)) {
				literals.add(new Literal(auxMap.get(rule), false));
			}
			nogoods.add(new Nogood(literals));
		}

		return nogoods;
	}

	/**
	 * The gamma function as on the slides
	 * 
	 * @param conjunction
	 * @return
	 */
	private List<Nogood> gamma(List<Literal> conjunction) {
		List<Nogood> nogoods = new ArrayList<Nogood>(2);

		Atom auxAtom = new Atom(currentAuxId, "aux_" + -currentAuxId);
		currentAuxId--;

		List<Literal> literals = new LinkedList<Literal>(conjunction);
		literals.add(new Literal(auxAtom, false));
		nogoods.add(new Nogood(literals));

		literals = new LinkedList<Literal>();
		Literal auxLiteral = new Literal(auxAtom, true);
		for (Literal l : conjunction) {
			literals.add(auxLiteral);
			literals.add(l.negation());
		}
		nogoods.add(new Nogood(literals));

		return nogoods;
	}

}
