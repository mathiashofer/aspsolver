package at.mhofer.aspsolver.data;

import java.util.ArrayList;
import java.util.List;

public class Nogood {

	private List<Literal> literals;

	public Nogood(List<Literal> clause) {
		literals = new ArrayList<Literal>(clause.size());
		for (Literal l : clause) {
			literals.add(new Literal(l.getAtom(), !l.isPositive()));
		}
	}

	public TruthValue getTruthValue() {
		return TruthValue.UNKNOWN;
	}

	public List<Literal> getLiterals() {
		return literals;
	}
}
