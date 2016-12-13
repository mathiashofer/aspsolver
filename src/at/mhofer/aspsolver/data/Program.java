package at.mhofer.aspsolver.data;

import java.util.List;

/**
 * Encapsulates the parsed information of the answer set program
 * 
 * @author Mathias
 *
 */
public class Program {

	private List<Rule> rules;

	/**
	 * Contains always true resp. always false literals
	 */
	private Assignment initialAssignment;

	private List<Atom> atoms;

	public Program(List<Rule> rules, Assignment initialAssignment, List<Atom> atoms) {
		super();
		this.rules = rules;
		this.initialAssignment = initialAssignment;
		this.atoms = atoms;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public Assignment getInitialAssignment() {
		return initialAssignment;
	}

	public void setInitialAssignment(Assignment initialAssignment) {
		this.initialAssignment = initialAssignment;
	}

	public List<Atom> getAtoms() {
		return atoms;
	}

	public void setAtoms(List<Atom> atoms) {
		this.atoms = atoms;
	}

}
