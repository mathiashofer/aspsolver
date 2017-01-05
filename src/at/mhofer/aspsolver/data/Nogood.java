package at.mhofer.aspsolver.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nogood implements Iterable<Literal>{

	private List<Literal> literals;
	
	public Nogood(Nogood nogood) {
		this(nogood.literals);
	}

	public Nogood(List<Literal> clause) {
		literals = new ArrayList<Literal>(clause.size());
		for (Literal l : clause) {
			literals.add(new Literal(l.getAtom(), !l.isPositive()));
		}
	}

	public boolean isSatisfiedBy(Assignment assignment) {
		return assignment.containsAll(literals);
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
	 * @param except the literal which should not be returned
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
}
