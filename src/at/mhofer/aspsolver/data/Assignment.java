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
		return assignedLiterals.contains(l);
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
					"Atom with id " + literal.getAtom().getId() + " already assigned!");
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
	
	@Override
	public String toString() {
		return assignedLiterals.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignedAtoms == null) ? 0 : assignedAtoms.hashCode());
		result = prime * result + ((assignedLiterals == null) ? 0 : assignedLiterals.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assignment other = (Assignment) obj;
		if (assignedAtoms == null) {
			if (other.assignedAtoms != null)
				return false;
		} else if (!assignedAtoms.equals(other.assignedAtoms))
			return false;
		if (assignedLiterals == null) {
			if (other.assignedLiterals != null)
				return false;
		} else if (!assignedLiterals.equals(other.assignedLiterals))
			return false;
		return true;
	}
	
	
}
