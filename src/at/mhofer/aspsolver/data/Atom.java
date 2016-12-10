package at.mhofer.aspsolver.data;

public class Atom {

	private int id;

	private String label;

	private TruthValue truthValue = TruthValue.UNKNOWN;

	private boolean fixed;

	public Atom(int id) {
		super();
		this.id = id;
	}

	public Atom(int id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public Atom(int id, String label, boolean fixed) {
		super();
		this.id = id;
		this.label = label;
		this.fixed = fixed;
	}

	public Atom(int id, String label, TruthValue truthValue, boolean fixed) {
		super();
		this.id = id;
		this.label = label;
		this.truthValue = truthValue;
		this.fixed = fixed;
	}

	public TruthValue getTruthValue() {
		return truthValue;
	}

	public void setTruthValue(TruthValue truthValue) {
		this.truthValue = truthValue;
	}

	public int getId() {
		return id;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isFixed() {
		return fixed;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
