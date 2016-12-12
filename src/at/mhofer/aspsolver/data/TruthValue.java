package at.mhofer.aspsolver.data;

public enum TruthValue {

	TRUE, FALSE, UNASSIGNED;

	public TruthValue negate() {
		TruthValue value = values()[ordinal()];
		if (value == TruthValue.TRUE) {
			return TruthValue.FALSE;
		} else if (value == TruthValue.FALSE) {
			return TruthValue.TRUE;
		} else {
			return TruthValue.UNASSIGNED;
		}
	}
}
