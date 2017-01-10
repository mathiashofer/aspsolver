package at.mhofer.aspsolver.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nogood implements Iterable<Literal> {

	private List<Literal> literals;

	public Nogood(Nogood nogood) {
		this(nogood.literals, false);
	}

	/**
	 * Expects a clause which should be converted to a nogood
	 * 
	 * @param clause
	 */
	public Nogood(List<Literal> clause) {
		literals = new ArrayList<Literal>(clause.size());
		for (Literal l : clause) {
			literals.add(new Literal(l.getAtom(), !l.isPositive()));
		}
	}

	/**
	 * 
	 * @param literals
	 * @param clause
	 *            converts the clause into a nogood if true, otherwise it doesnt
	 */
	public Nogood(List<Literal> literals, boolean clause) {
		this.literals = new ArrayList<Literal>(literals.size());
		for (Literal l : literals) {
			this.literals.add(new Literal(l.getAtom(), clause ? !l.isPositive() : l.isPositive()));
		}
	}

	public boolean isSatisfiedBy(Assignment assignment) {
		for (Literal l : literals) {
			if (!assignment.contains(l)) {
				return false;
			}
		}
		return true;
	}

	public boolean isFalsifiedBy(Assignment assignment) {
		for (Literal l : literals) {
			if (assignment.contains(l.negation())) {
				return true;
			}
		}
		return false;
	}

	public List<Literal> getUnassignedLiterals(Assignment assignment) {
		List<Literal> unassigned = new LinkedList<Literal>();
		for (Literal l : literals) {
			if (!assignment.isAssigned(l.getAtom())) {
				unassigned.add(l);
			}
		}
		return unassigned;
	}

	/**
	 * 
	 * @param assignment
	 * @param except
	 *            the literal which should not be returned
	 * @return
	 */
	public Literal getFirstUnassignedLiteral(Assignment assignment, Literal except) {
		for (Literal l : literals) {
			if (!assignment.isAssigned(l.getAtom()) && !l.equals(except)) {
				return l;
			}
		}
		return null;
	}

	public List<Literal> getLiterals() {
		return literals;
	}

	@Override
	public Iterator<Literal> iterator() {
		return literals.iterator();
	}

	@Override
	public String toString() {
		return literals.toString();
	}

}
