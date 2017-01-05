package at.mhofer.aspsolver.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import at.mhofer.aspsolver.data.err.IllegalAssignmentException;

public class Assignment implements Iterable<Literal>{

	private List<Literal> assignedLiterals;

	private Set<Atom> assignedAtoms = new HashSet<Atom>();

	public Assignment() {
		super();
		this.assignedLiterals = new LinkedList<Literal>();
	}
	
	public Assignment(Assignment toCopy) {
		super();
		this.assignedLiterals = new LinkedList<Literal>(toCopy.assignedLiterals);
		this.assignedAtoms = new HashSet<Atom>(toCopy.assignedAtoms);
	}

	public Assignment(List<Literal> assignedLiterals) {
		super();
		this.assignedLiterals = assignedLiterals;
	}

	public boolean contains(Literal l) {
		return assignedAtoms.contains(l);
	}

	public boolean containsAll(Collection<Literal> ls) {
		return assignedAtoms.containsAll(ls);
	}
	
	/**
	 * 
	 * @param assignment
	 * @return all literals which are only in this assignment but not in the given one
	 */
	public List<Literal> difference(Assignment assignment) {
		List<Literal> difference = new LinkedList<Literal>(assignedLiterals);
		difference.removeAll(assignment.getAssignedLiterals());
		return difference;
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
	
	public boolean isComplete(Collection<Atom> atoms) {
		return assignedAtoms.containsAll(atoms);
	}
	
	public void print() {
		StringBuilder builder = new StringBuilder("{");
		boolean first = true;
		for (Literal l : assignedLiterals) {
			if (first) {
				first = false;
			} else{
				builder.append(", ");
			}
			String prefix = l.isPositive() ? "T" : "F";
			builder.append(prefix + l.getAtom().getLabel());
		}
		builder.append("}");
		System.out.println(builder.toString());
	}

	@Override
	public Iterator<Literal> iterator() {
		return assignedLiterals.iterator();
	}
}
