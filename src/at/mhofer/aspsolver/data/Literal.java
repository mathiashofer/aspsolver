package at.mhofer.aspsolver.data;

public class Literal {

	private boolean positive;

	private Atom atom;

	public Literal(Atom atom, boolean positive) {
		super();
		this.positive = positive;
		this.atom = atom;
	}
	
	/**
	 * Creates a negation of this literal
	 * 
	 * @return
	 */
	public Literal negation() {
		return new Literal(atom, !positive);
	}
	
	@Override
	public String toString() {
		return (positive ? "T" : "F") + atom.getLabel();
	}

	public Atom getAtom() {
		return atom;
	}

	public boolean isPositive() {
		return positive;
	}

	public boolean isNegative() {
		return !positive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atom == null) ? 0 : atom.hashCode());
		result = prime * result + (positive ? 1231 : 1237);
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
		Literal other = (Literal) obj;
		if (atom == null) {
			if (other.atom != null)
				return false;
		} else if (!atom.equals(other.atom))
			return false;
		if (positive != other.positive)
			return false;
		return true;
	}
}
