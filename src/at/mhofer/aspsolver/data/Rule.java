package at.mhofer.aspsolver.data;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	private List<Literal> head;
	
	private List<Literal> body;
	
	public Rule(Literal head, List<Literal> body) {
		super();
		this.head = new ArrayList<Literal>(0);
		this.head.add(head);
		this.body = body;
	}

	public Rule(List<Literal> head, List<Literal> body) {
		super();
		this.head = head;
		this.body = body;
	}

	public List<Literal> getHead() {
		return head;
	}

	public void setHead(List<Literal> head) {
		this.head = head;
	}

	public List<Literal> getBody() {
		return body;
	}

	public void setBody(List<Literal> body) {
		this.body = body;
	}
}
