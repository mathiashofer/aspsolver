package at.mhofer.aspsolver.data;

import java.util.List;

public class Rule {

	private Atom head;
	
	private List<Literal> body;

	public Rule(Atom head, List<Literal> body) {
		super();
		this.head = head;
		this.body = body;
	}

	public Atom getHead() {
		return head;
	}

	public void setHead(Atom head) {
		this.head = head;
	}

	public List<Literal> getBody() {
		return body;
	}

	public void setBody(List<Literal> body) {
		this.body = body;
	}
}
