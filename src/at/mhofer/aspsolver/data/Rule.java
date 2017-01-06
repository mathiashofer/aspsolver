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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
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
		Rule other = (Rule) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		return true;
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
