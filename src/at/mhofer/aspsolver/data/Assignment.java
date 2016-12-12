package at.mhofer.aspsolver.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import at.mhofer.aspsolver.data.err.IllegalAssignmentException;

public class Assignment {

	private List<Literal> assignedLiterals;

	/**
	 * Offers constant time for contains()
	 */
	private Set<Atom> assignedAtoms = new HashSet<Atom>();

	public Assignment() {
		super();
		this.assignedLiterals = new LinkedList<Literal>();
	}

	public Assignment(List<Literal> assignedLiterals) {
		super();
		this.assignedLiterals = assignedLiterals;
	}

	public boolean contains(Object o) {
		return assignedAtoms.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return assignedAtoms.containsAll(c);
	}

	/**
	 * 
	 * @param literal
	 * @throws IllegalAssignmentException if the atom is already assigned
	 */
	public void assign(Literal literal) {
		if (assignedAtoms.contains(literal.getAtom())) {
			throw new IllegalAssignmentException(
					"Atom with id " + literal.getAtom().getId() + " already assignedLiterals!");
		}
		this.assignedLiterals.add(literal);
		assignedAtoms.add(literal.getAtom());
	}
	
	/**
	 * Does nothing if the given literal is not assigned
	 * 
	 * @param literal
	 */
	public void unassign(Literal literal) {
		assignedLiterals.remove(literal);
		assignedAtoms.remove(literal.getAtom());
	}

	public boolean isAssigned(Atom atom) {
		return assignedAtoms.contains(atom);
	}

	public List<Literal> getAssignedLiterals() {
		return assignedLiterals;
	}
}
