package at.mhofer.aspsolver.data;

public class Atom {

	private int id;

	private String label;

	public Atom(int id) {
		super();
		this.id = id;
	}

	public Atom(int id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
