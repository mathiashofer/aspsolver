package at.mhofer.aspsolver.data;

public class Literal {

	private boolean positive;

	private Atom atom;

	public Literal(Atom atom, boolean positive) {
		super();
		this.positive = positive;
		this.atom = atom;
	}

	public TruthValue getTruthValue() {
		return positive ? atom.getTruthValue() : atom.getTruthValue().negate();
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

}
